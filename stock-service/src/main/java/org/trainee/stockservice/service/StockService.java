package org.trainee.stockservice.service;

import jakarta.transaction.Transactional;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.trainee.stockservice.client.ProductClient;
import org.trainee.stockservice.dto.StockProductRequest;
import org.trainee.stockservice.dto.StockProductResponse;
import org.trainee.stockservice.dto.StockRequest;
import org.trainee.stockservice.dto.StockResponse;
import org.trainee.stockservice.exception.StockNotFoundException;
import org.trainee.stockservice.key.StockProductKey;
import org.trainee.stockservice.mapper.StockMapper;
import org.trainee.stockservice.model.Stock;
import org.trainee.stockservice.model.StockProduct;
import org.trainee.stockservice.repository.StockProductRepository;
import org.trainee.stockservice.repository.StockRepository;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


@Service
public class StockService {
    private final ProductClient productClient;
    private final StockRepository stockRepository;
    private final StockProductRepository stockProductRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ConcurrentMap<Long, CompletableFuture<Long>> productFutures = new ConcurrentHashMap<>();
    private final static String STOCK_ERROR_MESSAGE = "Entity with name: {0} and id {1} not found!";
    private String REQUEST_TOPIC = "product-request-topic";

    public StockService(ProductClient productClient, StockRepository stockRepository, StockProductRepository stockProductRepository, KafkaTemplate<String, String> kafkaTemplate) {
        this.productClient = productClient;
        this.stockRepository = stockRepository;
        this.stockProductRepository = stockProductRepository;
        this.kafkaTemplate = kafkaTemplate;
    }


    public List<StockResponse> getAllStocks() {
        return stockRepository.findAll().stream().map(StockMapper::stockToResponse).toList();
    }

    public StockResponse getStockById(Long id) {
        String message = MessageFormat.format(STOCK_ERROR_MESSAGE, productClient.getStockType(), id);
        Stock stock = stockRepository.findById(id).orElseThrow(() -> new StockNotFoundException(message));
        return StockMapper.stockToResponse(stock);
    }

    public StockResponse createStock(StockRequest stockRequest) {
        return StockResponse.builder()
                .name(stockRequest.getName()).build();
    }

    @Transactional
    public StockProductResponse addProduct(Long stockId, StockProductRequest stockProductRequest) {
        String stockMessage = MessageFormat.format(STOCK_ERROR_MESSAGE, productClient.getStockType(), stockId);
        String productMessage = MessageFormat.format(STOCK_ERROR_MESSAGE, productClient.getProductType(), stockProductRequest.getProductId());
        stockRepository.findById(stockId)
                .orElseThrow(() -> new StockNotFoundException(stockMessage));

        Long id = requestProduct(stockProductRequest.getProductId());
        if (id == null) {
            throw new StockNotFoundException(productMessage);
        }
        StockProduct stockProductCheck = stockProductRepository.findByProductIdAndStockId(id, stockId).orElseThrow(() -> new StockNotFoundException(productMessage));
        StockProduct stockProduct;
        if (stockProductCheck != null) {
            stockProductCheck.setQuantity(stockProductCheck.getQuantity() + stockProductRequest.getQuantity());
            updateStockProductQuantity(id, stockId, stockProductRequest);
            return StockMapper.stockProductToResponse(stockProductCheck);
        } else {
            stockProduct = StockMapper.requestToStockProduct(stockProductRequest);
            stockProduct.setStockId(stockId);
            stockProduct.setProductId(id);
            stockProduct.setQuantity(stockProductRequest.getQuantity());
            stockProductRepository.save(stockProduct);
            return StockMapper.stockProductToResponse(stockProduct);
        }
    }

    public List<StockProductResponse> getProductsInStock(Long stockId) {
        List<StockProduct> list = stockProductRepository.findByStockId(stockId);
        List<StockProductResponse> responses = new ArrayList<>();
        for (StockProduct product : list) {
            responses.add(StockMapper.stockProductToResponse(product));
        }
        return responses;
    }

    public void removeProduct(Long stockId, Long productId) {
        String stockMessage = MessageFormat.format(STOCK_ERROR_MESSAGE, productClient.getStockType(), stockId);
        String productMessage = MessageFormat.format(STOCK_ERROR_MESSAGE, productClient.getProductType(), productId);
        stockRepository.findById(stockId)
                .orElseThrow(() -> new StockNotFoundException(stockMessage));

        Long id = requestProduct(productId);
        if (id == null) {
            throw new StockNotFoundException(productMessage);
        }
        StockProductKey key = new StockProductKey(stockId, productId);
        stockProductRepository.deleteById(key);
    }

    public boolean isInStock(Long stockId, Long productId) {
        StockProductKey id = new StockProductKey(stockId, productId);
        return stockProductRepository.existsById(id);
    }

    public StockProductResponse updateStockProductQuantity(Long stockId, Long productId, StockProductRequest stockProductRequest) {
        return stockProductRepository.findByProductIdAndStockId(productId, stockId)
                .map(existingStock -> {
                            StockProduct updatedStock = StockMapper.updateStockProductFromRequest(existingStock, stockProductRequest);
                            StockProduct savedStock = stockProductRepository.save(updatedStock);
                            return StockMapper.stockProductToResponse(savedStock);
                        }
                ).orElseThrow(() -> new StockNotFoundException(STOCK_ERROR_MESSAGE));
    }

    @Transactional
    public StockProduct increaseProductQuantity(Long productId, Long stockId, int quantityChange) {
        String productMessage = MessageFormat.format(STOCK_ERROR_MESSAGE, productClient.getProductType(), productId);
        StockProduct stockProduct = stockProductRepository.findByProductIdAndStockId(productId, stockId)
                .orElseThrow(() -> new StockNotFoundException(productMessage));

        int newQuantity = stockProduct.getQuantity() + quantityChange;
        if (newQuantity < 0) {
            throw new IllegalStateException("Not enough stock available");
        }
        stockProduct.setQuantity(newQuantity);
        return stockProductRepository.save(stockProduct);
    }

    public List<StockProductResponse> getProducts(Long productId) {
        String productMessage = MessageFormat.format(STOCK_ERROR_MESSAGE, productClient.getProductType(), productId);
        Long id = requestProduct(productId);
        List<StockProduct> stockProducts = stockProductRepository.findAllByProductId(id);
        List<StockProductResponse> responses = new ArrayList<>();
        for (StockProduct stockProduct : stockProducts) {
            StockMapper.stockProductToResponse(stockProduct);
            responses.add(StockMapper.stockProductToResponse(stockProduct));
        }
        return responses;
    }

    private Long requestProduct(Long productId) {
        kafkaTemplate.send(REQUEST_TOPIC, productId.toString());
        CompletableFuture<Long> future = new CompletableFuture<>();
        productFutures.put(productId, future);
        try {
            return future.get();
        } catch (Exception e) {
            throw new RuntimeException("Не удалось получить данные о продукте", e);
        } finally {
            productFutures.remove(productId);
        }
    }

    @KafkaListener(topics = "product-response-topic", groupId = "stock-service-group")
    public void consumeProductResponse(Long id) {
        CompletableFuture<Long> future = productFutures.get(id);
        if (future != null) {
            future.complete(id);
        }

    }
}

package org.trainee.stockservice.service;

import jakarta.transaction.Transactional;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.trainee.productservice.enums.EntityType;
import org.trainee.productservice.exception.EntityNotFoundException;
import org.trainee.stockservice.dto.ProductDto;
import org.trainee.stockservice.dto.StockProductRequest;
import org.trainee.stockservice.dto.StockProductResponse;
import org.trainee.stockservice.dto.StockRequest;
import org.trainee.stockservice.dto.StockResponse;
import org.trainee.stockservice.key.StockProductKey;
import org.trainee.stockservice.mapper.StockMapper;
import org.trainee.stockservice.model.Stock;
import org.trainee.stockservice.model.StockProduct;
import org.trainee.stockservice.repository.StockProductRepository;
import org.trainee.stockservice.repository.StockRepository;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;


@Service
public class StockService {
    private final StockRepository stockRepository;
    private final StockProductRepository stockProductRepository;
    private final KafkaTemplate<String, Long> kafkaTemplate;
    private final ProductConsumerService productConsumerService;
    final static String STOCK_ERROR_MESSAGE = "Entity with name: {0} and id {1} not found!";

    public StockService(StockRepository stockRepository, StockProductRepository stockProductRepository, KafkaTemplate<String, Long> kafkaTemplate, ProductConsumerService productConsumerService) {
        this.stockRepository = stockRepository;
        this.stockProductRepository = stockProductRepository;
        this.kafkaTemplate = kafkaTemplate;
        this.productConsumerService = productConsumerService;
    }

    public List<StockResponse> getAllStocks() {
        return stockRepository.findAll().stream().map(StockMapper::stockToResponse).toList();
    }

    public StockResponse getStockById(Long id) {
        Stock stock = stockRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("m"));
        return StockMapper.stockToResponse(stock);
    }

    public StockResponse createStock(StockRequest stockRequest) {
        return StockResponse.builder()
                .name(stockRequest.getName()).build();
    }

    @Transactional
    public StockProductResponse addProduct(Long stockId, StockProductRequest stockProductRequest) {
        String stockMessage = MessageFormat.format(STOCK_ERROR_MESSAGE, EntityType.STOCK.name(), stockId);
        String productMessage = MessageFormat.format(STOCK_ERROR_MESSAGE, EntityType.PRODUCT.name(), stockProductRequest.getProductId());
        stockRepository.findById(stockId)
                .orElseThrow(() -> new EntityNotFoundException(stockMessage));

        kafkaTemplate.send("prouct-request-topic", stockProductRequest.getProductId());

        ProductDto product = productConsumerService.getProductResponse(stockProductRequest.getProductId());
        if (product == null) {
            throw new EntityNotFoundException(productMessage);
        }
        StockProduct stockProductCheck = stockProductRepository.findByProductIdAndStockId(product.getId(), stockId).orElseThrow(() -> new EntityNotFoundException(productMessage));
        StockProduct stockProduct;
        if (stockProductCheck != null) {
            stockProductCheck.setQuantity(stockProductCheck.getQuantity() + stockProductRequest.getQuantity());
            updateStockProductQuantity(product.getId(), stockId, stockProductRequest);
            return StockMapper.stockProductToResponse(stockProductCheck);
        } else {
            stockProduct = StockMapper.requestToStockProduct(stockProductRequest);
            stockProduct.setStockId(stockId);
            stockProduct.setProductId(product.getId());
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
        String stockMessage = MessageFormat.format(STOCK_ERROR_MESSAGE, EntityType.STOCK.name(), stockId);
        String productMessage = MessageFormat.format(STOCK_ERROR_MESSAGE, EntityType.PRODUCT.name(), productId);
        stockRepository.findById(stockId)
                .orElseThrow(() -> new EntityNotFoundException(stockMessage));

        kafkaTemplate.send("product-request-topic", productId);

        ProductDto product = productConsumerService.getProductResponse(productId);
        if (product == null) {
            throw new EntityNotFoundException(productMessage);
        }
        StockProductKey id = new StockProductKey(stockId, productId);
        stockProductRepository.deleteById(id);
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
                ).orElseThrow(() -> new EntityNotFoundException(STOCK_ERROR_MESSAGE));
    }

    @Transactional
    public StockProduct increaseProductQuantity(Long productId, Long stockId, int quantityChange) {
        String productMessage = MessageFormat.format(STOCK_ERROR_MESSAGE, EntityType.PRODUCT.name(), productId);
        StockProduct stockProduct = stockProductRepository.findByProductIdAndStockId(productId, stockId)
                .orElseThrow(() -> new EntityNotFoundException(productMessage));

        int newQuantity = stockProduct.getQuantity() + quantityChange;
        if (newQuantity < 0) {
            throw new IllegalStateException("Not enough stock available");
        }
        stockProduct.setQuantity(newQuantity);
        return stockProductRepository.save(stockProduct);
    }

    public StockProductResponse getProduct(Long productId) {
        String productMessage = MessageFormat.format(STOCK_ERROR_MESSAGE, EntityType.PRODUCT.name(), productId);
        kafkaTemplate.send("product-request-topic", productId);
        ProductDto product = productConsumerService.getProductResponse(productId);
        if (product == null) {
            throw new EntityNotFoundException(productMessage);
        }

        StockProduct stockProduct = stockProductRepository.findByProductId(productId)
                .orElseThrow(() -> new EntityNotFoundException(productMessage));

        StockProductResponse dto = new StockProductResponse();
        dto.setProductId(product.getId());
        dto.setQuantity(stockProduct.getQuantity());
        return dto;
    }
}

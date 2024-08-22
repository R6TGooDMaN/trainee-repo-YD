package org.trainee.stockservice.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.trainee.productservice.enums.EntityType;
import org.trainee.productservice.exception.EntityNotFoundException;
import org.trainee.productservice.model.Product;
import org.trainee.stockservice.dto.StockProductRequest;
import org.trainee.stockservice.dto.StockProductResponse;
import org.trainee.stockservice.dto.StockResponse;
import org.trainee.stockservice.key.StockProductKey;
import org.trainee.stockservice.mapper.StockMapper;
import org.trainee.stockservice.model.Stock;
import org.trainee.stockservice.model.StockProduct;
import org.trainee.stockservice.repository.StockProductRepository;
import org.trainee.stockservice.repository.StockRepository;

import java.text.MessageFormat;
import java.util.List;


@Service
public class StockService {
    private final StockRepository stockRepository;
    private final StockProductRepository stockProductRepository;
    private RestTemplate restTemplate;
    String productServiceUrl = "http://localhost:8080/api/v1/product/";
    final static String STOCK_ERROR_MESSAGE = "Entity with name: {0} and id {1} not found!";

    @Autowired
    public StockService(StockRepository stockRepository, StockProductRepository stockProductRepository, RestTemplate restTemplate) {
        this.stockRepository = stockRepository;
        this.stockProductRepository = stockProductRepository;
        this.restTemplate = restTemplate;
    }

    public List<StockResponse> getAllStocks() {
        return stockRepository.findAll().stream().map(StockMapper::stockToResponse).toList();
    }

    public StockResponse getStockById(Long id) {
        Stock stock = stockRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("m"));
        return StockMapper.stockToResponse(stock);
    }

    @Transactional
    public StockResponse addProduct(Long stockId, StockProductRequest stockProductRequest) {
        String stockMessage = MessageFormat.format(STOCK_ERROR_MESSAGE, EntityType.STOCK.name(), stockId);
        String productMessage = MessageFormat.format(STOCK_ERROR_MESSAGE, EntityType.PRODUCT.name(), stockProductRequest.getProductId());

        Stock stock = stockRepository.findById(stockId)
                .orElseThrow(() -> new EntityNotFoundException(stockMessage));
        Product product = restTemplate.getForObject(productServiceUrl + stockProductRequest.getProductId(), Product.class);
        if (product == null) {
            throw new EntityNotFoundException(productMessage);
        }
        StockProduct stockProduct = StockMapper.requestToStockProduct(stockProductRequest);
        stockProduct.setStock(stock);
        stockProduct.setProduct(product);

        stock.getStockProducts().add(stockProduct);
        stockProductRepository.save(stockProduct);
        return StockMapper.stockToResponse(stock);
    }

    public void removeProduct(Long stockId, Long productId) {
        String stockMessage = MessageFormat.format(STOCK_ERROR_MESSAGE, EntityType.STOCK.name(), stockId);
        String productMessage = MessageFormat.format(STOCK_ERROR_MESSAGE, EntityType.PRODUCT.name(), productId);
        stockRepository.findById(stockId)
                .orElseThrow(() -> new EntityNotFoundException(stockMessage));
        Product product = restTemplate.getForObject(productServiceUrl + productId, Product.class);
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

    @Transactional
    public void updateProductQuantity(Long productId, int quantityChange) {
        String productMessage = MessageFormat.format(STOCK_ERROR_MESSAGE, EntityType.PRODUCT.name(), productId);
        StockProduct stockProduct = stockProductRepository.findByProductId(productId)
                .orElseThrow(() -> new EntityNotFoundException(productMessage));

        int newQuantity = stockProduct.getQuantity() + quantityChange;
        if (newQuantity < 0) {
            throw new IllegalStateException("Not enough stock available");
        }
        stockProduct.setQuantity(newQuantity);
        stockProductRepository.save(stockProduct);
    }
    public StockProductResponse getProduct(Long productId) {
        String productMessage = MessageFormat.format(STOCK_ERROR_MESSAGE, EntityType.PRODUCT.name(), productId);
        ResponseEntity<Product> response = restTemplate.getForEntity(productServiceUrl + productId, Product.class);
        Product product = response.getBody();

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

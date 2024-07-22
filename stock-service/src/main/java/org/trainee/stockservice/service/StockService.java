package org.trainee.stockservice.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.trainee.productservice.exception.EntityNotFoundException;
import org.trainee.productservice.model.Product;
import org.trainee.stockservice.dto.StockProductRequest;
import org.trainee.stockservice.dto.StockResponse;
import org.trainee.stockservice.key.StockProductKey;
import org.trainee.stockservice.mapper.StockMapper;
import org.trainee.stockservice.model.Stock;
import org.trainee.stockservice.model.StockProduct;
import org.trainee.stockservice.repository.StockProductRepository;
import org.trainee.stockservice.repository.StockRepository;

import java.util.List;
import java.util.Optional;


@Service
public class StockService {
    private final StockRepository stockRepository;
    private final StockProductRepository stockProductRepository;
    private RestTemplate restTemplate;
    String productServiceUrl = "http://localhost:8080/api/v1/product";
    final String STOCK_ERROR_MESSAGE = "Stock not found!";
    final String PRODUCT_ERROR_MESSAGE = "Product not found!";

    @Autowired
    public StockService(StockRepository stockRepository, StockProductRepository stockProductRepository, RestTemplate restTemplate) {
        this.stockRepository = stockRepository;
        this.stockProductRepository = stockProductRepository;
        this.restTemplate = restTemplate;
    }

    public List<StockResponse> getAllStocks() {
        return stockRepository.findAll().stream().map(StockMapper::stockToResponse).toList();
    }

    public Optional<StockResponse> getStockById(Long id) {
        return stockRepository.findById(id)
                .map(StockMapper::stockToResponse);
    }

    @Transactional
    public StockResponse addProduct(Long stockId, StockProductRequest stockProductRequest) {
        Stock stock = stockRepository.findById(stockId)
                .orElseThrow(() -> new EntityNotFoundException(STOCK_ERROR_MESSAGE));
        Product product = restTemplate.getForObject(productServiceUrl + "/" + stockProductRequest.getProductId(), Product.class);
        if (product == null) {
            throw new EntityNotFoundException(PRODUCT_ERROR_MESSAGE);
        }
        StockProduct stockProduct = StockMapper.requestToStockProduct(stockProductRequest);
        stockProduct.setStock(stock);
        stockProduct.setProduct(product);

        stock.getStockProducts().add(stockProduct);
        stockProductRepository.save(stockProduct);
        return StockMapper.stockToResponse(stock);
    }

    public void removeProduct(Long stockId, Long productId) {
        stockRepository.findById(stockId)
                .orElseThrow(() -> new EntityNotFoundException(STOCK_ERROR_MESSAGE));

        StockProductKey id = new StockProductKey(stockId, productId);
        stockProductRepository.deleteById(id);
    }

    public boolean isInStock(Long stockId, Long productId) {
        StockProductKey id = new StockProductKey(stockId, productId);
        return stockProductRepository.existsById(id);
    }

}

package org.trainee.stockservice.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.trainee.productservice.exception.EntityNotFoundException;
import org.trainee.productservice.model.Product;
import org.trainee.stockservice.model.Stock;
import org.trainee.stockservice.repository.StockRepository;

import java.util.List;
import java.util.Optional;


@Service
public class StockService {
    private final StockRepository stockRepository;
    private RestTemplate restTemplate;
    String productServiceUrl = "http://localhost:8080/api/v1/product";
    final String STOCK_ERROR_MESSAGE = "Stock not found!";
    final String PRODUCT_ERROR_MESSAGE = "Product not found!";

    @Autowired
    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
        this.restTemplate = new RestTemplate();
    }

    public List<Stock> getAllStocks() {
        return stockRepository.findAll();
    }

    public Optional<Stock> getStockById(Long id) {
        return stockRepository.findById(id);
    }

    @Transactional
    public Stock addProduct(Long stockId, Long productId) {
        Stock stock = stockRepository.findById(stockId).orElseThrow(() -> new EntityNotFoundException(STOCK_ERROR_MESSAGE));
        Product product = restTemplate.getForObject(productServiceUrl + "/" + productId, Product.class);
        if (product == null) {
            throw new EntityNotFoundException(PRODUCT_ERROR_MESSAGE);
        }
        stock.getProducts().add(product);
        stockRepository.save(stock);
        return stock;
    }

    public void removeProduct(Long stockId, Long productId) {
        Stock stock = stockRepository.findById(stockId).orElseThrow(()-> new EntityNotFoundException(STOCK_ERROR_MESSAGE));
        Product product = restTemplate.getForObject(productServiceUrl + "/" + productId, Product.class);
        if (product == null) {
            throw new EntityNotFoundException(PRODUCT_ERROR_MESSAGE);
        }
        stock.getProducts().remove(product);
        stockRepository.save(stock);
    }

    public boolean isInStock(Long stockId, Long productId) {
        Stock stock = stockRepository.findById(stockId).orElseThrow(() -> new EntityNotFoundException(STOCK_ERROR_MESSAGE));
        Product product = restTemplate.getForObject(productServiceUrl + "/" + productId, Product.class);
        if (product == null) {
            throw new EntityNotFoundException(PRODUCT_ERROR_MESSAGE);
        }
        return stock.getProducts().stream().anyMatch(p -> p.getId().equals(product.getId()));
    }

}

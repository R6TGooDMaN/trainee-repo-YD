package org.trainee.stockservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.trainee.stockservice.dto.StockProductRequest;
import org.trainee.stockservice.dto.StockProductResponse;
import org.trainee.stockservice.dto.StockRequest;
import org.trainee.stockservice.dto.StockResponse;
import org.trainee.stockservice.model.Stock;
import org.trainee.stockservice.service.StockService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/stock")
public class StockController {
    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @GetMapping
    public List<StockResponse> getStocks() {
        return stockService.getAllStocks();
    }

    @GetMapping("/{id}")
    public ResponseEntity<StockResponse> getStockById(@PathVariable Long id) {
        StockResponse stock = stockService.getStockById(id);
        return ResponseEntity.ok(stock);
    }

    @PostMapping("/create")
    public ResponseEntity<StockResponse> createStock(@RequestBody StockRequest stockRequest) {
        StockResponse stock = stockService.createStock(stockRequest);
        return ResponseEntity.ok(stock);
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<List<StockProductResponse>> getStockProductById(@PathVariable Long id) {
        List<StockProductResponse> stock = stockService.getProductsInStock(id);
        return ResponseEntity.ok(stock);
    }

    @PostMapping("/{stockId}/save/")
    public ResponseEntity<StockProductResponse> addToStock(@PathVariable Long stockId, @RequestBody StockProductRequest stockProductRequest) {
        StockProductResponse stock = stockService.addProduct(stockId, stockProductRequest);
        return ResponseEntity.ok(stock);
    }

    @DeleteMapping("/{stockId}/{productId}")
    public ResponseEntity<Stock> removeFromStock(@PathVariable Long stockId, @PathVariable Long productId) {
        stockService.removeProduct(stockId, productId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{stockId}/{productId}")
    public ResponseEntity<Boolean> isInStock(@PathVariable Long stockId, @PathVariable Long productId) {
        boolean isInStock = stockService.isInStock(stockId, productId);
        return ResponseEntity.ok(isInStock);
    }

    @PutMapping("/{productId}/{stockId}/{quantity}")
    public ResponseEntity<StockResponse> updateQuantity(@PathVariable Long productId, @PathVariable Long stockId, @PathVariable Integer quantity) {
        stockService.increaseProductQuantity(productId, stockId, quantity);
        return ResponseEntity.ok().build();
    }
}


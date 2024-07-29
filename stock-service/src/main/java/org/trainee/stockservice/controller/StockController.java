package org.trainee.stockservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.trainee.stockservice.dto.StockProductRequest;
import org.trainee.stockservice.dto.StockResponse;
import org.trainee.stockservice.model.Stock;
import org.trainee.stockservice.service.StockService;

import java.util.List;

@RestController
@RequestMapping("/api/stock")
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

    @PostMapping("/{stockId}/save/{productId}")
    public ResponseEntity<StockResponse> addToStock(@PathVariable Long stockId, @RequestBody StockProductRequest stockProductRequest) {
        StockResponse stock = stockService.addProduct(stockId, stockProductRequest);
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
}


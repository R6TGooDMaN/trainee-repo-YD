package org.trainee.stockservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.trainee.stockservice.model.Stock;
import org.trainee.stockservice.service.StockService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/stock")
public class StockController {
    private final StockService stockService;
    @Autowired
    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @GetMapping
    public List<Stock> getStocks() {
        return stockService.getAllStocks();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Stock> getStock(@PathVariable Long id) {
        Optional<Stock> stock = stockService.getStockById(id);
        return stock.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/{stockId}/save/{productId}")
    public ResponseEntity<Stock> addToStock(@PathVariable Long stockId, @PathVariable Long productId) {
        Stock stock = stockService.addProduct(stockId,productId);
        return ResponseEntity.ok(stock);
    }

    @DeleteMapping("/{stockId}/{productId}")
    public ResponseEntity<Stock> removeFromStock(@PathVariable Long stockId, @PathVariable Long productId) {
        stockService.removeProduct(stockId,productId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{stockId}/{productId}")
    public ResponseEntity<Boolean> isInStock(@PathVariable Long stockId, @PathVariable Long productId) {
        boolean isInStock = stockService.isInStock(stockId,productId);
        return ResponseEntity.ok(isInStock);
    }
}


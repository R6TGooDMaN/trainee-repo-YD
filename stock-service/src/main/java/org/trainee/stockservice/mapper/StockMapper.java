package org.trainee.stockservice.mapper;

import org.trainee.productservice.model.Product;
import org.trainee.stockservice.dto.StockProductRequest;
import org.trainee.stockservice.dto.StockProductResponse;
import org.trainee.stockservice.dto.StockRequest;
import org.trainee.stockservice.dto.StockResponse;
import org.trainee.stockservice.model.Stock;
import org.trainee.stockservice.model.StockProduct;

public interface StockMapper {

    static Stock requestToStock(StockRequest stockRequest) {
        Stock stock = new Stock();
        stock.setName(stockRequest.getName());
        stock.setStockProducts(stockRequest.getStockProductRequests()
                .stream()
                .map(StockMapper::requestToStockProduct)
                .toList());
        return stock;
    }

    static StockResponse stockToResponse(Stock stock) {
        StockResponse stockResponse = new StockResponse();
        stockResponse.setId(stock.getId());
        stockResponse.setName(stock.getName());
        stockResponse.setStockProductResponses(stock.getStockProducts().stream()
                .map(StockMapper::stockProductToResponse)
                .toList());
        return stockResponse;
    }

    static StockProductResponse stockProductToResponse(StockProduct stockProduct) {
        StockProductResponse stockProductResponse = new StockProductResponse();
        stockProductResponse.setProductId(stockProduct.getProduct().getId());
        stockProductResponse.setStockId(stockProduct.getStock().getId());
        stockProductResponse.setQuantity(stockProduct.getQuantity());
        return stockProductResponse;
    }

    static StockProduct requestToStockProduct(StockProductRequest stockProductRequest) {
        StockProduct stockProduct = new StockProduct();
        Product product = new Product();
        Stock stock = new Stock();
        product.setId(stockProductRequest.getProductId());
        stockProduct.setProduct(product);
        stockProduct.setStock(stock);
        stockProduct.setQuantity(stockProductRequest.getQuantity());
        return stockProduct;
    }
}

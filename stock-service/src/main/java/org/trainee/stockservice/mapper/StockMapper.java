package org.trainee.stockservice.mapper;

import lombok.experimental.UtilityClass;
import org.trainee.productservice.dto.ProductRequest;
import org.trainee.productservice.model.Product;
import org.trainee.stockservice.dto.StockProductRequest;
import org.trainee.stockservice.dto.StockProductResponse;
import org.trainee.stockservice.dto.StockRequest;
import org.trainee.stockservice.dto.StockResponse;
import org.trainee.stockservice.model.Stock;
import org.trainee.stockservice.model.StockProduct;

@UtilityClass
public class StockMapper {

    public static Stock requestToStock(StockRequest stockRequest) {
        return Stock.builder()
                .name(stockRequest.getName())
                .build();
    }

    public static StockResponse stockToResponse(Stock stock) {
        return StockResponse.builder()
                .id(stock.getId())
                .name(stock.getName())
                .build();
    }

    public static StockProductResponse stockProductToResponse(StockProduct stockProduct) {
        return StockProductResponse.builder()
                .productId(stockProduct.getProductId())
                .stockId(stockProduct.getStockId())
                .quantity(stockProduct.getQuantity())
                .build();
    }

    public static StockProduct requestToStockProduct(StockProductRequest stockProductRequest) {
        return StockProduct.builder()
                .stockId(stockProductRequest.getStockId())
                .productId(stockProductRequest.getProductId())
                .quantity(stockProductRequest.getQuantity())
                .build();
    }

    public StockProduct updateStockProductFromRequest(StockProduct stockProduct, StockProductRequest productStockRequest) {
        stockProduct.setStockId(productStockRequest.getStockId());
        stockProduct.setProductId(productStockRequest.getProductId());
        stockProduct.setQuantity(productStockRequest.getQuantity());
        return stockProduct;
    }

}

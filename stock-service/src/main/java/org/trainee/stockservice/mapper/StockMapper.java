package org.trainee.stockservice.mapper;

import lombok.experimental.UtilityClass;
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
                .stockProducts(stockRequest.getStockProductRequests()
                        .stream()
                        .map(StockMapper::requestToStockProduct)
                        .toList())
                .build();
    }

    public static StockResponse stockToResponse(Stock stock) {
        return StockResponse.builder()
                .id(stock.getId())
                .name(stock.getName())
                .stockProductResponses(stock.getStockProducts().stream()
                        .map(StockMapper::stockProductToResponse)
                        .toList())
                .build();
    }

    public static StockProductResponse stockProductToResponse(StockProduct stockProduct) {
        return StockProductResponse.builder()
                .productId(stockProduct.getProduct().getId())
                .stockId(stockProduct.getStock().getId())
                .quantity(stockProduct.getQuantity())
                .build();
    }

    public static StockProduct requestToStockProduct(StockProductRequest stockProductRequest) {
        Product product = Product.builder()
                .id(stockProductRequest.getProductId())
                .build();

        return StockProduct.builder()
                .product(product)
                .quantity(stockProductRequest.getQuantity())
                .build();
    }
}

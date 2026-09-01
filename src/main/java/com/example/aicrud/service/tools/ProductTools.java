package com.example.aicrud.service.tools;

import com.example.aicrud.dto.ProductRequest;
import com.example.aicrud.dto.ProductResponse;
import com.example.aicrud.entity.Product;
import com.example.aicrud.repository.ProductRepository;
import com.example.aicrud.service.ProductService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Configuration
public class ProductTools {

    private final ProductRepository productRepository;
    private final ProductService productService;

    public ProductTools(ProductRepository productRepository, ProductService productService) {
        this.productRepository = productRepository;
        this.productService = productService;
    }

    public record SearchRequest(String keyword) {}
    public record IdRequest(Long id) {}
    public record CategoryRequest(String category) {}
    public record LowStockRequest(Integer threshold) {}
    public record CreateItemRequest(String name, String description, String category, Double price, Integer stockQuantity) {}
    public record EmptyRequest() {}

    @Bean
    @Description("Search for products in the catalog by keyword matching name, description, category, or AI tags.")
    public Function<SearchRequest, List<ProductResponse>> searchProductsFunction() {
        return request -> {
            String kw = (request != null && request.keyword() != null) ? request.keyword() : "";
            return productRepository.searchProducts(kw).stream()
                    .map(this::mapToResponse)
                    .toList();
        };
    }

    @Bean
    @Description("Get detailed information about a specific product by its ID.")
    public Function<IdRequest, ProductResponse> getProductByIdFunction() {
        return request -> productService.getProductById(request.id());
    }

    @Bean
    @Description("Find all products in a given category.")
    public Function<CategoryRequest, List<ProductResponse>> getProductsByCategoryFunction() {
        return request -> productRepository.findByCategoryIgnoreCase(request.category()).stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Bean
    @Description("Find products where inventory stock is below a certain threshold.")
    public Function<LowStockRequest, List<ProductResponse>> getLowStockProductsFunction() {
        return request -> {
            int th = (request != null && request.threshold() != null && request.threshold() > 0) ? request.threshold() : 10;
            return productService.getLowStockProducts(th);
        };
    }

    @Bean
    @Description("Create and save a new product into the database catalog.")
    public Function<CreateItemRequest, ProductResponse> createProductFunction() {
        return request -> {
            ProductRequest pReq = ProductRequest.builder()
                    .name(request.name())
                    .description(request.description())
                    .category(request.category())
                    .price(BigDecimal.valueOf(request.price() != null ? request.price() : 0.0))
                    .stockQuantity(request.stockQuantity() != null ? request.stockQuantity() : 0)
                    .autoGenerateAiContent(false)
                    .build();
            return productService.createProduct(pReq);
        };
    }

    @Bean
    @Description("Get catalog statistics including total count of products, distinct categories, and low stock count.")
    public Function<EmptyRequest, Map<String, Object>> getCatalogSummaryFunction() {
        return request -> {
            Map<String, Object> summary = new HashMap<>();
            long totalProducts = productRepository.count();
            List<String> categories = productRepository.findAllCategories();
            List<Product> lowStock = productRepository.findByStockQuantityLessThan(10);

            summary.put("totalProducts", totalProducts);
            summary.put("categories", categories);
            summary.put("lowStockCount", lowStock.size());
            return summary;
        };
    }

    private ProductResponse mapToResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .category(product.getCategory())
                .price(product.getPrice())
                .stockQuantity(product.getStockQuantity())
                .aiSummary(product.getAiSummary())
                .aiTags(product.getAiTags())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }
}

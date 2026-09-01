package com.example.aicrud.service;

import com.example.aicrud.dto.ProductRequest;
import com.example.aicrud.dto.ProductResponse;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface ProductService {

    ProductResponse createProduct(ProductRequest request);

    ProductResponse getProductById(Long id);

    Page<ProductResponse> getAllProducts(String search, String category, int page, int size, String sortBy, String sortDir);

    List<ProductResponse> getAllProductsList();

    ProductResponse updateProduct(Long id, ProductRequest request);

    ProductResponse patchProduct(Long id, Map<String, Object> updates);

    void deleteProduct(Long id);

    List<String> getAllCategories();

    List<ProductResponse> getLowStockProducts(int threshold);
}

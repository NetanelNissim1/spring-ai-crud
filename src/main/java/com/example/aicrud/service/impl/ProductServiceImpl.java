package com.example.aicrud.service.impl;

import com.example.aicrud.dto.ProductRequest;
import com.example.aicrud.dto.ProductResponse;
import com.example.aicrud.entity.Product;
import com.example.aicrud.exception.ResourceNotFoundException;
import com.example.aicrud.repository.ProductRepository;
import com.example.aicrud.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public ProductResponse createProduct(ProductRequest request) {
        log.info("Creating new product: {}", request.getName());

        Product product = Product.builder()
                .name(request.getName().trim())
                .description(request.getDescription())
                .category(request.getCategory().trim())
                .price(request.getPrice())
                .stockQuantity(request.getStockQuantity())
                .aiSummary(request.getAiSummary())
                .aiTags(request.getAiTags())
                .build();

        Product savedProduct = productRepository.save(product);
        log.info("Product created with ID: {}", savedProduct.getId());
        return mapToResponse(savedProduct);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
        return mapToResponse(product);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponse> getAllProducts(
            String search, String category, int page, int size, String sortBy, String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Product> productPage;
        if (StringUtils.hasText(search)) {
            productPage = productRepository.searchProducts(search.trim(), pageable);
        } else if (StringUtils.hasText(category)) {
            productPage = productRepository.findByCategoryIgnoreCase(category.trim(), pageable);
        } else {
            productPage = productRepository.findAll(pageable);
        }

        return productPage.map(this::mapToResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getAllProductsList() {
        return productRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public ProductResponse updateProduct(Long id, ProductRequest request) {
        log.info("Updating product with ID: {}", id);

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));

        product.setName(request.getName().trim());
        product.setDescription(request.getDescription());
        product.setCategory(request.getCategory().trim());
        product.setPrice(request.getPrice());
        product.setStockQuantity(request.getStockQuantity());
        if (request.getAiSummary() != null) {
            product.setAiSummary(request.getAiSummary());
        }
        if (request.getAiTags() != null) {
            product.setAiTags(request.getAiTags());
        }

        Product updatedProduct = productRepository.save(product);
        log.info("Product updated successfully with ID: {}", updatedProduct.getId());
        return mapToResponse(updatedProduct);
    }

    @Override
    public ProductResponse patchProduct(Long id, Map<String, Object> updates) {
        log.info("Patching product with ID: {}", id);

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));

        if (updates.containsKey("name") && updates.get("name") != null) {
            product.setName(updates.get("name").toString().trim());
        }
        if (updates.containsKey("description")) {
            product.setDescription((String) updates.get("description"));
        }
        if (updates.containsKey("category") && updates.get("category") != null) {
            product.setCategory(updates.get("category").toString().trim());
        }
        if (updates.containsKey("price") && updates.get("price") != null) {
            product.setPrice(new BigDecimal(updates.get("price").toString()));
        }
        if (updates.containsKey("stockQuantity") && updates.get("stockQuantity") != null) {
            product.setStockQuantity(Integer.parseInt(updates.get("stockQuantity").toString()));
        }
        if (updates.containsKey("aiSummary")) {
            product.setAiSummary((String) updates.get("aiSummary"));
        }
        if (updates.containsKey("aiTags")) {
            product.setAiTags((String) updates.get("aiTags"));
        }

        Product patchedProduct = productRepository.save(product);
        return mapToResponse(patchedProduct);
    }

    @Override
    public void deleteProduct(Long id) {
        log.info("Deleting product with ID: {}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
        productRepository.delete(product);
        log.info("Product deleted with ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getAllCategories() {
        return productRepository.findAllCategories();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getLowStockProducts(int threshold) {
        return productRepository.findByStockQuantityLessThan(threshold).stream()
                .map(this::mapToResponse)
                .toList();
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

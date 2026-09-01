package com.example.aicrud.controller;

import com.example.aicrud.dto.ProductRequest;
import com.example.aicrud.dto.ProductResponse;
import com.example.aicrud.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
@Tag(name = "Product CRUD Management", description = "Endpoints for standard CRUD operations on the product catalog")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    @Operation(summary = "Create a new product", description = "Creates and stores a new product in the database.")
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest request) {
        ProductResponse created = productService.createProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get product by ID", description = "Fetches complete details of a single product.")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @GetMapping
    @Operation(summary = "List and search products", description = "Retrieve products with support for keyword search, category filter, pagination, and sorting.")
    public ResponseEntity<Page<ProductResponse>> getAllProducts(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        return ResponseEntity.ok(productService.getAllProducts(search, category, page, size, sortBy, sortDir));
    }

    @GetMapping("/all")
    @Operation(summary = "Get all products as unpaginated list", description = "Returns a flat list of all products in the catalog.")
    public ResponseEntity<List<ProductResponse>> getAllProductsList() {
        return ResponseEntity.ok(productService.getAllProductsList());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update product", description = "Replaces existing product fields with new values.")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequest request) {
        return ResponseEntity.ok(productService.updateProduct(id, request));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Partial update product", description = "Updates specific fields of an existing product.")
    public ResponseEntity<ProductResponse> patchProduct(
            @PathVariable Long id,
            @RequestBody Map<String, Object> updates) {
        return ResponseEntity.ok(productService.patchProduct(id, updates));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete product", description = "Deletes a product permanently by its ID.")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/categories")
    @Operation(summary = "List all product categories", description = "Returns a list of distinct product categories.")
    public ResponseEntity<List<String>> getAllCategories() {
        return ResponseEntity.ok(productService.getAllCategories());
    }

    @GetMapping("/low-stock")
    @Operation(summary = "List low-stock products", description = "Returns products whose inventory stock is below a specified threshold.")
    public ResponseEntity<List<ProductResponse>> getLowStockProducts(
            @RequestParam(defaultValue = "10") int threshold) {
        return ResponseEntity.ok(productService.getLowStockProducts(threshold));
    }
}

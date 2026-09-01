package com.example.aicrud;

import com.example.aicrud.controller.ProductController;
import com.example.aicrud.dto.ProductRequest;
import com.example.aicrud.dto.ProductResponse;
import com.example.aicrud.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductControllerPureUnitTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    private ProductRequest sampleRequest;
    private ProductResponse sampleResponse;

    @BeforeEach
    void setUp() {
        sampleRequest = ProductRequest.builder()
                .name("Noise-Canceling Headphones")
                .category("Electronics")
                .price(new BigDecimal("199.99"))
                .stockQuantity(45)
                .build();

        sampleResponse = ProductResponse.builder()
                .id(1L)
                .name("Noise-Canceling Headphones")
                .category("Electronics")
                .price(new BigDecimal("199.99"))
                .stockQuantity(45)
                .build();
    }

    @Test
    @DisplayName("Unit test createProduct: returns 201 Created and response body")
    void testCreateProduct() {
        when(productService.createProduct(sampleRequest)).thenReturn(sampleResponse);

        ResponseEntity<ProductResponse> response = productController.createProduct(sampleRequest);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(1L);
        assertThat(response.getBody().getName()).isEqualTo("Noise-Canceling Headphones");
        verify(productService, times(1)).createProduct(sampleRequest);
    }

    @Test
    @DisplayName("Unit test getProductById: returns 200 OK and product details")
    void testGetProductById() {
        when(productService.getProductById(1L)).thenReturn(sampleResponse);

        ResponseEntity<ProductResponse> response = productController.getProductById(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(sampleResponse);
        verify(productService, times(1)).getProductById(1L);
    }

    @Test
    @DisplayName("Unit test getAllProducts: returns 200 OK and paginated page")
    void testGetAllProducts() {
        Page<ProductResponse> page = new PageImpl<>(List.of(sampleResponse));
        when(productService.getAllProducts("headphone", "Electronics", 0, 10, "id", "desc"))
                .thenReturn(page);

        ResponseEntity<Page<ProductResponse>> response = productController.getAllProducts(
                "headphone", "Electronics", 0, 10, "id", "desc");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getContent()).hasSize(1);
    }

    @Test
    @DisplayName("Unit test getAllProductsList: returns 200 OK and list of all products")
    void testGetAllProductsList() {
        when(productService.getAllProductsList()).thenReturn(List.of(sampleResponse));

        ResponseEntity<List<ProductResponse>> response = productController.getAllProductsList();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
    }

    @Test
    @DisplayName("Unit test updateProduct: returns 200 OK and updated product")
    void testUpdateProduct() {
        when(productService.updateProduct(1L, sampleRequest)).thenReturn(sampleResponse);

        ResponseEntity<ProductResponse> response = productController.updateProduct(1L, sampleRequest);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(sampleResponse);
        verify(productService, times(1)).updateProduct(1L, sampleRequest);
    }

    @Test
    @DisplayName("Unit test patchProduct: returns 200 OK and patched product")
    void testPatchProduct() {
        Map<String, Object> updates = Map.of("price", 179.99);
        when(productService.patchProduct(1L, updates)).thenReturn(sampleResponse);

        ResponseEntity<ProductResponse> response = productController.patchProduct(1L, updates);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(sampleResponse);
        verify(productService, times(1)).patchProduct(1L, updates);
    }

    @Test
    @DisplayName("Unit test deleteProduct: returns 204 No Content")
    void testDeleteProduct() {
        doNothing().when(productService).deleteProduct(1L);

        ResponseEntity<Void> response = productController.deleteProduct(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(productService, times(1)).deleteProduct(1L);
    }

    @Test
    @DisplayName("Unit test getAllCategories: returns 200 OK and category list")
    void testGetAllCategories() {
        when(productService.getAllCategories()).thenReturn(List.of("Electronics", "Furniture"));

        ResponseEntity<List<String>> response = productController.getAllCategories();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).containsExactly("Electronics", "Furniture");
    }

    @Test
    @DisplayName("Unit test getLowStockProducts: returns 200 OK and low stock list")
    void testGetLowStockProducts() {
        when(productService.getLowStockProducts(10)).thenReturn(List.of(sampleResponse));

        ResponseEntity<List<ProductResponse>> response = productController.getLowStockProducts(10);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
    }
}

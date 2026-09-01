package com.example.aicrud;

import com.example.aicrud.dto.ProductRequest;
import com.example.aicrud.dto.ProductResponse;
import com.example.aicrud.entity.Product;
import com.example.aicrud.exception.ResourceNotFoundException;
import com.example.aicrud.repository.ProductRepository;
import com.example.aicrud.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product sampleProduct;
    private ProductRequest sampleRequest;

    @BeforeEach
    void setUp() {
        sampleProduct = Product.builder()
                .id(1L)
                .name("Mechanical Keyboard")
                .description("RGB backlit mechanical keyboard")
                .category("Electronics")
                .price(new BigDecimal("99.99"))
                .stockQuantity(25)
                .aiSummary("Top tier gaming keyboard")
                .aiTags("gaming, rgb, keyboard")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        sampleRequest = ProductRequest.builder()
                .name("Mechanical Keyboard")
                .description("RGB backlit mechanical keyboard")
                .category("Electronics")
                .price(new BigDecimal("99.99"))
                .stockQuantity(25)
                .aiSummary("Top tier gaming keyboard")
                .aiTags("gaming, rgb, keyboard")
                .build();
    }

    @Test
    @DisplayName("Should create product successfully")
    void testCreateProduct() {
        when(productRepository.save(any(Product.class))).thenReturn(sampleProduct);

        ProductResponse response = productService.createProduct(sampleRequest);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getName()).isEqualTo("Mechanical Keyboard");
        assertThat(response.getPrice()).isEqualTo(new BigDecimal("99.99"));
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    @DisplayName("Should retrieve product by ID successfully")
    void testGetProductById() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(sampleProduct));

        ProductResponse response = productService.getProductById(1L);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getCategory()).isEqualTo("Electronics");
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when product not found")
    void testGetProductByIdNotFound() {
        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> productService.getProductById(999L));
    }

    @Test
    @DisplayName("Should update product successfully")
    void testUpdateProduct() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(sampleProduct));
        when(productRepository.save(any(Product.class))).thenReturn(sampleProduct);

        ProductRequest updateRequest = ProductRequest.builder()
                .name("Mechanical Keyboard Pro")
                .category("Electronics")
                .price(new BigDecimal("129.99"))
                .stockQuantity(15)
                .build();

        ProductResponse response = productService.updateProduct(1L, updateRequest);

        assertThat(response).isNotNull();
        verify(productRepository).save(any(Product.class));
    }

    @Test
    @DisplayName("Should delete product successfully")
    void testDeleteProduct() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(sampleProduct));
        doNothing().when(productRepository).delete(sampleProduct);

        productService.deleteProduct(1L);

        verify(productRepository, times(1)).delete(sampleProduct);
    }

    @Test
    @DisplayName("Should list low stock products")
    void testGetLowStockProducts() {
        when(productRepository.findByStockQuantityLessThan(10)).thenReturn(List.of(sampleProduct));

        List<ProductResponse> lowStock = productService.getLowStockProducts(10);

        assertThat(lowStock).hasSize(1);
        assertThat(lowStock.get(0).getName()).isEqualTo("Mechanical Keyboard");
    }
}

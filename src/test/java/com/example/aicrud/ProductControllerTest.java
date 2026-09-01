package com.example.aicrud;

import com.example.aicrud.controller.ProductController;
import com.example.aicrud.dto.ProductRequest;
import com.example.aicrud.dto.ProductResponse;
import com.example.aicrud.exception.ResourceNotFoundException;
import com.example.aicrud.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    private ProductRequest validRequest;
    private ProductResponse sampleResponse;

    @BeforeEach
    void setUp() {
        validRequest = ProductRequest.builder()
                .name("Ergonomic Keyboard")
                .description("Split mechanical ergonomic keyboard")
                .category("Electronics")
                .price(new BigDecimal("129.99"))
                .stockQuantity(25)
                .aiSummary("Ergonomically designed mechanical keyboard")
                .aiTags("ergonomic, keyboard, hardware")
                .build();

        sampleResponse = ProductResponse.builder()
                .id(1L)
                .name("Ergonomic Keyboard")
                .description("Split mechanical ergonomic keyboard")
                .category("Electronics")
                .price(new BigDecimal("129.99"))
                .stockQuantity(25)
                .aiSummary("Ergonomically designed mechanical keyboard")
                .aiTags("ergonomic, keyboard, hardware")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    // =========================================================================
    // 1. CREATE PRODUCT (POST /api/products)
    // =========================================================================
    @Nested
    @DisplayName("POST /api/products (Create)")
    class CreateProductTests {

        @Test
        @DisplayName("Should create product and return 201 Created")
        void testCreateProductSuccess() throws Exception {
            when(productService.createProduct(any(ProductRequest.class))).thenReturn(sampleResponse);

            mockMvc.perform(post("/api/products")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validRequest)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value(1L))
                    .andExpect(jsonPath("$.name").value("Ergonomic Keyboard"))
                    .andExpect(jsonPath("$.category").value("Electronics"))
                    .andExpect(jsonPath("$.price").value(129.99))
                    .andExpect(jsonPath("$.stockQuantity").value(25))
                    .andExpect(jsonPath("$.aiSummary").value("Ergonomically designed mechanical keyboard"));

            verify(productService, times(1)).createProduct(any(ProductRequest.class));
        }

        @Test
        @DisplayName("Should return 400 Bad Request when required fields are missing/invalid")
        void testCreateProductValidationErrors() throws Exception {
            ProductRequest invalidRequest = ProductRequest.builder()
                    .name("")              // Blank name
                    .category("")          // Blank category
                    .price(new BigDecimal("-10.00")) // Negative price
                    .stockQuantity(-5)     // Negative stock
                    .build();

            mockMvc.perform(post("/api/products")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalidRequest)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value(400))
                    .andExpect(jsonPath("$.validationErrors.name").exists())
                    .andExpect(jsonPath("$.validationErrors.category").exists())
                    .andExpect(jsonPath("$.validationErrors.price").exists())
                    .andExpect(jsonPath("$.validationErrors.stockQuantity").exists());

            verify(productService, never()).createProduct(any(ProductRequest.class));
        }
    }

    // =========================================================================
    // 2. READ PRODUCT BY ID (GET /api/products/{id})
    // =========================================================================
    @Nested
    @DisplayName("GET /api/products/{id} (Find by ID)")
    class GetProductByIdTests {

        @Test
        @DisplayName("Should return 200 OK with product details when found")
        void testGetProductByIdSuccess() throws Exception {
            when(productService.getProductById(1L)).thenReturn(sampleResponse);

            mockMvc.perform(get("/api/products/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1L))
                    .andExpect(jsonPath("$.name").value("Ergonomic Keyboard"))
                    .andExpect(jsonPath("$.price").value(129.99));

            verify(productService, times(1)).getProductById(1L);
        }

        @Test
        @DisplayName("Should return 404 Not Found when product ID does not exist")
        void testGetProductByIdNotFound() throws Exception {
            when(productService.getProductById(999L))
                    .thenThrow(new ResourceNotFoundException("Product", "id", 999L));

            mockMvc.perform(get("/api/products/999"))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status").value(404))
                    .andExpect(jsonPath("$.message").value("Product not found with id: '999'"));

            verify(productService, times(1)).getProductById(999L);
        }
    }

    // =========================================================================
    // 3. READ ALL PRODUCTS / SEARCH / FILTER (GET /api/products)
    // =========================================================================
    @Nested
    @DisplayName("GET /api/products (List, Filter, Search, Pagination)")
    class GetAllProductsTests {

        @Test
        @DisplayName("Should return paginated list of products with 200 OK")
        void testGetAllProductsDefaultPagination() throws Exception {
            Page<ProductResponse> page = new PageImpl<>(List.of(sampleResponse));
            when(productService.getAllProducts(isNull(), isNull(), eq(0), eq(10), eq("id"), eq("desc")))
                    .thenReturn(page);

            mockMvc.perform(get("/api/products"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content").isArray())
                    .andExpect(jsonPath("$.content[0].id").value(1L))
                    .andExpect(jsonPath("$.totalElements").value(1));

            verify(productService, times(1)).getAllProducts(isNull(), isNull(), eq(0), eq(10), eq("id"), eq("desc"));
        }

        @Test
        @DisplayName("Should filter products by search keyword and category")
        void testGetProductsWithSearchAndCategory() throws Exception {
            Page<ProductResponse> page = new PageImpl<>(List.of(sampleResponse));
            when(productService.getAllProducts(eq("keyboard"), eq("Electronics"), eq(1), eq(5), eq("price"), eq("asc")))
                    .thenReturn(page);

            mockMvc.perform(get("/api/products")
                            .param("search", "keyboard")
                            .param("category", "Electronics")
                            .param("page", "1")
                            .param("size", "5")
                            .param("sortBy", "price")
                            .param("sortDir", "asc"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content[0].name").value("Ergonomic Keyboard"));

            verify(productService, times(1))
                    .getAllProducts(eq("keyboard"), eq("Electronics"), eq(1), eq(5), eq("price"), eq("asc"));
        }

        @Test
        @DisplayName("GET /api/products/all should return unpaginated list of all products")
        void testGetAllProductsUnpaginated() throws Exception {
            when(productService.getAllProductsList()).thenReturn(List.of(sampleResponse));

            mockMvc.perform(get("/api/products/all"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$[0].name").value("Ergonomic Keyboard"));

            verify(productService, times(1)).getAllProductsList();
        }
    }

    // =========================================================================
    // 4. UPDATE PRODUCT (PUT /api/products/{id})
    // =========================================================================
    @Nested
    @DisplayName("PUT /api/products/{id} (Full Update)")
    class UpdateProductTests {

        @Test
        @DisplayName("Should update product and return 200 OK")
        void testUpdateProductSuccess() throws Exception {
            ProductResponse updatedResponse = ProductResponse.builder()
                    .id(1L)
                    .name("Ergonomic Keyboard V2")
                    .category("Electronics")
                    .price(new BigDecimal("149.99"))
                    .stockQuantity(30)
                    .build();

            when(productService.updateProduct(eq(1L), any(ProductRequest.class))).thenReturn(updatedResponse);

            mockMvc.perform(put("/api/products/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1L))
                    .andExpect(jsonPath("$.name").value("Ergonomic Keyboard V2"))
                    .andExpect(jsonPath("$.price").value(149.99));

            verify(productService, times(1)).updateProduct(eq(1L), any(ProductRequest.class));
        }

        @Test
        @DisplayName("Should return 404 Not Found when updating non-existent product")
        void testUpdateProductNotFound() throws Exception {
            when(productService.updateProduct(eq(999L), any(ProductRequest.class)))
                    .thenThrow(new ResourceNotFoundException("Product", "id", 999L));

            mockMvc.perform(put("/api/products/999")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validRequest)))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status").value(404));

            verify(productService, times(1)).updateProduct(eq(999L), any(ProductRequest.class));
        }

        @Test
        @DisplayName("Should return 400 Bad Request when update payload is invalid")
        void testUpdateProductValidationError() throws Exception {
            ProductRequest invalid = ProductRequest.builder()
                    .name("")
                    .category("")
                    .price(new BigDecimal("-1.0"))
                    .stockQuantity(-1)
                    .build();

            mockMvc.perform(put("/api/products/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalid)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value(400));

            verify(productService, never()).updateProduct(anyLong(), any(ProductRequest.class));
        }
    }

    // =========================================================================
    // 5. PATCH PRODUCT (PATCH /api/products/{id})
    // =========================================================================
    @Nested
    @DisplayName("PATCH /api/products/{id} (Partial Update)")
    class PatchProductTests {

        @Test
        @DisplayName("Should partially update fields and return 200 OK")
        void testPatchProductSuccess() throws Exception {
            Map<String, Object> updates = new HashMap<>();
            updates.put("price", 109.99);
            updates.put("stockQuantity", 40);

            ProductResponse patchedResponse = ProductResponse.builder()
                    .id(1L)
                    .name("Ergonomic Keyboard")
                    .category("Electronics")
                    .price(new BigDecimal("109.99"))
                    .stockQuantity(40)
                    .build();

            when(productService.patchProduct(eq(1L), anyMap())).thenReturn(patchedResponse);

            mockMvc.perform(patch("/api/products/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updates)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.price").value(109.99))
                    .andExpect(jsonPath("$.stockQuantity").value(40));

            verify(productService, times(1)).patchProduct(eq(1L), anyMap());
        }

        @Test
        @DisplayName("Should return 404 Not Found when patching non-existent product")
        void testPatchProductNotFound() throws Exception {
            Map<String, Object> updates = Map.of("price", 99.0);
            when(productService.patchProduct(eq(999L), anyMap()))
                    .thenThrow(new ResourceNotFoundException("Product", "id", 999L));

            mockMvc.perform(patch("/api/products/999")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updates)))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status").value(404));
        }
    }

    // =========================================================================
    // 6. DELETE PRODUCT (DELETE /api/products/{id})
    // =========================================================================
    @Nested
    @DisplayName("DELETE /api/products/{id} (Delete)")
    class DeleteProductTests {

        @Test
        @DisplayName("Should delete product and return 204 No Content")
        void testDeleteProductSuccess() throws Exception {
            doNothing().when(productService).deleteProduct(1L);

            mockMvc.perform(delete("/api/products/1"))
                    .andExpect(status().isNoContent());

            verify(productService, times(1)).deleteProduct(1L);
        }

        @Test
        @DisplayName("Should return 404 Not Found when deleting non-existent product")
        void testDeleteProductNotFound() throws Exception {
            doThrow(new ResourceNotFoundException("Product", "id", 999L))
                    .when(productService).deleteProduct(999L);

            mockMvc.perform(delete("/api/products/999"))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.status").value(404));

            verify(productService, times(1)).deleteProduct(999L);
        }
    }

    // =========================================================================
    // 7. CATEGORIES & LOW-STOCK ENDPOINTS
    // =========================================================================
    @Nested
    @DisplayName("Auxiliary Queries (Categories & Low Stock)")
    class AuxiliaryQueryTests {

        @Test
        @DisplayName("GET /api/products/categories should return distinct category list")
        void testGetCategories() throws Exception {
            List<String> categories = List.of("Books", "Electronics", "Furniture", "Kitchen");
            when(productService.getAllCategories()).thenReturn(categories);

            mockMvc.perform(get("/api/products/categories"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$.length()").value(4))
                    .andExpect(jsonPath("$[1]").value("Electronics"));

            verify(productService, times(1)).getAllCategories();
        }

        @Test
        @DisplayName("GET /api/products/low-stock should return products below threshold")
        void testGetLowStockProducts() throws Exception {
            when(productService.getLowStockProducts(15)).thenReturn(List.of(sampleResponse));

            mockMvc.perform(get("/api/products/low-stock").param("threshold", "15"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$[0].name").value("Ergonomic Keyboard"));

            verify(productService, times(1)).getLowStockProducts(15);
        }
    }
}

package com.example.aicrud;

import com.example.aicrud.controller.AiEnrichmentController;
import com.example.aicrud.dto.AiEnrichmentRequest;
import com.example.aicrud.dto.AiEnrichmentResponse;
import com.example.aicrud.dto.ReviewAnalysisResponse;
import com.example.aicrud.service.AiProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AiEnrichmentController.class)
class AiEnrichmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AiProductService aiProductService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("POST /api/ai/enrich should return AI generated metadata")
    void testEnrichProduct() throws Exception {
        AiEnrichmentRequest request = AiEnrichmentRequest.builder()
                .productName("Smart Water Bottle")
                .targetAudience("Athletes")
                .build();

        AiEnrichmentResponse response = AiEnrichmentResponse.builder()
                .suggestedCategory("Fitness")
                .enhancedDescription("Next-gen hydration tracker")
                .aiSummary("Top smart bottle for fitness enthusiasts")
                .tags(List.of("fitness", "hydration", "smart-bottle"))
                .sellingPoints(List.of("Tracks water intake", "24h temperature retention"))
                .build();

        when(aiProductService.enrichProduct(any(AiEnrichmentRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/ai/enrich")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.suggestedCategory").value("Fitness"))
                .andExpect(jsonPath("$.enhancedDescription").value("Next-gen hydration tracker"))
                .andExpect(jsonPath("$.tags[0]").value("fitness"));

        verify(aiProductService, times(1)).enrichProduct(any(AiEnrichmentRequest.class));
    }

    @Test
    @DisplayName("POST /api/ai/enrich/{id} should enrich existing product")
    void testEnrichExistingProduct() throws Exception {
        AiEnrichmentResponse response = AiEnrichmentResponse.builder()
                .suggestedCategory("Electronics")
                .enhancedDescription("Updated description")
                .aiSummary("Enhanced AI summary")
                .tags(List.of("audio", "wireless"))
                .build();

        when(aiProductService.enrichExistingProduct(1L)).thenReturn(response);

        mockMvc.perform(post("/api/ai/enrich/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.suggestedCategory").value("Electronics"))
                .andExpect(jsonPath("$.aiSummary").value("Enhanced AI summary"));

        verify(aiProductService, times(1)).enrichExistingProduct(1L);
    }

    @Test
    @DisplayName("POST /api/ai/analyze-reviews/{id} should return sentiment and review breakdown")
    void testAnalyzeReviews() throws Exception {
        ReviewAnalysisResponse response = ReviewAnalysisResponse.builder()
                .sentiment("POSITIVE")
                .sentimentScore(9.2)
                .executiveSummary("Loved the build quality and ergonomics")
                .positiveHighlights(List.of("Comfortable", "Long battery life"))
                .areasForImprovement(List.of("Slightly heavy"))
                .recommendationForBuyer("Highly recommended for daily use")
                .build();

        when(aiProductService.analyzeProductReviews(eq(1L), anyString())).thenReturn(response);

        mockMvc.perform(post("/api/ai/analyze-reviews/1")
                        .contentType(MediaType.TEXT_PLAIN)
                        .content("Great headphones, battery lasts forever!"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sentiment").value("POSITIVE"))
                .andExpect(jsonPath("$.sentimentScore").value(9.2))
                .andExpect(jsonPath("$.executiveSummary").value("Loved the build quality and ergonomics"));

        verify(aiProductService, times(1)).analyzeProductReviews(eq(1L), anyString());
    }
}

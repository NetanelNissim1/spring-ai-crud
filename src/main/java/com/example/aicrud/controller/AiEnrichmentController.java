package com.example.aicrud.controller;

import com.example.aicrud.dto.AiEnrichmentRequest;
import com.example.aicrud.dto.AiEnrichmentResponse;
import com.example.aicrud.dto.ReviewAnalysisResponse;
import com.example.aicrud.service.AiProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai")
@CrossOrigin(origins = "*")
@Tag(name = "AI Product Enrichment & Insights", description = "AI-assisted generation of product descriptions, SEO tags, categories, and review sentiment")
public class AiEnrichmentController {

    private final AiProductService aiProductService;

    public AiEnrichmentController(AiProductService aiProductService) {
        this.aiProductService = aiProductService;
    }

    @PostMapping("/enrich")
    @Operation(summary = "Generate AI marketing metadata", description = "Uses Spring AI to generate description, tags, category, and selling points from a product name.")
    public ResponseEntity<AiEnrichmentResponse> enrichProduct(@Valid @RequestBody AiEnrichmentRequest request) {
        return ResponseEntity.ok(aiProductService.enrichProduct(request));
    }

    @PostMapping("/enrich/{productId}")
    @Operation(summary = "Enrich an existing product in the catalog", description = "Generates AI summary, tags, and enhancements, updating the database record directly.")
    public ResponseEntity<AiEnrichmentResponse> enrichExistingProduct(@PathVariable Long productId) {
        return ResponseEntity.ok(aiProductService.enrichExistingProduct(productId));
    }

    @PostMapping("/analyze-reviews/{productId}")
    @Operation(summary = "Analyze product reviews with AI", description = "Evaluates sentiment, generates summary, pros, cons, and buyer recommendation.")
    public ResponseEntity<ReviewAnalysisResponse> analyzeReviews(
            @PathVariable Long productId,
            @RequestBody String reviewsText) {
        return ResponseEntity.ok(aiProductService.analyzeProductReviews(productId, reviewsText));
    }
}

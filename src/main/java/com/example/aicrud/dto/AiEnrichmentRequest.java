package com.example.aicrud.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AiEnrichmentRequest {

    @NotBlank(message = "Product name is required for AI enrichment")
    private String productName;

    private String currentDescription;

    private String category;

    private String targetAudience;

    private String keyFeatures;
}

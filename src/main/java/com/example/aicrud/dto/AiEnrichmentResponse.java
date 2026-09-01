package com.example.aicrud.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AiEnrichmentResponse {

    private String suggestedCategory;
    private String enhancedDescription;
    private String aiSummary;
    private List<String> tags;
    private List<String> sellingPoints;
}

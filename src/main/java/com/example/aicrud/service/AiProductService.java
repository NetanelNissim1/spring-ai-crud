package com.example.aicrud.service;

import com.example.aicrud.dto.AiEnrichmentRequest;
import com.example.aicrud.dto.AiEnrichmentResponse;
import com.example.aicrud.dto.ChatMessageDto;
import com.example.aicrud.dto.ReviewAnalysisResponse;

public interface AiProductService {

    AiEnrichmentResponse enrichProduct(AiEnrichmentRequest request);

    AiEnrichmentResponse enrichExistingProduct(Long productId);

    ReviewAnalysisResponse analyzeProductReviews(Long productId, String reviewsText);

    ChatMessageDto.Response processChatMessage(ChatMessageDto.Request request);
}

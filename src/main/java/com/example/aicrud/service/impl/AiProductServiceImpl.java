package com.example.aicrud.service.impl;

import com.example.aicrud.dto.AiEnrichmentRequest;
import com.example.aicrud.dto.AiEnrichmentResponse;
import com.example.aicrud.dto.ChatMessageDto;
import com.example.aicrud.dto.ReviewAnalysisResponse;
import com.example.aicrud.entity.Product;
import com.example.aicrud.exception.ResourceNotFoundException;
import com.example.aicrud.repository.ProductRepository;
import com.example.aicrud.service.AiProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Slf4j
@Service
public class AiProductServiceImpl implements AiProductService {

    private final ProductRepository productRepository;
    private final ChatClient chatClient;

    @Value("${app.ai.mock-fallback-enabled:true}")
    private boolean mockFallbackEnabled;

    public AiProductServiceImpl(
            ProductRepository productRepository,
            ChatClient.Builder chatClientBuilder) {
        this.productRepository = productRepository;
        this.chatClient = chatClientBuilder
                .defaultSystem("You are an intelligent e-commerce catalog assistant. " +
                        "You help users manage products, inspect inventory, create new items, " +
                        "and summarize catalog data using your available functions. " +
                        "Always be helpful, precise, and concise in your answers.")
                .defaultFunctions(
                        "searchProductsFunction",
                        "getProductByIdFunction",
                        "getProductsByCategoryFunction",
                        "getLowStockProductsFunction",
                        "createProductFunction",
                        "getCatalogSummaryFunction"
                )
                .build();
    }

    @Override
    public AiEnrichmentResponse enrichProduct(AiEnrichmentRequest request) {
        log.info("Requesting AI enrichment for product: {}", request.getProductName());

        String prompt = String.format(
                "You are an expert e-commerce copywriter. Analyze this product and provide marketing metadata:\n" +
                "Product Name: %s\n" +
                "Current Description: %s\n" +
                "Category: %s\n" +
                "Target Audience: %s\n" +
                "Key Features: %s\n\n" +
                "Return a well-formatted structured output containing suggestedCategory, enhancedDescription, aiSummary, tags (list of 4-6 keywords), and sellingPoints (list of 3-5 bullet points).",
                request.getProductName(),
                request.getCurrentDescription() != null ? request.getCurrentDescription() : "N/A",
                request.getCategory() != null ? request.getCategory() : "N/A",
                request.getTargetAudience() != null ? request.getTargetAudience() : "General consumers",
                request.getKeyFeatures() != null ? request.getKeyFeatures() : "High quality, durable"
        );

        try {
            return chatClient.prompt()
                    .user(prompt)
                    .call()
                    .entity(AiEnrichmentResponse.class);
        } catch (Exception ex) {
            log.warn("Spring AI call failed or key not configured. Using fallback AI generator: {}", ex.getMessage());
            if (mockFallbackEnabled) {
                return generateFallbackEnrichment(request);
            }
            throw new RuntimeException("AI enrichment failed: " + ex.getMessage(), ex);
        }
    }

    @Override
    @Transactional
    public AiEnrichmentResponse enrichExistingProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));

        AiEnrichmentRequest request = AiEnrichmentRequest.builder()
                .productName(product.getName())
                .currentDescription(product.getDescription())
                .category(product.getCategory())
                .build();

        AiEnrichmentResponse response = enrichProduct(request);

        // Update product with AI insights
        if (response.getAiSummary() != null) {
            product.setAiSummary(response.getAiSummary());
        }
        if (response.getTags() != null && !response.getTags().isEmpty()) {
            product.setAiTags(String.join(", ", response.getTags()));
        }
        if (response.getEnhancedDescription() != null &&
                (product.getDescription() == null || product.getDescription().isBlank())) {
            product.setDescription(response.getEnhancedDescription());
        }

        productRepository.save(product);
        return response;
    }

    @Override
    public ReviewAnalysisResponse analyzeProductReviews(Long productId, String reviewsText) {
        log.info("Analyzing reviews for product ID: {}", productId);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));

        String prompt = String.format(
                "Analyze the following customer reviews for the product '%s' (Category: %s):\n\n" +
                "Reviews:\n\"%s\"\n\n" +
                "Provide a comprehensive review analysis including sentiment (POSITIVE, NEUTRAL, or NEGATIVE), " +
                "sentimentScore (number between 0.0 and 10.0), executiveSummary, positiveHighlights (list), " +
                "areasForImprovement (list), and recommendationForBuyer.",
                product.getName(),
                product.getCategory(),
                reviewsText
        );

        try {
            return chatClient.prompt()
                    .user(prompt)
                    .call()
                    .entity(ReviewAnalysisResponse.class);
        } catch (Exception ex) {
            log.warn("Spring AI call failed or key not configured. Using fallback review analyzer: {}", ex.getMessage());
            if (mockFallbackEnabled) {
                return generateFallbackReviewAnalysis(product, reviewsText);
            }
            throw new RuntimeException("AI review analysis failed: " + ex.getMessage(), ex);
        }
    }

    @Override
    public ChatMessageDto.Response processChatMessage(ChatMessageDto.Request request) {
        String conversationId = request.getConversationId() != null
                ? request.getConversationId()
                : UUID.randomUUID().toString();

        log.info("Processing AI Chat message [conv: {}]: {}", conversationId, request.getMessage());

        try {
            String aiAnswer = chatClient.prompt()
                    .user(request.getMessage())
                    .call()
                    .content();

            return ChatMessageDto.Response.builder()
                    .response(aiAnswer)
                    .conversationId(conversationId)
                    .toolActions(List.of("Executed Spring AI Function Tools"))
                    .build();
        } catch (Exception ex) {
            log.warn("Spring AI call failed: {}. Providing fallback response.", ex.getMessage());
            if (mockFallbackEnabled) {
                return generateFallbackChatResponse(request.getMessage(), conversationId);
            }
            throw new RuntimeException("AI chat failed: " + ex.getMessage(), ex);
        }
    }

    // =========================================================================
    // Fallback Mock Implementations for Zero-Configuration / Offline Execution
    // =========================================================================

    private AiEnrichmentResponse generateFallbackEnrichment(AiEnrichmentRequest request) {
        String name = request.getProductName();
        String category = request.getCategory() != null && !request.getCategory().isBlank()
                ? request.getCategory()
                : inferCategory(name);

        return AiEnrichmentResponse.builder()
                .suggestedCategory(category)
                .enhancedDescription("Engineered for premium performance and durability, the " + name +
                        " offers cutting-edge design, intuitive usability, and exceptional reliability for everyday demands.")
                .aiSummary("Top-tier " + category.toLowerCase(Locale.ROOT) + " solution featuring premium build quality and ergonomic design.")
                .tags(List.of(
                        category.toLowerCase(Locale.ROOT),
                        "premium",
                        "top-rated",
                        "bestseller",
                        name.toLowerCase(Locale.ROOT).split(" ")[0]
                ))
                .sellingPoints(List.of(
                        "Industry-leading build quality and long-lasting durability",
                        "Ergonomic and modern aesthetic crafted for daily efficiency",
                        "Highly rated by verified customers for ease of use",
                        "Backed by standard comprehensive warranty coverage"
                ))
                .build();
    }

    private ReviewAnalysisResponse generateFallbackReviewAnalysis(Product product, String reviewsText) {
        boolean hasNegativeWords = reviewsText != null &&
                (reviewsText.toLowerCase().contains("bad") ||
                 reviewsText.toLowerCase().contains("poor") ||
                 reviewsText.toLowerCase().contains("broken") ||
                 reviewsText.toLowerCase().contains("slow"));

        String sentiment = hasNegativeWords ? "NEUTRAL" : "POSITIVE";
        double score = hasNegativeWords ? 6.8 : 9.2;

        return ReviewAnalysisResponse.builder()
                .sentiment(sentiment)
                .sentimentScore(score)
                .executiveSummary("Customers praise the " + product.getName() + " for its high performance and aesthetic value.")
                .positiveHighlights(List.of(
                        "Outstanding value for the price point",
                        "Sleek and sturdy design",
                        "Fast setup and intuitive operation"
                ))
                .areasForImprovement(List.of(
                        "Packaging could include more detailed printed documentation",
                        "Minor learning curve for advanced settings"
                ))
                .recommendationForBuyer("Highly recommended for users seeking a reliable " + product.getCategory().toLowerCase() + " solution.")
                .build();
    }

    private ChatMessageDto.Response generateFallbackChatResponse(String userMessage, String convId) {
        String lower = userMessage.toLowerCase();
        List<String> actions = new ArrayList<>();
        String reply;

        if (lower.contains("summary") || lower.contains("catalog") || lower.contains("overview") || lower.contains("stats")) {
            long count = productRepository.count();
            List<String> cats = productRepository.findAllCategories();
            actions.add("getCatalogSummaryFunction");
            reply = String.format("📊 **Catalog Summary**:\n- Total Products: %d\n- Available Categories: %s\n- Database Status: Online and healthy!",
                    count, String.join(", ", cats));
        } else if (lower.contains("low stock") || lower.contains("stock") || lower.contains("inventory")) {
            List<Product> lowStock = productRepository.findByStockQuantityLessThan(10);
            actions.add("getLowStockProductsFunction(10)");
            if (lowStock.isEmpty()) {
                reply = "✅ All items have sufficient stock levels (> 10 units in stock).";
            } else {
                StringBuilder sb = new StringBuilder("⚠️ **Low Stock Alert (less than 10 units)**:\n");
                lowStock.forEach(p -> sb.append(String.format("- **%s** (ID: %d): Only %d units remaining ($%.2f)\n",
                        p.getName(), p.getId(), p.getStockQuantity(), p.getPrice())));
                reply = sb.toString();
            }
        } else if (lower.contains("find") || lower.contains("search") || lower.contains("show") || lower.contains("list")) {
            String keyword = userMessage.replaceAll("(?i)(find|search|show|list|me|all|products|product|the)", "").trim();
            if (keyword.isBlank()) keyword = "a";
            List<Product> found = productRepository.searchProducts(keyword);
            actions.add("searchProductsFunction('" + keyword + "')");
            if (found.isEmpty()) {
                reply = "🔍 No products found matching your search. Try searching for 'Keyboard', 'Desk', 'Chair', or 'Book'.";
            } else {
                StringBuilder sb = new StringBuilder("🔍 **Matching Products**:\n");
                found.stream().limit(5).forEach(p -> sb.append(String.format("- **%s** ($%.2f) - Category: %s, Stock: %d\n",
                        p.getName(), p.getPrice(), p.getCategory(), p.getStockQuantity())));
                reply = sb.toString();
            }
        } else {
            reply = "👋 Hello! I am your AI Catalog Assistant powered by Spring AI. You can ask me to:\n" +
                    "- Search items (e.g., *'Find wireless headphones'*)\n" +
                    "- Check inventory (e.g., *'Show low stock items'*)\n" +
                    "- Get catalog stats (e.g., *'Summarize our catalog'*)\n" +
                    "- Create new products via conversation!";
        }

        return ChatMessageDto.Response.builder()
                .response(reply)
                .conversationId(convId)
                .toolActions(actions)
                .build();
    }

    private String inferCategory(String name) {
        String lower = name.toLowerCase();
        if (lower.contains("phone") || lower.contains("laptop") || lower.contains("keyboard") ||
            lower.contains("mouse") || lower.contains("headphone") || lower.contains("monitor")) {
            return "Electronics";
        } else if (lower.contains("chair") || lower.contains("desk") || lower.contains("table") ||
                   lower.contains("lamp") || lower.contains("sofa")) {
            return "Furniture";
        } else if (lower.contains("shirt") || lower.contains("jacket") || lower.contains("shoes") ||
                   lower.contains("pants") || lower.contains("hat")) {
            return "Clothing";
        } else if (lower.contains("book") || lower.contains("guide") || lower.contains("manual")) {
            return "Books";
        }
        return "General";
    }
}

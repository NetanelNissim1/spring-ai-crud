package com.example.aicrud.controller;

import com.example.aicrud.dto.ChatMessageDto;
import com.example.aicrud.service.AiProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai/chat")
@CrossOrigin(origins = "*")
@Tag(name = "AI Chat Assistant", description = "Conversational assistant with Spring AI Tool / Function Calling to manage products and query catalog")
public class AiChatController {

    private final AiProductService aiProductService;

    public AiChatController(AiProductService aiProductService) {
        this.aiProductService = aiProductService;
    }

    @PostMapping
    @Operation(summary = "Chat with AI catalog assistant", description = "Send natural language messages to search, summarize, create, or inspect products via autonomous tool calling.")
    public ResponseEntity<ChatMessageDto.Response> chat(@Valid @RequestBody ChatMessageDto.Request request) {
        return ResponseEntity.ok(aiProductService.processChatMessage(request));
    }
}

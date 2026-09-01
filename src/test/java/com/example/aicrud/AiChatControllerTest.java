package com.example.aicrud;

import com.example.aicrud.controller.AiChatController;
import com.example.aicrud.dto.ChatMessageDto;
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
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AiChatController.class)
class AiChatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AiProductService aiProductService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("POST /api/ai/chat should return AI assistant response")
    void testChatEndpoint() throws Exception {
        ChatMessageDto.Request request = ChatMessageDto.Request.builder()
                .message("Show me all electronics under $100")
                .conversationId("test-conv-123")
                .build();

        ChatMessageDto.Response response = ChatMessageDto.Response.builder()
                .response("Found 3 products matching your criteria.")
                .conversationId("test-conv-123")
                .toolActions(List.of("searchProductsFunction('electronics')"))
                .build();

        when(aiProductService.processChatMessage(any(ChatMessageDto.Request.class))).thenReturn(response);

        mockMvc.perform(post("/api/ai/chat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response").value("Found 3 products matching your criteria."))
                .andExpect(jsonPath("$.conversationId").value("test-conv-123"))
                .andExpect(jsonPath("$.toolActions[0]").value("searchProductsFunction('electronics')"));

        verify(aiProductService, times(1)).processChatMessage(any(ChatMessageDto.Request.class));
    }

    @Test
    @DisplayName("POST /api/ai/chat with blank message should return 400 Bad Request")
    void testChatEndpointBlankMessage() throws Exception {
        ChatMessageDto.Request invalidRequest = ChatMessageDto.Request.builder()
                .message("")
                .build();

        mockMvc.perform(post("/api/ai/chat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors.message").exists());

        verify(aiProductService, never()).processChatMessage(any());
    }
}

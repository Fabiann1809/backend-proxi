package com.fabian.aiproxy.controller;

import com.fabian.aiproxy.dto.request.GenerationRequest;
import com.fabian.aiproxy.dto.response.GenerationResponse;
import com.fabian.aiproxy.service.AIGenerationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai")
public class AIController {

    private final AIGenerationService aiGenerationService;

    public AIController(AIGenerationService aiGenerationService) {
        this.aiGenerationService = aiGenerationService;
    }

    @PostMapping("/generate")
    public ResponseEntity<GenerationResponse> generate(@Valid @RequestBody GenerationRequest request) throws Exception {
        return ResponseEntity.ok(aiGenerationService.generate(request));
    }
}

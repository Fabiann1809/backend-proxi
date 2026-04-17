package com.fabian.aiproxy.service;

import com.fabian.aiproxy.dto.request.GenerationRequest;
import com.fabian.aiproxy.dto.response.GenerationResponse;

public interface AIGenerationService {

    GenerationResponse generate(GenerationRequest request) throws Exception;
}

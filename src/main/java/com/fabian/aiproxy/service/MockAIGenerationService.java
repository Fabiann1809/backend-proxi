package com.fabian.aiproxy.service;

import com.fabian.aiproxy.dto.request.GenerationRequest;
import com.fabian.aiproxy.dto.response.GenerationResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
public class MockAIGenerationService implements AIGenerationService {

    private static final List<String> RESPONSES = List.of(
        "Artificial intelligence is reshaping industries by automating complex tasks that once required human expertise, enabling faster and more accurate decision-making at scale.",
        "Machine learning models trained on vast datasets can now recognize patterns invisible to the human eye, revolutionizing fields like medical imaging and financial fraud detection.",
        "Quantum computing promises to solve optimization problems in seconds that would take classical computers millions of years, opening new frontiers in cryptography and drug discovery.",
        "Neural networks inspired by the human brain have achieved superhuman performance in games like chess and Go, demonstrating the power of reinforcement learning in constrained environments.",
        "Natural language processing has advanced to the point where AI systems can understand context, sentiment, and nuance in text, enabling human-like conversation and automated content generation.",
        "Edge computing combined with AI allows devices to process data locally without sending it to the cloud, reducing latency and improving privacy in IoT applications.",
        "The convergence of robotics and deep learning is producing autonomous systems capable of navigating dynamic environments, from warehouse logistics to surgical assistance.",
        "Generative AI models can now create photorealistic images, compose music, and write code, blurring the line between human creativity and machine capability.",
        "Federated learning enables AI models to be trained across distributed devices without sharing raw data, preserving user privacy while still improving model accuracy.",
        "Explainable AI is becoming critical in regulated industries, requiring models to justify their predictions in human-understandable terms to ensure fairness and accountability."
    );

    private final Random random = new Random();

    @Override
    public GenerationResponse generate(GenerationRequest request) throws Exception {
        long start = System.currentTimeMillis();

        Thread.sleep(1200);

        String responseText = RESPONSES.get(random.nextInt(RESPONSES.size()));
        int tokensUsed = request.getPrompt().length() / 4 + responseText.length() / 4;

        return GenerationResponse.builder()
                .userId(request.getUserId())
                .generatedText(responseText)
                .tokensUsed(tokensUsed)
                .processingTimeMs(System.currentTimeMillis() - start)
                .timestamp(LocalDateTime.now())
                .build();
    }
}

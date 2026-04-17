package com.fabian.aiproxy.controller;

import com.fabian.aiproxy.dto.response.DailyUsageResponse;
import com.fabian.aiproxy.dto.response.QuotaStatusResponse;
import com.fabian.aiproxy.model.enums.Plan;
import com.fabian.aiproxy.service.UserQuotaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/quota")
public class QuotaController {

    private final UserQuotaService quotaService;

    public QuotaController(UserQuotaService quotaService) {
        this.quotaService = quotaService;
    }

    @GetMapping("/status")
    public ResponseEntity<QuotaStatusResponse> getStatus(@RequestParam String userId) {
        return ResponseEntity.ok(quotaService.getStatus(userId));
    }

    @GetMapping("/history")
    public ResponseEntity<List<DailyUsageResponse>> getHistory(@RequestParam String userId) {
        return ResponseEntity.ok(quotaService.getHistory(userId));
    }

    @PostMapping("/upgrade")
    public ResponseEntity<QuotaStatusResponse> upgradePlan(
            @RequestParam String userId,
            @RequestParam Plan plan
    ) {
        quotaService.upgradePlan(userId, plan);
        return ResponseEntity.ok(quotaService.getStatus(userId));
    }
}

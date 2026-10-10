
package com.smarttrip.controller;

import com.smarttrip.service.RagService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/rag")
public class RagController {

    private final RagService ragService;

    public RagController(RagService ragService) {
        this.ragService = ragService;
    }

    @PostMapping("/ask")
    public ResponseEntity<Map<String, String>> ask(
            @RequestBody Map<String, String> request) {

        String question = request.get("question");

        if (question == null || question.isBlank()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Please enter a question."));
        }

        try {
            Map<String, Object> result = ragService.ask(question);

            return ResponseEntity.ok(Map.of(
                    "answer", String.valueOf(result.get("answer")),
                    "sources", String.valueOf(result.get("sources"))
            ));

        } catch (Exception e) {
            e.printStackTrace();

            return ResponseEntity.internalServerError()
                    .body(Map.of(
                            "error",
                            "AI request failed. Check the backend terminal for the exact error."
                    ));
        }
    }
}

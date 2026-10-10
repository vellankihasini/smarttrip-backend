
package com.smarttrip.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class RagService {

    @Value("${GROQ_API_KEY:}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper mapper = new ObjectMapper();

    private final List<String> knowledge = List.of(
        "Paris: Eiffel Tower, Louvre Museum, Notre-Dame, Seine river cruise. "
            + "Budget activities include walking around Montmartre and public gardens.",
        "Switzerland: Interlaken, Lucerne, Lake Geneva, scenic train journeys, "
            + "mountain walks and lake activities. Mountain excursions can be expensive.",
        "Hyderabad: Charminar, Golconda Fort, Hussain Sagar, museums and local food.",
        "Budget planning: Include accommodation, transport, food, activities, "
            + "shopping and an emergency reserve. Prices vary by date and season.",
        "Travel safety: Check official opening times, local transport schedules, "
            + "weather forecasts and entry requirements before travelling."
    );

    private String retrieve(String question) {
        String q = question.toLowerCase();
        StringBuilder result = new StringBuilder();

        for (String item : knowledge) {
            String lower = item.toLowerCase();

            if (Arrays.stream(q.split("\\W+"))
                    .filter(word -> word.length() > 3)
                    .anyMatch(lower::contains)) {
                result.append(item).append("\n");
            }
        }

        if (result.isEmpty()) {
            result.append(String.join("\n", knowledge));
        }

        return result.toString();
    }

    public Map<String, Object> ask(String question) {
        if (question == null || question.isBlank()) {
            throw new IllegalArgumentException("Please enter a question.");
        }

        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException(
                "Groq API key is missing. Set GROQ_API_KEY and restart the backend."
            );
        }

        try {
            String context = retrieve(question);

            Map<String, Object> body = new HashMap<>();
            body.put("model", "llama-3.3-70b-versatile");
            body.put("temperature", 0.4);
            body.put("max_tokens", 1200);
            body.put("messages", List.of(
                Map.of(
                    "role", "system",
                    "content",
                    "You are SmartTrip, a helpful personalized travel planning assistant. "
                    + "Answer clearly and practically. For itinerary requests, organize "
                    + "recommendations day by day. Include estimated costs in INR when useful, "
                    + "but label them as estimates, not verified live prices. Never invent "
                    + "live availability or claim prices are current. Ask for missing details "
                    + "when needed. Use this retrieved travel knowledge as context:\n"
                    + context
                ),
                Map.of("role", "user", "content", question)
            ));

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);

            ResponseEntity<String> response = restTemplate.exchange(
                "https://api.groq.com/openai/v1/chat/completions",
                HttpMethod.POST,
                new HttpEntity<>(body, headers),
                String.class
            );

            JsonNode root = mapper.readTree(response.getBody());
            JsonNode answerNode = root.path("choices")
                                      .path(0)
                                      .path("message")
                                      .path("content");

            if (answerNode.isMissingNode() || answerNode.asText().isBlank()) {
                throw new IllegalStateException("AI returned an empty response.");
            }

            Map<String, Object> result = new HashMap<>();
            result.put("answer", answerNode.asText());
            result.put("sources", List.of("SmartTrip travel knowledge base"));
            return result;

        } catch (Exception e) {
            throw new IllegalStateException(
                "AI request failed: " + e.getMessage(), e
            );
        }
    }
}

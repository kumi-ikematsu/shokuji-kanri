package com.example.shokuji_kanri.controller;

import com.example.shokuji_kanri.entity.MealRecord;
import com.example.shokuji_kanri.entity.User;
import com.example.shokuji_kanri.repository.UserRepository;
import com.example.shokuji_kanri.service.MealRecordService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/photo")
public class PhotoUploadController {

    private final MealRecordService mealRecordService;
    private final UserRepository userRepository;

    @Value("${anthropic.api.key}")
    private String anthropicApiKey;

    @GetMapping("/upload")
    public String uploadForm() {
        return "photo-upload";
    }

    @PostMapping("/analyze")
    public String analyze(
            @RequestParam("photo") MultipartFile photo,
            @AuthenticationPrincipal UserDetails userDetails,
            Model model) {
        try {
            System.out.println("★ analyze called");

            String base64Image = Base64.getEncoder().encodeToString(photo.getBytes());

            String originalType = photo.getContentType();
            String mediaType;
            if (originalType != null && originalType.contains("jpeg")) {
                mediaType = "image/jpeg";
            } else if (originalType != null && originalType.contains("png")) {
                mediaType = "image/png";
            } else if (originalType != null && originalType.contains("gif")) {
                mediaType = "image/gif";
            } else if (originalType != null && originalType.contains("webp")) {
                mediaType = "image/webp";
            } else {
                mediaType = "image/jpeg";
            }

            System.out.println("★ mediaType: " + mediaType);

            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("x-api-key", anthropicApiKey);
            headers.set("anthropic-version", "2023-06-01");

            String prompt = "この料理の写真を見て、以下の情報をJSON形式で返してください。" +
                    "{\n" +
                    "  \"foodName\": \"食品名\",\n" +
                    "  \"calories\": 数値,\n" +
                    "  \"proteinG\": 数値,\n" +
                    "  \"fatG\": 数値,\n" +
                    "  \"carbsG\": 数値\n" +
                    "}\n" +
                    "数値は整数または小数で返してください。JSONのみ返してください。";

            Map<String, Object> imageSource = new HashMap<>();
            imageSource.put("type", "base64");
            imageSource.put("media_type", mediaType);
            imageSource.put("data", base64Image);

            Map<String, Object> imageContent = new HashMap<>();
            imageContent.put("type", "image");
            imageContent.put("source", imageSource);

            Map<String, Object> textContent = new HashMap<>();
            textContent.put("type", "text");
            textContent.put("text", prompt);

            Map<String, Object> message = new HashMap<>();
            message.put("role", "user");
            message.put("content", List.of(imageContent, textContent));

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", "claude-haiku-4-5");
            requestBody.put("max_tokens", 500);
            requestBody.put("messages", List.of(message));

            System.out.println("★ Sending request to Anthropic API...");
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(
                    "https://api.anthropic.com/v1/messages", request, String.class);

            System.out.println("★ Response: " + response.getStatusCode());

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.getBody());
            String text = root.get("content").get(0).get("text").asText();

            System.out.println("★ AI Response: " + text);

            text = text.trim();
            if (text.startsWith("```")) {
                text = text.replaceAll("```json", "").replaceAll("```", "").trim();
            }

            JsonNode result = mapper.readTree(text);

            model.addAttribute("foodName", result.get("foodName").asText());
            model.addAttribute("calories", result.get("calories").asDouble());
            model.addAttribute("proteinG", result.get("proteinG").asDouble());
            model.addAttribute("fatG", result.get("fatG").asDouble());
            model.addAttribute("carbsG", result.get("carbsG").asDouble());
            model.addAttribute("imageBase64", base64Image);
            model.addAttribute("imageMediaType", mediaType);

        } catch (Exception e) {
            System.out.println("★ Error: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "解析に失敗しました: " + e.getMessage());
            return "photo-upload";
        }

        return "ai-result";
    }

    @PostMapping("/save")
    public String save(
            @RequestParam String foodName,
            @RequestParam String mealType,
            @RequestParam double calories,
            @RequestParam double proteinG,
            @RequestParam double fatG,
            @RequestParam double carbsG,
            @AuthenticationPrincipal UserDetails userDetails) {

        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();

        MealRecord record = new MealRecord();
        record.setUserId(user.getId());
        record.setFoodName(foodName);
        record.setMealType(mealType);
        record.setMealDate(LocalDate.now());
        record.setCalories(calories);
        record.setProteinG(proteinG);
        record.setFatG(fatG);
        record.setCarbsG(carbsG);

        mealRecordService.save(record);
        return "redirect:/meals";
    }
}
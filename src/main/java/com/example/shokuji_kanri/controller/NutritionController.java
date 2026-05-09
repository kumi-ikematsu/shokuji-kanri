package com.example.shokuji_kanri.controller;

import com.example.shokuji_kanri.entity.MealRecord;
import com.example.shokuji_kanri.entity.User;
import com.example.shokuji_kanri.repository.UserRepository;
import com.example.shokuji_kanri.service.MealRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class NutritionController {

    private final MealRecordService mealRecordService;
    private final UserRepository userRepository;

    @GetMapping("/nutrition")
    public String nutrition(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
        List<MealRecord> todayMeals = mealRecordService.findByUserIdAndDate(user.getId(), LocalDate.now());

        double totalCalories = todayMeals.stream().mapToDouble(m -> m.getCalories() != null ? m.getCalories() : 0).sum();
        double totalProtein = todayMeals.stream().mapToDouble(m -> m.getProteinG() != null ? m.getProteinG() : 0).sum();
        double totalFat = todayMeals.stream().mapToDouble(m -> m.getFatG() != null ? m.getFatG() : 0).sum();
        double totalCarbs = todayMeals.stream().mapToDouble(m -> m.getCarbsG() != null ? m.getCarbsG() : 0).sum();

        model.addAttribute("totalCalories", totalCalories);
        model.addAttribute("totalProtein", totalProtein);
        model.addAttribute("totalFat", totalFat);
        model.addAttribute("totalCarbs", totalCarbs);
        model.addAttribute("mealCount", todayMeals.size());
        model.addAttribute("meals", todayMeals);

        return "nutrition";
    }
}
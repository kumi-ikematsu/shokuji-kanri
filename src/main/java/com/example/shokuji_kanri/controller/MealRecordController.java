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
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/meals")
public class MealRecordController {

    private final MealRecordService mealRecordService;
    private final UserRepository userRepository;

    @GetMapping
    public String list(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
        List<MealRecord> meals = mealRecordService.findByUserIdAndDate(user.getId(), LocalDate.now());

        double totalCalories = meals.stream().mapToDouble(m -> m.getCalories() != null ? m.getCalories() : 0).sum();
        double totalProtein = meals.stream().mapToDouble(m -> m.getProteinG() != null ? m.getProteinG() : 0).sum();
        double totalFat = meals.stream().mapToDouble(m -> m.getFatG() != null ? m.getFatG() : 0).sum();
        double totalCarbs = meals.stream().mapToDouble(m -> m.getCarbsG() != null ? m.getCarbsG() : 0).sum();

        model.addAttribute("meals", meals);
        model.addAttribute("totalCalories", totalCalories);
        model.addAttribute("totalProtein", totalProtein);
        model.addAttribute("totalFat", totalFat);
        model.addAttribute("totalCarbs", totalCarbs);

        return "meals/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("mealRecord", new MealRecord());
        return "meals/form";
    }

    @PostMapping("/new")
    public String create(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam String mealType,
            @RequestParam String foodName,
            @RequestParam String mealDate,
            @RequestParam(required = false) Double amountG,
            @RequestParam(required = false) Double calories,
            @RequestParam(required = false) Double proteinG,
            @RequestParam(required = false) Double fatG,
            @RequestParam(required = false) Double carbsG) {

        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();

        MealRecord record = new MealRecord();
        record.setUserId(user.getId());
        record.setMealType(mealType);
        record.setFoodName(foodName);
        record.setMealDate(LocalDate.parse(mealDate));
        record.setAmountG(amountG);
        record.setCalories(calories);
        record.setProteinG(proteinG);
        record.setFatG(fatG);
        record.setCarbsG(carbsG);

        mealRecordService.save(record);
        return "redirect:/meals";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        MealRecord meal = mealRecordService.findById(id).orElseThrow();
        model.addAttribute("meal", meal);
        return "meals/edit";
    }

    @PostMapping("/edit/{id}")
    public String update(
            @PathVariable Long id,
            @RequestParam String mealType,
            @RequestParam String foodName,
            @RequestParam String mealDate,
            @RequestParam(required = false) Double calories,
            @RequestParam(required = false) Double proteinG,
            @RequestParam(required = false) Double fatG,
            @RequestParam(required = false) Double carbsG) {

        MealRecord record = mealRecordService.findById(id).orElseThrow();
        record.setMealType(mealType);
        record.setFoodName(foodName);
        record.setMealDate(LocalDate.parse(mealDate));
        record.setCalories(calories);
        record.setProteinG(proteinG);
        record.setFatG(fatG);
        record.setCarbsG(carbsG);

        mealRecordService.save(record);
        return "redirect:/meals";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        mealRecordService.deleteById(id);
        return "redirect:/meals";
    }
}
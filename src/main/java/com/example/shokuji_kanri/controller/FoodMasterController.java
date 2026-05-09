package com.example.shokuji_kanri.controller;

import com.example.shokuji_kanri.entity.FoodMaster;
import com.example.shokuji_kanri.service.FoodMasterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/food")
public class FoodMasterController {

    private final FoodMasterService foodMasterService;

    @GetMapping
    public String list(@RequestParam(required = false) String keyword, Model model) {
        List<FoodMaster> foods;
        if (keyword != null && !keyword.isEmpty()) {
            foods = foodMasterService.findByKeyword(keyword);
        } else {
            foods = foodMasterService.findAll();
        }
        model.addAttribute("foods", foods);
        model.addAttribute("keyword", keyword);
        return "admin/food-list";
    }

    @GetMapping("/new")
    public String newForm() {
        return "admin/food-form";
    }

    @PostMapping("/new")
    public String create(
            @RequestParam String foodName,
            @RequestParam(required = false) Double calories,
            @RequestParam(required = false) Double proteinG,
            @RequestParam(required = false) Double fatG,
            @RequestParam(required = false) Double carbsG,
            @RequestParam(required = false) Double servingSizeG) {

        FoodMaster food = new FoodMaster();
        food.setFoodName(foodName);
        food.setCalories(calories);
        food.setProteinG(proteinG);
        food.setFatG(fatG);
        food.setCarbsG(carbsG);
        food.setServingSizeG(servingSizeG);

        foodMasterService.save(food);
        return "redirect:/admin/food";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        FoodMaster food = foodMasterService.findById(id).orElseThrow();
        model.addAttribute("food", food);
        return "admin/food-edit";
    }

    @PostMapping("/edit/{id}")
    public String update(
            @PathVariable Long id,
            @RequestParam String foodName,
            @RequestParam(required = false) Double calories,
            @RequestParam(required = false) Double proteinG,
            @RequestParam(required = false) Double fatG,
            @RequestParam(required = false) Double carbsG,
            @RequestParam(required = false) Double servingSizeG) {

        FoodMaster food = foodMasterService.findById(id).orElseThrow();
        food.setFoodName(foodName);
        food.setCalories(calories);
        food.setProteinG(proteinG);
        food.setFatG(fatG);
        food.setCarbsG(carbsG);
        food.setServingSizeG(servingSizeG);

        foodMasterService.save(food);
        return "redirect:/admin/food";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        foodMasterService.deleteById(id);
        return "redirect:/admin/food";
    }
}
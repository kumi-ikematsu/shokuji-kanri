package com.example.shokuji_kanri.controller;

import com.example.shokuji_kanri.entity.User;
import com.example.shokuji_kanri.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/profile")
public class ProfileController {

    private final UserRepository userRepository;

    @GetMapping
    public String profile(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
        model.addAttribute("user", user);
        return "profile";
    }

    @PostMapping
    public String update(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam String username,
            @RequestParam(required = false) Double heightCm,
            @RequestParam(required = false) Double weightKg,
            Model model) {

        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
        user.setUsername(username);
        user.setHeightCm(heightCm);
        user.setWeightKg(weightKg);
        userRepository.save(user);

        model.addAttribute("user", user);
        model.addAttribute("success", "プロフィールを更新しました！");
        return "profile";
    }
}
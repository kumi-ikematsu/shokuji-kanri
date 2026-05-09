package com.example.shokuji_kanri.controller;

import com.example.shokuji_kanri.entity.User;
import com.example.shokuji_kanri.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/users")
public class AdminUserController {

    private final UserRepository userRepository;

    @GetMapping
    public String list(Model model) {
        List<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "admin/user-list";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        User user = userRepository.findById(id).orElseThrow();
        model.addAttribute("user", user);
        return "admin/user-edit";
    }

    @PostMapping("/edit/{id}")
    public String update(
            @PathVariable Long id,
            @RequestParam String username,
            @RequestParam String email) {

        User user = userRepository.findById(id).orElseThrow();
        user.setUsername(username);
        user.setEmail(email);
        userRepository.save(user);
        return "redirect:/admin/users";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        userRepository.deleteById(id);
        return "redirect:/admin/users";
    }
}
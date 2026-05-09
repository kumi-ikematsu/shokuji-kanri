package com.example.shokuji_kanri.service;

import com.example.shokuji_kanri.entity.User;
import com.example.shokuji_kanri.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void register(String username, String email, String password, Double heightCm, Double weightKg) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("このメールアドレスはすでに登録されています。");
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(password));
        user.setRoleId(1L);
        user.setHeightCm(heightCm);
        user.setWeightKg(weightKg);

        userRepository.save(user);
    }
}
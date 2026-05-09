package com.example.shokuji_kanri.service;

import com.example.shokuji_kanri.entity.MealRecord;
import com.example.shokuji_kanri.repository.MealRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MealRecordService {

    private final MealRecordRepository mealRecordRepository;

    public List<MealRecord> findByUserId(Long userId) {
        return mealRecordRepository.findByUserIdOrderByMealDateDesc(userId);
    }

    public List<MealRecord> findByUserIdAndDate(Long userId, LocalDate date) {
        return mealRecordRepository.findByUserIdAndMealDate(userId, date);
    }

    public Optional<MealRecord> findById(Long id) {
        return mealRecordRepository.findById(id);
    }

    public void save(MealRecord mealRecord) {
        mealRecordRepository.save(mealRecord);
    }

    public void deleteById(Long id) {
        mealRecordRepository.deleteById(id);
    }
}
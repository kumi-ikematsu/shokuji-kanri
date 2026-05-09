package com.example.shokuji_kanri.repository;

import com.example.shokuji_kanri.entity.MealRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface MealRecordRepository extends JpaRepository<MealRecord, Long> {
    List<MealRecord> findByUserIdOrderByMealDateDesc(Long userId);
    List<MealRecord> findByUserIdAndMealDate(Long userId, LocalDate mealDate);
}
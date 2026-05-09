package com.example.shokuji_kanri.repository;

import com.example.shokuji_kanri.entity.FoodMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FoodMasterRepository extends JpaRepository<FoodMaster, Long> {
    List<FoodMaster> findByFoodNameContaining(String keyword);
}
package com.example.shokuji_kanri.service;

import com.example.shokuji_kanri.entity.FoodMaster;
import com.example.shokuji_kanri.repository.FoodMasterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FoodMasterService {

    private final FoodMasterRepository foodMasterRepository;

    public List<FoodMaster> findAll() {
        return foodMasterRepository.findAll();
    }

    public List<FoodMaster> findByKeyword(String keyword) {
        return foodMasterRepository.findByFoodNameContaining(keyword);
    }

    public Optional<FoodMaster> findById(Long id) {
        return foodMasterRepository.findById(id);
    }

    public void save(FoodMaster foodMaster) {
        foodMasterRepository.save(foodMaster);
    }

    public void deleteById(Long id) {
        foodMasterRepository.deleteById(id);
    }
}
package com.fitness.AI.service.service;

import com.fitness.AI.service.model.Recommendation;
import com.fitness.AI.service.repository.RecommendationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecommendationService {
    @Autowired
    private RecommendationRepo recommendationRepo;

    public List<Recommendation> getUserRecommendation(String userId) {
        return recommendationRepo.findByUserId(userId);
    }

    public Recommendation getActivityRecommendation(String activityId) {
        return recommendationRepo.findByActivityId(activityId)
                .orElseThrow(() -> new RuntimeException("No recommendation found for this activityId: "+ activityId));
    }
}

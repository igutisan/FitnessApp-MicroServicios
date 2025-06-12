package com.fitness.AI.service.service;

import com.fitness.AI.service.model.Activity;
import com.fitness.AI.service.model.Recommendation;
import com.fitness.AI.service.repository.RecommendationRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ActivityMessageListener {

    @Autowired
    private ActivityAiService aiService;

    @Autowired
    private RecommendationRepo recommendationRepo;

    @RabbitListener(queues = "activity.queue")
    public void processActivity(Activity activity){
        log.info("Received activity for processing: {}",activity.getId());
       // log.info("Generated Recommendation: {} ", aiService.generateRecommendation(activity));
        Recommendation recommendation = aiService.generateRecommendation(activity);
        recommendationRepo.save(recommendation);
    }
}

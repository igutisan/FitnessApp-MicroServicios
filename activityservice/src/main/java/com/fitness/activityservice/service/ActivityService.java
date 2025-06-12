package com.fitness.activityservice.service;

import com.fitness.activityservice.dto.ActivityRequest;
import com.fitness.activityservice.dto.ActivityResponse;
import com.fitness.activityservice.model.Activity;
import com.fitness.activityservice.repository.ActivityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
public class ActivityService {

    @Autowired
    private ActivityRepository activityRepository;
    @Autowired
    private UserValidationService  userValidationService;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange.name}")
    private String exchange;
    @Value("${rabbitmq.routing.key}")
    private String routingKey;

    public ActivityResponse trackActivity(ActivityRequest request) {
        boolean isValidate = userValidationService.validateUser(request.getUserId());

        if(!isValidate) throw new RuntimeException("Invalid user: "+request.getUserId());

        Activity activity = Activity.builder()
                .userId(request.getUserId())
                .type(request.getType())
                .duration(request.getDuration())
                .caloriesBurned(request.getCaloriesBurned())
                .startTime(request.getStartTime())
                .additionalMetrics(request.getAdditionalMetrics())
                .build();
        Activity savedActivity = activityRepository.save(activity);

        //Publish to RabbitMQ for AI Processing
        try {
            rabbitTemplate.convertAndSend(exchange,routingKey,savedActivity);
        }catch (Exception e){
            log.error("Fail to publish activity to RabbitMQ : ",e);
        }
        return mapToResponse(savedActivity);
    }

    public ActivityResponse mapToResponse(Activity activity){
        ActivityResponse activityResponse = new ActivityResponse();
        activityResponse.setId(activity.getId());
        activityResponse.setType(activity.getType());
        activityResponse.setDuration(activity.getDuration());
        activityResponse.setCaloriesBurned(activity.getCaloriesBurned());
        activityResponse.setCreatedAt(activity.getCreatedAt());
        activityResponse.setStartTime(activity.getStartTime());
        activityResponse.setUpdatedAt(activity.getUpdatedAt());
        activityResponse.setUserId(activity.getUserId());
        activityResponse.setAdditionalMetrics(activity.getAdditionalMetrics());
        return activityResponse;
    }

    public List<ActivityResponse> getUserActivities(String userId) {
        List<Activity> activities = activityRepository.findByUserId(userId);
       return activities.stream()
               .map(this::mapToResponse)
               .collect(Collectors.toList());

    }

    public ActivityResponse getActivities(String activityId) {
       return activityRepository.findById(activityId)
               .map(this::mapToResponse)
               .orElseThrow(() -> new RuntimeException("Activity not found whit id: "+ activityId));
    }
}

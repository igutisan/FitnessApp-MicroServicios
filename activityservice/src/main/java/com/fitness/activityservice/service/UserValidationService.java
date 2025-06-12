package com.fitness.activityservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
@Slf4j
public class UserValidationService {
    @Autowired
    private WebClient userServiceWebClient;

    //Implementation necesary for consultance users with reactive programming with webflux
    public boolean validateUser(String userId){

        try {
            log.info("Calling User Validation API for userID: {userId}"+ userId);
           return Boolean.TRUE.equals(userServiceWebClient.get()
                   .uri("/api/users/{userId}/validate", userId)
                   .retrieve()
                   .bodyToMono(Boolean.class)
                   .block());
        }catch (WebClientResponseException e){
            if(e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new RuntimeException("Invalid URI");
            }
            else  if(e.getStatusCode() == HttpStatus.BAD_REQUEST){
                throw new RuntimeException("Invalid request: "+userId);
            }

        }
        return false;

    }

}

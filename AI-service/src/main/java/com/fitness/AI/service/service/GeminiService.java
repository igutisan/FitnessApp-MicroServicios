package com.fitness.AI.service.service;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import org.springframework.stereotype.Service;

@Service
public class GeminiService {
    public String getAnswer(String text){
        Client client = new Client();

        GenerateContentResponse response =
                client.models.generateContent(
                        "gemini-2.5-flash-preview-05-20",
                        text,
                        null);

        return response.text();
    }
}

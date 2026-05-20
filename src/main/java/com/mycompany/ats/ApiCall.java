/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.ats;

/**
 *
 * @author Mohit kumar
 */

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.json.*;


public class ApiCall {

    public JSONObject getResponse(String text) {
        
        try {
            

            String apiKey = "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";
           String invoke_url="https://integrate.api.nvidia.com/v1/chat/completions";

            String prompt = """
                             
                            
                            INPUT:
                            1. Resume text (raw string)
                            2. Optional Job Description (raw string, may be null)
                            
                            TASK:
                            Analyze the resume like a real ATS system and return a structured evaluation.
                            
                            EVALUATION RULES:
                            
                            1. BASIC INFORMATION CHECK
                            - Candidate name present
                            - Valid email present
                            - Valid phone number present
                            - Location/address (optional but preferred)
                            - LinkedIn/GitHub/portfolio (bonus)
                            
                            2. STRUCTURE & FORMATTING
                            - Sections: Summary, Education, Skills, Experience, Projects
                            - Clear section headings
                            - Bullet points usage
                            - No excessive symbols or tables (ATS readability)
                            
                            3. SKILLS ANALYSIS
                            IF job description is provided:
                                - Extract required skills from job description
                                - Compare with resume skills
                                - Calculate skill match percentage
                            ELSE:
                                - Infer relevant skills based on education + experience
                                - Check if skills are supported by projects/experience
                            
                            4. EXPERIENCE VALIDATION
                            - Are claims supported by measurable impact?
                            - Action verbs used?
                            - Any vague or weak descriptions?
                            
                            5. PROJECT VALIDATION
                            - Projects align with listed skills
                            - Projects show practical application
                            - Technical depth present
                            
                            6. KEYWORD OPTIMIZATION
                            - Presence of industry keywords
                            - Missing important keywords
                            
                            7. CONSISTENCY CHECK
                            - Skills vs experience vs projects alignment
                            - No contradictions
                            
                            SCORING:
                            Return a score out of 100 based on:
                            - Basic Info (10)
                            - Structure (15)
                            - Skills Match (25)
                            - Experience Quality (20)
                            - Projects Quality (15)
                            - Keyword Optimization (15)
                            
                            OUTPUT FORMAT (STRICT JSON ONLY):
                            
                            {
                              "score": number,
                              "summary": "short overall evaluation",
                            
                              "sections": {
                                "basic_info": {
                                  "score": number,
                                  "issues": [string],
                                  "advice": [string]
                                },
                                "structure": {
                                  "score": number,
                                  "issues": [string],
                                  "advice": [string]
                                },
                                "skills": {
                                  "score": number,
                                  "match_percentage": number,
                                  "missing_skills": [string],
                                  "issues": [string],
                                  "advice": [string]
                                },
                                "experience": {
                                  "score": number,
                                  "issues": [string],
                                  "advice": [string]
                                },
                                "projects": {
                                  "score": number,
                                  "issues": [string],
                                  "advice": [string]
                                },
                                "keywords": {
                                  "score": number,
                                  "missing_keywords": [string],
                                  "advice": [string]
                                }
                              }
                            }
                            
                            CONSTRAINTS:
                            - Be objective and critical, just like a real ATS system
                            - Focus on actionable feedback, not just identification of issues
                            - try to be specific in advice (e.g. "Add more quantifiable achievements in experience section" rather than "Experience section is weak")
                            - Do NOT hallucinate experience or skills
                            - Be strict and realistic (not generous scoring)
                            - If something is missing, explicitly flag it
                            - Advice must be actionable and specific
                            - Keep responses concise but informative
                            - Always return valid JSON without any extra commentary
                            - dont give \n in response give back content in proper jason which can be parsed by JSON.parse() without errors
                            
                               content : ${text}
                                
                            
                                
                                
                            """.formatted(text);
            
            JSONObject message = new JSONObject();
            message.put("role", "You are an advanced ATS (Applicant Tracking System) evaluator.");
            message.put("content", prompt);

            JSONArray messages = new JSONArray();
            messages.put(message);

            JSONObject body = new JSONObject();
            body.put("model", "meta/llama-4-maverick-17b-128e-instruct");
            body.put("messages", messages);
            body.put("temperature", 0.5);

            String json = body.toString();
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(invoke_url))
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());
            
              System.out.println("response");
            System.out.println(response.body());
            JSONObject apiResponse = new JSONObject(response.body());

                String content = apiResponse
                        .getJSONArray("choices")
                        .getJSONObject(0)
                        .getJSONObject("message")
                        .getString("content");
                 System.out.println(content);
                 
                 content = content
                .replace("```json", "")
                .replace("```", "")
                .trim();
                 
                 JSONObject atsData = new JSONObject(content);
                 return atsData;

        } catch (Exception e) {
             System.out.println("Error");
             e.printStackTrace();
             return null;
            
        }
    }
    
    
    
}

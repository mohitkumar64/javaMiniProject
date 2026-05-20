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

public class ApiCall {

    public String getResponse(String text) {

        try {

            String apiKey = "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";
           String invoke_url="https://integrate.api.nvidia.com/v1/chat/completions";

            String json = """
            {
               "model": "nvidia/nemotron-3-nano-omni-30b-a3b-reasoning",
              "messages": [
                {
                  "role": "user",
                  "content": "hello"
                }
              ],
              "temperature": 0.5
            }
            """;

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

        } catch (Exception e) {
             System.out.println("Error");
            e.printStackTrace();
        }
    }
}

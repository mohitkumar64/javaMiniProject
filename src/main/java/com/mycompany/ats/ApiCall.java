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
      String invoke_url = "https://integrate.api.nvidia.com/v1/chat/completions";

      // ===== SYSTEM MESSAGE: Sets the AI persona and behavior =====
      String systemPrompt = """
          You are a ruthlessly strict ATS (Applicant Tracking System) scoring engine.
          You evaluate resumes with the same cold, algorithmic precision as real ATS software.
          You do NOT give generous or encouraging scores. A mediocre resume gets a mediocre score.
          You MUST differentiate clearly between strong, average, and weak resumes.
          Typical score distribution: weak resumes 20-40, average 40-60, good 60-75, excellent 75-90, near-perfect 90+.
          A score above 80 should be RARE and only for truly exceptional resumes.
          You always respond with valid JSON only. No markdown, no commentary, no explanation outside JSON.
          """;

      // ===== USER MESSAGE: The actual evaluation task =====
      String userPrompt = """
          Analyze the following resume text and return a strict ATS evaluation as JSON.

          === RESUME TEXT START ===
          %s
          === RESUME TEXT END ===

          STEP 1 - IDENTIFY THE TARGET ROLE:
          Read the resume's Summary/Objective section and project descriptions carefully.
          Determine the candidate's target job role or domain (e.g., "Java Backend Developer", "Data Scientist", "Frontend Developer").
          ALL evaluation, missing skills, missing keywords, and advice MUST be anchored to this identified role.
          If no clear role is stated, infer the most likely role from the combination of skills + projects + education.

          STEP 2 - EVALUATE EACH SECTION WITH STRICT PENALTIES:

          BASIC INFO (max 10 points):
          - Full name present: +2, missing: 0
          - Valid email present: +2, missing: 0
          - Valid phone number present: +2, missing: 0
          - Location/city present: +1, missing: 0
          - LinkedIn OR GitHub OR portfolio link present: +3, missing: 0
          - Award ONLY the points for items that are clearly present

          STRUCTURE & FORMATTING (max 15 points):
          - Has clear Summary/Objective section: +3, missing: 0
          - Has Education section: +2, missing: 0
          - Has Skills section: +2, missing: 0
          - Has Experience section: +3, missing: 0
          - Has Projects section: +2, missing: 0
          - Uses bullet points (not paragraphs): +2, uses paragraphs: 0
          - Clean formatting (no excessive symbols/tables): +1, messy: 0

          SKILLS RELEVANCE (max 25 points):
          - CRITICAL: Only evaluate skills that are relevant to the TARGET ROLE identified in Step 1
          - How many core skills for the target role are listed? (0-10 points)
          - Are the listed skills actually demonstrated in projects or experience? (0-10 points)
          - Skill depth: Are they listing just names or showing proficiency levels? (0-5 points)
          - Penalty: -3 for each core skill listed but NOT backed by any project or experience
          - match_percentage = (skills demonstrated in projects or experience / total skills listed) * 100
          - missing_skills: ONLY list skills that are DIRECTLY REQUIRED for the target role AND are not present in the resume. Do NOT suggest unrelated or aspirational skills.

          EXPERIENCE QUALITY (max 20 points):
          - If NO experience section exists: score = 0, period.
          - Each experience entry with measurable impact (numbers, percentages, metrics): +4 per entry (max 12)
          - Uses strong action verbs (developed, implemented, optimized, led): +4
          - Vague descriptions like "worked on", "responsible for", "helped with": -3 per occurrence
          - No dates or unclear timeline: -4

          PROJECTS QUALITY (max 15 points):
          - If NO projects section exists: score = 0, period.
          - Each project that clearly states: problem, tech stack, outcome: +5 (max 10)
          - Projects align with listed skills: +3
          - Projects show real-world application (not just tutorials): +2
          - Penalty: Projects that are clearly basic tutorials (e.g., "Todo app", "Calculator"): -2 each

          KEYWORD OPTIMIZATION (max 15 points):
          - CRITICAL: Only check for keywords relevant to the TARGET ROLE identified in Step 1
          - Presence of role-specific technical keywords: +5
          - Presence of industry/domain keywords: +5
          - Presence of methodology keywords (Agile, CI/CD, TDD, etc.): +3
          - Presence of soft skill keywords (leadership, collaboration): +2
          - missing_keywords: ONLY list keywords that a recruiter for the TARGET ROLE would search for. Do NOT list generic or unrelated keywords.

          STEP 3 - CALCULATE TOTAL SCORE:
          total_score = basic_info + structure + skills + experience + projects + keywords
          The total MUST equal the sum of section scores. Double-check this.

          STEP 4 - WRITE THE SUMMARY:
          The summary must be 2-3 sentences that:
          - States the identified target role
          - Gives the biggest strength of the resume
          - Gives the single most critical improvement needed
          - Is brutally honest, not encouraging

          OUTPUT FORMAT (STRICT JSON ONLY, no other text):
          {
            "score": <number: sum of all section scores>,
            "summary": "<string: 2-3 sentence brutally honest evaluation>",
            "sections": {
              "basic_info": {
                "score": <number out of 10>,
                "issues": ["<specific issue 1>", "<specific issue 2>"],
                "advice": ["<actionable advice 1>"]
              },
              "structure": {
                "score": <number out of 15>,
                "issues": ["<specific issue>"],
                "advice": ["<actionable advice>"]
              },
              "skills": {
                "score": <number out of 25>,
                "match_percentage": <number: percentage of skills backed by projects/experience>,
                "missing_skills": ["<skill DIRECTLY required for target role>"],
                "issues": ["<specific issue>"],
                "advice": ["<actionable advice>"]
              },
              "experience": {
                "score": <number out of 20>,
                "issues": ["<specific issue>"],
                "advice": ["<actionable advice>"]
              },
              "projects": {
                "score": <number out of 15>,
                "issues": ["<specific issue>"],
                "advice": ["<actionable advice>"]
              },
              "keywords": {
                "score": <number out of 15>,
                "missing_keywords": ["<keyword a recruiter for THIS role would search>"],
                "advice": ["<actionable advice>"]
              }
            }
          }

          HARD RULES:
          - The total score MUST equal the sum of all 6 section scores. Verify before responding.
          - missing_skills MUST only contain skills directly relevant to the target role found in the resume summary or projects. Never suggest random trending skills.
          - missing_keywords MUST only contain keywords a recruiter hiring for the target role would actually search for.
          - Do NOT hallucinate skills, experience, or projects that are not in the resume.
          - Do NOT be generous. If a section is missing, its score is 0.
          - Each issue and advice string must be specific and actionable, not vague.
          - Return ONLY the JSON object. No markdown code fences. No extra text before or after.
          - The JSON must be parseable by a standard JSON parser with no errors.
          """
          .formatted(text);

      // ===== BUILD MESSAGES ARRAY WITH PROPER ROLES =====
      JSONObject systemMessage = new JSONObject();
      systemMessage.put("role", "system");
      systemMessage.put("content", systemPrompt);

      JSONObject userMessage = new JSONObject();
      userMessage.put("role", "user");
      userMessage.put("content", userPrompt);

      JSONArray messages = new JSONArray();
      messages.put(systemMessage);
      messages.put(userMessage);

      JSONObject body = new JSONObject();
      body.put("model", "meta/llama-4-maverick-17b-128e-instruct");
      body.put("messages", messages);
      body.put("temperature", 0.2);

      String json = body.toString();
      HttpClient client = HttpClient.newHttpClient();

      HttpRequest request = HttpRequest.newBuilder()
          .uri(new URI(invoke_url))
          .header("Authorization", "Bearer " + apiKey)
          .header("Content-Type", "application/json")
          .POST(HttpRequest.BodyPublishers.ofString(json))
          .build();

      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

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

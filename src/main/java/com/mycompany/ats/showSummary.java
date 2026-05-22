package com.mycompany.ats;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import org.json.*;

public class showSummary extends JPanel {

    app frame;
    JSONObject atsData;
    User user;

    public showSummary(app frame, JSONObject data, User user) {

        this.frame = frame;
        this.atsData = data;

        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(new Color(245, 245, 245));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JButton backButton = new JButton("← Back to Dashboard");

        backButton.setFont(new Font("SansSerif", Font.BOLD, 14));

        backButton.setFocusPainted(false);

        backButton.setBackground(new Color(33, 150, 243));

        backButton.setForeground(Color.WHITE);

        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        backButton.setBorder(
                BorderFactory.createEmptyBorder(10, 18, 10, 18));

        backButton.addActionListener(e -> {

            frame.showDashboard(user);

        });

        mainPanel.add(backButton);
        mainPanel.add(Box.createVerticalStrut(20));

        try {

            // ================= HEADER =================

            int score = atsData.getInt("score");
            String summary = atsData.getString("summary");

            JPanel headerPanel = createCard();

            JLabel scoreLabel = new JLabel("ATS Score: " + score + "/100");

            scoreLabel.setFont(new Font("SansSerif", Font.BOLD, 28));

            if (score >= 75) {
                scoreLabel.setForeground(new Color(46, 125, 50));
            } else if (score >= 50) {
                scoreLabel.setForeground(new Color(255, 140, 0));
            } else {
                scoreLabel.setForeground(Color.RED);
            }

            JTextArea summaryArea = createTextArea(summary);

            headerPanel.add(scoreLabel);
            headerPanel.add(Box.createVerticalStrut(15));
            headerPanel.add(summaryArea);

            mainPanel.add(headerPanel);
            mainPanel.add(Box.createVerticalStrut(20));

            // ================= SECTIONS =================

            JSONObject sections = atsData.getJSONObject("sections");

            String[] keys = {
                    "basic_info",
                    "structure",
                    "skills",
                    "experience",
                    "projects",
                    "keywords"
            };

            // Max scores for each section
            Map<String, Integer> maxScores = new HashMap<>();
            maxScores.put("basic_info", 10);
            maxScores.put("structure", 15);
            maxScores.put("skills", 25);
            maxScores.put("experience", 20);
            maxScores.put("projects", 15);
            maxScores.put("keywords", 15);

            for (String key : keys) {

                JSONObject section = sections.getJSONObject(key);

                JPanel card = createCard();

                // ===== TITLE =====

                JLabel title = new JLabel(
                        key.replace("_", " ").toUpperCase());

                title.setFont(new Font("SansSerif", Font.BOLD, 22));
                title.setForeground(new Color(33, 33, 33));

                leftAlign(title);

                card.add(title);
                card.add(Box.createVerticalStrut(10));

                // ===== SCORE =====

                int sectionScore = section.getInt("score");
                int maxScore = maxScores.getOrDefault(key, 0);

                JLabel scoreText = new JLabel(
                        "Section Score: " + sectionScore + " / " + maxScore);

                scoreText.setFont(new Font("SansSerif", Font.BOLD, 16));
                scoreText.setForeground(new Color(25, 118, 210));

                leftAlign(scoreText);

                card.add(scoreText);

                // ===== MATCH PERCENT =====

                if (section.has("match_percentage")) {

                    JLabel match = new JLabel(
                            "Skill Match: "
                                    + section.getInt("match_percentage")
                                    + "%");

                    match.setFont(new Font("SansSerif", Font.PLAIN, 15));
                    match.setForeground(new Color(56, 142, 60));

                    leftAlign(match);

                    card.add(Box.createVerticalStrut(10));
                    card.add(match);
                }

                // ===== ISSUES =====

                if (section.has("issues")) {

                    JSONArray issues = section.getJSONArray("issues");

                    card.add(Box.createVerticalStrut(15));

                    JLabel issueTitle = createHeading("Issues");
                    JTextArea issueArea = createTextArea(buildBulletText(issues));

                    card.add(issueTitle);
                    card.add(Box.createVerticalStrut(5));
                    card.add(issueArea);
                }

                // ===== ADVICE =====

                if (section.has("advice")) {

                    JSONArray advice = section.getJSONArray("advice");

                    card.add(Box.createVerticalStrut(15));

                    JLabel adviceTitle = createHeading("Advice");
                    JTextArea adviceArea = createTextArea(buildBulletText(advice));

                    card.add(adviceTitle);
                    card.add(Box.createVerticalStrut(5));
                    card.add(adviceArea);
                }

                // ===== MISSING SKILLS =====

                if (section.has("missing_skills")) {

                    JSONArray missing = section.getJSONArray("missing_skills");

                    card.add(Box.createVerticalStrut(15));

                    JLabel missingTitle = createHeading("Missing Skills");

                    JTextArea missingArea = createTextArea(buildBulletText(missing));

                    card.add(missingTitle);
                    card.add(Box.createVerticalStrut(5));
                    card.add(missingArea);
                }

                // ===== MISSING KEYWORDS =====

                if (section.has("missing_keywords")) {

                    JSONArray keywords = section.getJSONArray("missing_keywords");

                    card.add(Box.createVerticalStrut(15));

                    JLabel keywordTitle = createHeading("Missing Keywords");

                    JTextArea keywordArea = createTextArea(buildBulletText(keywords));

                    card.add(keywordTitle);
                    card.add(Box.createVerticalStrut(5));
                    card.add(keywordArea);
                }

                mainPanel.add(card);
                mainPanel.add(Box.createVerticalStrut(20));
            }

            JScrollPane scrollPane = new JScrollPane(mainPanel);

            scrollPane.setBorder(null);
            scrollPane.getViewport()
                    .setBackground(new Color(245, 245, 245));

            scrollPane.getVerticalScrollBar()
                    .setUnitIncrement(16);

            add(scrollPane, BorderLayout.CENTER);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= CARD =================

    private JPanel createCard() {

        JPanel panel = new JPanel();

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.setBackground(Color.WHITE);

        panel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(220, 220, 220)),
                        new EmptyBorder(18, 18, 18, 18)));

        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        return panel;
    }

    // ================= TEXT AREA =================

    private JTextArea createTextArea(String text) {

        JTextArea area = new JTextArea(text);

        area.setLineWrap(true);
        area.setWrapStyleWord(true);

        area.setEditable(false);

        area.setFont(new Font("SansSerif", Font.PLAIN, 14));

        area.setForeground(new Color(60, 60, 60));

        area.setOpaque(false);

        area.setAlignmentX(Component.LEFT_ALIGNMENT);

        area.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 120));

        return area;
    }

    // ================= HEADING =================

    private JLabel createHeading(String text) {

        JLabel label = new JLabel(text);

        label.setFont(new Font("SansSerif", Font.BOLD, 15));

        label.setForeground(new Color(33, 33, 33));

        leftAlign(label);

        return label;
    }

    // ================= BULLETS =================

    private String buildBulletText(JSONArray array) {

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < array.length(); i++) {

            sb.append("• ")
                    .append(array.getString(i))
                    .append("\n");
        }

        return sb.toString();
    }

    // ================= ALIGNMENT =================

    private void leftAlign(JComponent component) {
        component.setAlignmentX(Component.LEFT_ALIGNMENT);
    }
}
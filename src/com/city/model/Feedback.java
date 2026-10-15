package com.city.model;

import java.sql.Timestamp;

public class Feedback {

    private final int feedbackId;
    private final int residentId;
    private final String category;
    private final String message;
    private final Timestamp submittedAt;

    public Feedback(int feedbackId, int residentId, String category, String message, Timestamp submittedAt) {
        this.feedbackId = feedbackId;
        this.residentId = residentId;
        this.category = category;
        this.message = message;
        this.submittedAt = submittedAt;
    }

    public int getFeedbackId() {
        return feedbackId;
    }

    public int getResidentId() {
        return residentId;
    }

    public String getCategory() {
        return category;
    }

    public String getMessage() {
        return message;
    }

    public Timestamp getSubmittedAt() {
        return submittedAt;
    }
}

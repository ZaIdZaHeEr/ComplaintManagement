package com.zaid.complaintmanagement2.models;

import com.google.firebase.database.IgnoreExtraProperties;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@IgnoreExtraProperties
public class Complaint implements Serializable {
    private String id;
    private String title;
    private String description;
    private String category;
    private String status = "Pending";
    private String priority = "Medium";
    private long timestamp;
    private long lastUpdated;
    private String submittedBy;
    private String submittedByEmail;

    // 1. Empty Constructor (REQUIRED for Firebase)
    public Complaint() {}

    // 2. Main Constructor (Fixed to match your error exactly: 9 arguments)
    // Order: id, title, description, category, status, priority, TIMESTAMP, submittedBy, submittedByEmail
    public Complaint(String id, String title, String description, String category,
                     String status, String priority, long timestamp, String submittedBy, String submittedByEmail) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.category = category;
        this.status = status;
        this.priority = priority;
        this.timestamp = timestamp;
        this.submittedBy = submittedBy;
        this.submittedByEmail = submittedByEmail;

        // Auto-fill lastUpdated
        this.lastUpdated = timestamp;
    }

    // 3. Alternate Constructor (In case you use the 8-argument version elsewhere)
    public Complaint(String id, String title, String description, String category,
                     String status, String priority, String submittedByEmail, long timestamp) {
        this(id, title, description, category, status, priority, timestamp, submittedByEmail, submittedByEmail);
    }

    // Getters
    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getCategory() { return category; }
    public String getStatus() { return status; }
    public String getPriority() { return priority; }
    public long getTimestamp() { return timestamp; }
    public long getLastUpdated() { return lastUpdated; }
    public String getSubmittedBy() { return submittedBy; }
    public String getSubmittedByEmail() { return submittedByEmail; }

    // Setters
    public void setId(String id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setCategory(String category) { this.category = category; }
    public void setStatus(String status) { this.status = status; }
    public void setPriority(String priority) { this.priority = priority; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
    public void setLastUpdated(long lastUpdated) { this.lastUpdated = lastUpdated; }
    public void setSubmittedBy(String submittedBy) { this.submittedBy = submittedBy; }
    public void setSubmittedByEmail(String submittedByEmail) { this.submittedByEmail = submittedByEmail; }

    // Helper Methods
    public String getFormattedDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
        return sdf.format(new Date(timestamp));
    }
}
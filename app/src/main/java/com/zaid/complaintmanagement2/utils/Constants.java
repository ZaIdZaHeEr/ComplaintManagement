package com.zaid.complaintmanagement2.utils;

public class Constants {
    public static final String PREFS_NAME = "ComplaintManagementPrefs";
    public static final String KEY_REMEMBER_ME = "remember_me";
    public static final String KEY_USER_EMAIL = "user_email";
    public static final String KEY_USER_NAME = "user_name";
    public static final String KEY_IS_ADMIN = "is_admin";

    // Intent Extras
    public static final String EXTRA_COMPLAINT = "complaint";
    public static final String EXTRA_COMPLAINT_ID = "complaint_id";
    public static final String EXTRA_IS_EDIT_MODE = "is_edit_mode";

    // Categories
    public static final String[] CATEGORIES = {
            "Maintenance",
            "Technical",
            "Electrical",
            "Plumbing",
            "Cleaning",
            "Security",
            "Other"
    };

    // Priority Levels
    public static final String[] PRIORITIES = {
            "Low",
            "Medium",
            "High"
    };

    // Status
    public static final String[] STATUSES = {
            "Pending",
            "In Progress",
            "Resolved"
    };

    // Status Constants
    public static final String STATUS_PENDING = "Pending";
    public static final String STATUS_IN_PROGRESS = "In Progress";
    public static final String STATUS_RESOLVED = "Resolved";

    // Priority Constants
    public static final String PRIORITY_LOW = "Low";
    public static final String PRIORITY_MEDIUM = "Medium";
    public static final String PRIORITY_HIGH = "High";

    // Sort Options
    public static final int SORT_DATE_NEWEST = 0;
    public static final int SORT_DATE_OLDEST = 1;
    public static final int SORT_PRIORITY_HIGH_LOW = 2;
    public static final int SORT_PRIORITY_LOW_HIGH = 3;
}

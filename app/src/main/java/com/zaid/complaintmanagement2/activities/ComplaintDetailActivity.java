package com.zaid.complaintmanagement2.activities;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zaid.complaintmanagement2.R;
import com.zaid.complaintmanagement2.models.Complaint;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ComplaintDetailActivity extends AppCompatActivity {

    // Declare all the views from your new XML
    private TextView tvComplaintId, tvTitle, tvStatus, tvPriority, tvDescription, tvCategory, tvDate, tvSubmittedBy;
    private DatabaseReference mDatabase;
    private String complaintId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint_detail);

        // 1. Initialize Views
        tvComplaintId = findViewById(R.id.tvComplaintId);
        tvTitle = findViewById(R.id.tvTitle);
        tvStatus = findViewById(R.id.tvStatus);
        tvPriority = findViewById(R.id.tvPriority);
        tvDescription = findViewById(R.id.tvDescription);
        tvCategory = findViewById(R.id.tvCategory);
        tvDate = findViewById(R.id.tvDate);
        tvSubmittedBy = findViewById(R.id.tvSubmittedBy);

        // 2. Get the Complaint ID passed from the Home Screen
        if (getIntent().hasExtra("complaint_id")) {
            complaintId = getIntent().getStringExtra("complaint_id");
        } else {
            Toast.makeText(this, "Error: No Complaint ID found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // 3. Fetch Data from Firebase
        mDatabase = FirebaseDatabase.getInstance().getReference("complaints").child(complaintId);
        fetchComplaintDetails();
    }

    private void fetchComplaintDetails() {
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Complaint complaint = snapshot.getValue(Complaint.class);
                    if (complaint != null) {
                        updateUI(complaint);
                    }
                } else {
                    Toast.makeText(ComplaintDetailActivity.this, "Complaint not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ComplaintDetailActivity.this, "Database Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUI(Complaint complaint) {
        // Set Simple Text
        tvComplaintId.setText("#" + complaint.getId());
        tvTitle.setText(complaint.getTitle());
        tvDescription.setText(complaint.getDescription());
        tvCategory.setText(complaint.getCategory());
        tvSubmittedBy.setText(complaint.getSubmittedBy());
        tvStatus.setText(complaint.getStatus());
        tvPriority.setText(complaint.getPriority() + " Priority");

        // Set Formatted Date
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy • hh:mm a", Locale.getDefault());
            String dateString = sdf.format(new Date(complaint.getTimestamp()));
            tvDate.setText(dateString);
        } catch (Exception e) {
            tvDate.setText("Unknown Date");
        }

        // --- DYNAMIC COLORING (The Magic Part) ---

        // 1. Status Colors (Text & Background)
        int statusColor;
        int statusBgColor;

        if ("Resolved".equalsIgnoreCase(complaint.getStatus())) {
            statusColor = Color.parseColor("#4CAF50"); // Green Text
            statusBgColor = Color.parseColor("#E8F5E9"); // Light Green BG
        } else if ("Pending".equalsIgnoreCase(complaint.getStatus())) {
            statusColor = Color.parseColor("#FF9800"); // Orange Text
            statusBgColor = Color.parseColor("#FFF3E0"); // Light Orange BG
        } else {
            statusColor = Color.parseColor("#2196F3"); // Blue Text
            statusBgColor = Color.parseColor("#E3F2FD"); // Light Blue BG
        }

        tvStatus.setTextColor(statusColor);
        tvStatus.setBackgroundColor(statusBgColor); // Sets the badge background

        // 2. Priority Colors (Text Only for cleaner look)
        int priorityColor;
        if ("High".equalsIgnoreCase(complaint.getPriority())) {
            priorityColor = Color.parseColor("#D32F2F"); // Red
        } else if ("Medium".equalsIgnoreCase(complaint.getPriority())) {
            priorityColor = Color.parseColor("#FF9800"); // Orange
        } else {
            priorityColor = Color.parseColor("#4CAF50"); // Green
        }
        tvPriority.setTextColor(priorityColor);
    }
}
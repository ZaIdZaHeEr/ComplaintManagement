package com.zaid.complaintmanagement2.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.zaid.complaintmanagement2.R;
import com.zaid.complaintmanagement2.models.Complaint;

import java.util.UUID;

public class AddComplaintActivity extends AppCompatActivity {

    private EditText etTitle, etDescription;
    private Spinner spinnerCategory, spinnerPriority; // Added Priority Spinner
    private Button btnSubmit;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_complaint);

        // 1. Initialize Firebase
        mDatabase = FirebaseDatabase.getInstance().getReference("complaints");

        // 2. Initialize Views
        etTitle = findViewById(R.id.etTitle);
        etDescription = findViewById(R.id.etDescription);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        spinnerPriority = findViewById(R.id.spinnerPriority); // Bind new Spinner
        btnSubmit = findViewById(R.id.btnSubmit);

        // 3. Setup Category Dropdown
        String[] categories = {"Maintenance", "Electrical", "Plumbing", "Internet", "Housekeeping", "Other"};
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categories);
        spinnerCategory.setAdapter(categoryAdapter);

        // 4. Setup Priority Dropdown (NEW)
        String[] priorities = {"Low", "Medium", "High"};
        ArrayAdapter<String> priorityAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, priorities);
        spinnerPriority.setAdapter(priorityAdapter);
        spinnerPriority.setSelection(1); // Set "Medium" as default

        // 5. Submit Button Logic
        btnSubmit.setOnClickListener(v -> submitComplaint());
    }

    private void submitComplaint() {
        String title = etTitle.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        String category = spinnerCategory.getSelectedItem().toString();
        String priority = spinnerPriority.getSelectedItem().toString(); // Get selected priority

        // Validation
        if (TextUtils.isEmpty(title)) {
            etTitle.setError("Title is required");
            return;
        }
        if (TextUtils.isEmpty(description)) {
            etDescription.setError("Description is required");
            return;
        }

        // Check Auth
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "You must be logged in!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create Complaint Object
        String complaintId = "CMP-" + (System.currentTimeMillis() % 10000); // Simple ID generation
        long timestamp = System.currentTimeMillis();
        String status = "Pending";
        String userEmail = currentUser.getEmail();

        Complaint complaint = new Complaint(
                complaintId,
                title,
                description,
                category,
                status,
                priority, // Pass the selected priority here
                userEmail,
                timestamp
        );

        // Save to Firebase
        mDatabase.child(complaintId).setValue(complaint)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(AddComplaintActivity.this, "Complaint Submitted Successfully!", Toast.LENGTH_SHORT).show();
                        finish(); // Close activity and go back
                    } else {
                        Toast.makeText(AddComplaintActivity.this, "Failed to submit: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
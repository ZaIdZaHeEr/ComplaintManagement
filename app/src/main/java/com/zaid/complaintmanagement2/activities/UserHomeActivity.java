package com.zaid.complaintmanagement2.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.zaid.complaintmanagement2.R;
import com.zaid.complaintmanagement2.adapters.ComplaintAdapter;
import com.zaid.complaintmanagement2.models.Complaint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UserHomeActivity extends AppCompatActivity {

    private ListView listView;
    private ComplaintAdapter adapter;
    private List<Complaint> complaints = new ArrayList<>();
    private Button btnAddComplaint;
    private ImageView btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        // 1. Initialize Views
        listView = findViewById(R.id.listViewComplaints);
        btnAddComplaint = findViewById(R.id.btnAddComplaint);
        btnLogout = findViewById(R.id.btnLogout);

        // 2. Setup Logout
        if (btnLogout != null) {
            btnLogout.setOnClickListener(v -> {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(UserHomeActivity.this, LoginActivity.class));
                finishAffinity();
            });
        }

        // 3. Setup Adapter
        adapter = new ComplaintAdapter(this, complaints);
        if (listView != null) {
            listView.setAdapter(adapter);
        }

        // 4. Load Data
        loadComplaints();

        // 5. SHORT CLICK: Open Details
        if (listView != null) {
            listView.setOnItemClickListener((parent, view, position, id) -> {
                if (position < complaints.size()) {
                    Complaint clicked = complaints.get(position);
                    Intent intent = new Intent(UserHomeActivity.this, ComplaintDetailActivity.class);
                    intent.putExtra("complaint_id", clicked.getId());
                    startActivity(intent);
                }
            });
        }

        // 6. LONG CLICK: Delete (Only if Pending)
        if (listView != null) {
            listView.setOnItemLongClickListener((parent, view, position, id) -> {
                Complaint clicked = complaints.get(position);
                handleLongClick(clicked);
                return true; // True means we handled the click
            });
        }

        // 7. Add Complaint Button
        if (btnAddComplaint != null) {
            btnAddComplaint.setOnClickListener(v ->
                    startActivity(new Intent(UserHomeActivity.this, AddComplaintActivity.class))
            );
        }
    }

    // Logic to check status before allowing delete
    private void handleLongClick(Complaint complaint) {
        String status = complaint.getStatus();

        if ("Pending".equalsIgnoreCase(status)) {
            // Allow Delete
            confirmDelete(complaint.getId());
        } else {
            // Deny Delete
            Toast.makeText(this, "⚠️ Cannot delete! This complaint is already " + status + ".", Toast.LENGTH_LONG).show();
        }
    }

    private void confirmDelete(String complaintId) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Complaint")
                .setMessage("Are you sure you want to delete this pending complaint?")
                .setPositiveButton("Delete", (dialog, which) -> deleteComplaint(complaintId))
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void deleteComplaint(String complaintId) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("complaints").child(complaintId);
        ref.removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(UserHomeActivity.this, "Complaint Deleted Successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(UserHomeActivity.this, "Failed to delete", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadComplaints() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Toast.makeText(UserHomeActivity.this, "Error: No user logged in", Toast.LENGTH_LONG).show();
            return;
        }

        String userEmail = user.getEmail();
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("complaints");
        Query userQuery = databaseRef.orderByChild("submittedByEmail").equalTo(userEmail);

        userQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                complaints.clear();

                for (DataSnapshot data : snapshot.getChildren()) {
                    Complaint complaint = data.getValue(Complaint.class);
                    if (complaint != null) {
                        complaints.add(complaint);
                    }
                }
                Collections.reverse(complaints);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(UserHomeActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadComplaints();
    }
}
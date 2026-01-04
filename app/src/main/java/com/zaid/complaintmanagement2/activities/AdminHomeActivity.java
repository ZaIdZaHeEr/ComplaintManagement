package com.zaid.complaintmanagement2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zaid.complaintmanagement2.R;
import com.zaid.complaintmanagement2.adapters.ComplaintAdapter;
import com.zaid.complaintmanagement2.models.Complaint;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AdminHomeActivity extends AppCompatActivity {

    private ListView listView;
    private ComplaintAdapter adapter;
    private List<Complaint> complaintList = new ArrayList<>();
    private TextView tvTitle;
    private Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        // 1. Initialize Views
        tvTitle = findViewById(R.id.tvTitle);
        btnLogout = findViewById(R.id.btnLogout);
        listView = findViewById(R.id.listViewComplaints);

        // 2. Setup Header
        if (tvTitle != null) tvTitle.setText("Admin Panel");

        // 3. Setup Logout
        if (btnLogout != null) {
            btnLogout.setOnClickListener(v -> {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(AdminHomeActivity.this, LoginActivity.class));
                finishAffinity();
                Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();
            });
        }

        // 4. Setup Adapter
        adapter = new ComplaintAdapter(this, complaintList);
        if (listView != null) {
            listView.setAdapter(adapter);
        }

        // 5. Load Data
        loadAllComplaints();

        // 6. CLICK LISTENERS

        // A. Short Click -> Open Details
        if (listView != null) {
            listView.setOnItemClickListener((parent, view, position, id) -> {
                Complaint clickedComplaint = complaintList.get(position);
                Intent intent = new Intent(AdminHomeActivity.this, ComplaintDetailActivity.class);
                intent.putExtra("complaint_id", clickedComplaint.getId());
                startActivity(intent);
            });
        }

        // B. Long Click -> Show Enhanced Action Menu
        if (listView != null) {
            listView.setOnItemLongClickListener((parent, view, position, id) -> {
                showActionMenu(view, position);
                return true; // True means we handled the click
            });
        }
    }

    private void loadAllComplaints() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("complaints");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                complaintList.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Complaint complaint = data.getValue(Complaint.class);
                    if (complaint != null) {
                        complaintList.add(complaint);
                    }
                }

                // --- SORTING LOGIC ---
                // Sort by Timestamp Descending (Newest time -> Biggest number -> Top of list)
                Collections.sort(complaintList, new Comparator<Complaint>() {
                    @Override
                    public int compare(Complaint c1, Complaint c2) {
                        return Long.compare(c2.getTimestamp(), c1.getTimestamp());
                    }
                });

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(AdminHomeActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showActionMenu(View view, int position) {
        Complaint selectedComplaint = complaintList.get(position);

        PopupMenu popup = new PopupMenu(this, view);
        popup.getMenuInflater().inflate(R.menu.admin_status_menu, popup.getMenu());

        // Force Icons to Show
        try {
            Field[] fields = popup.getClass().getDeclaredFields();
            for (Field field : fields) {
                if ("mPopup".equals(field.getName())) {
                    field.setAccessible(true);
                    Object menuPopupHelper = field.get(popup);
                    Class<?> classPopupHelper = Class.forName(menuPopupHelper.getClass().getName());
                    Method setForceShowIcon = classPopupHelper.getMethod("setForceShowIcon", boolean.class);
                    setForceShowIcon.invoke(menuPopupHelper, true);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        popup.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();

            if (id == R.id.menu_pending) {
                updateComplaintStatus(selectedComplaint.getId(), "Pending");
            } else if (id == R.id.menu_in_progress) {
                updateComplaintStatus(selectedComplaint.getId(), "In Progress");
            } else if (id == R.id.menu_resolved) {
                updateComplaintStatus(selectedComplaint.getId(), "Resolved");
            } else if (id == R.id.menu_delete) {
                confirmDelete(selectedComplaint.getId());
            }
            return true;
        });

        popup.show();
    }

    private void confirmDelete(String complaintId) {
        // Using standard Android Alert Dialog
        new AlertDialog.Builder(this)
                .setTitle("Delete Complaint")
                .setMessage("Are you sure you want to delete this complaint? This cannot be undone.")
                .setPositiveButton("Delete", (dialog, which) -> deleteComplaint(complaintId))
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void deleteComplaint(String complaintId) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("complaints").child(complaintId);
        ref.removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(AdminHomeActivity.this, "Complaint Deleted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(AdminHomeActivity.this, "Failed to delete", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateComplaintStatus(String complaintId, String newStatus) {
        DatabaseReference complaintRef = FirebaseDatabase.getInstance().getReference("complaints").child(complaintId);
        complaintRef.child("status").setValue(newStatus).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(AdminHomeActivity.this, "Status updated to: " + newStatus, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(AdminHomeActivity.this, "Update failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
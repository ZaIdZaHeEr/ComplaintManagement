package com.zaid.complaintmanagement2.managers;

import android.util.Log;
import androidx.annotation.NonNull;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zaid.complaintmanagement2.models.Complaint;
import java.util.ArrayList;
import java.util.List;

public class ComplaintManager {
    private static ComplaintManager instance;
    private DatabaseReference databaseRef;

    public interface ComplaintsCallback {
        void onComplaintsLoaded(List<Complaint> complaints);
    }

    private ComplaintManager() {
        databaseRef = FirebaseDatabase.getInstance().getReference("complaints");
    }

    public static ComplaintManager getInstance() {
        if (instance == null) {
            instance = new ComplaintManager();
        }
        return instance;
    }

    public String generateComplaintId() {
        return "CMP-" + String.format("%04d", (int) (Math.random() * 10000));
    }

    // OLD (long IDs):
    public void addComplaint(Complaint complaint) {
        String complaintId = generateComplaintId();  // CMP-4847
        complaint.setId(complaintId);
        databaseRef.child(complaintId).setValue(complaint)
                .addOnSuccessListener(aVoid -> Log.d("ComplaintManager", "Complaint added: " + complaintId))
                .addOnFailureListener(e -> Log.e("ComplaintManager", "Error adding complaint", e));
    }



    public void getAllComplaints(ComplaintsCallback callback) {
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Complaint> complaints = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Complaint complaint = dataSnapshot.getValue(Complaint.class);
                    if (complaint != null) {
                        complaints.add(complaint);
                    }
                }
                callback.onComplaintsLoaded(complaints);
                Log.d("ComplaintManager", "Firebase found: " + complaints.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("ComplaintManager", "Permission denied: " + error.getMessage());
            }
        });
    }

    public void getUserComplaints(String userEmail, ComplaintsCallback callback) {
        databaseRef.orderByChild("submittedByEmail").equalTo(userEmail)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<Complaint> complaints = new ArrayList<>();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Complaint complaint = dataSnapshot.getValue(Complaint.class);
                            if (complaint != null) {
                                complaints.add(complaint);
                            }
                        }
                        callback.onComplaintsLoaded(complaints);
                        Log.d("ComplaintManager", "User complaints found: " + complaints.size());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("ComplaintManager", "User query error: " + error.getMessage());
                    }
                });
    }

    public void getComplaintById(String complaintId, ValueEventListener listener) {
        databaseRef.child(complaintId).addListenerForSingleValueEvent(listener);
    }
}

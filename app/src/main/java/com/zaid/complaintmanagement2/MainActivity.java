package com.zaid.complaintmanagement2;

import android.os.Bundle;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.zaid.complaintmanagement2.managers.ComplaintManager;
import com.zaid.complaintmanagement2.models.Complaint;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 🔥 FIREBASE TEST - Saves complaint to cloud!
        testFirebaseConnection();
    }

    private void testFirebaseConnection() {
        Complaint testComplaint = new Complaint(
                "TEST-001",
                "Firebase Working!",
                "This complaint saved to Firebase cloud database!",
                "Technical",
                "Pending",
                "High",
                System.currentTimeMillis(),
                "Test User",
                "test@zaid.com"
        );

        ComplaintManager.getInstance().addComplaint(testComplaint);

        Toast.makeText(this, "Check Firebase Console for TEST-001!", Toast.LENGTH_LONG).show();
    }
}

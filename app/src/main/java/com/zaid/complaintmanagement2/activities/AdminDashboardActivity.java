package com.zaid.complaintmanagement2.activities;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.zaid.complaintmanagement2.R;

public class AdminDashboardActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        TextView tvGreeting = findViewById(R.id.tvAdminGreeting);
        tvGreeting.setText("Welcome, Admin!\nThis is your dashboard.");
    }
}

package com.zaid.complaintmanagement2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.UnderlineSpan;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.zaid.complaintmanagement2.R;

public class AdminLoginActivity extends AppCompatActivity {

    private EditText etAdminEmail, etAdminPassword;
    private Button btnAdminLogin;
    private TextView tvBackToUserLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        etAdminEmail = findViewById(R.id.etAdminEmail);
        etAdminPassword = findViewById(R.id.etAdminPassword);
        btnAdminLogin = findViewById(R.id.btnAdminLogin);
        tvBackToUserLogin = findViewById(R.id.tvBackToUserLogin);

        // Underline for back link
        SpannableString underlineBack = new SpannableString(tvBackToUserLogin.getText());
        underlineBack.setSpan(new UnderlineSpan(), 0, underlineBack.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvBackToUserLogin.setText(underlineBack);
        tvBackToUserLogin.setMovementMethod(LinkMovementMethod.getInstance());

        btnAdminLogin.setOnClickListener(v -> attemptAdminLogin());
        tvBackToUserLogin.setOnClickListener(v -> {
            startActivity(new Intent(AdminLoginActivity.this, LoginActivity.class));
            finish();
        });
    }

    private void attemptAdminLogin() {
        String email = etAdminEmail.getText().toString().trim();
        String password = etAdminPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter admin email and password", Toast.LENGTH_SHORT).show();
            return;
        }

        // ✅ HARDCODED ADMIN ACCESS (No Firebase Auth issues)
        if (email.equals("admin@complaints.com") && password.equals("admin123")) {
            Toast.makeText(this, "✅ Admin login successful!", Toast.LENGTH_SHORT).show();
            // Pass ADMIN flag
            Intent intent = new Intent(AdminLoginActivity.this, AdminHomeActivity.class);
            intent.putExtra("IS_ADMIN", true);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "❌ Invalid admin credentials!", Toast.LENGTH_LONG).show();
        }
    }
}

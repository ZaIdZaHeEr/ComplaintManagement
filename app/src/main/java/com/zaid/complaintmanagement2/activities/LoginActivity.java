package com.zaid.complaintmanagement2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseUser;
import com.zaid.complaintmanagement2.R;
import com.zaid.complaintmanagement2.managers.AuthManager;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin;
    private TextView tvSignup, tvForgotPassword, tvAdminLogin;
    private AuthManager authManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();
        authManager = AuthManager.getInstance();

        btnLogin.setOnClickListener(v -> attemptLogin());
        tvSignup.setOnClickListener(v -> startActivity(new Intent(this, SignupActivity.class)));
        tvForgotPassword.setOnClickListener(v -> startActivity(new Intent(this, ForgotPasswordActivity.class)));
        tvAdminLogin.setOnClickListener(v -> startActivity(new Intent(this, AdminLoginActivity.class)));
    }

    private void initViews() {
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvSignup = findViewById(R.id.tvSignup);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        tvAdminLogin = findViewById(R.id.tvAdminLogin);
    }

    private void attemptLogin() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        btnLogin.setEnabled(false);
        authManager.login(email, password, new AuthManager.LoginCallback() {
            @Override
            public void onSuccess(FirebaseUser user) {
                btnLogin.setEnabled(true);

                if (user != null && user.getEmail() != null) {
                    String userEmail = user.getEmail();

                    // ADMIN CHECK
                    if (userEmail.equals("admin@complaints.com")) {
                        Toast.makeText(LoginActivity.this, "Admin login successful!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, AdminHomeActivity.class));
                    } else {
                        // REGULAR USER
                        Toast.makeText(LoginActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, UserHomeActivity.class);
                        intent.putExtra("USER_EMAIL", userEmail);
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Login successful but no email found", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, UserHomeActivity.class));
                }
                finish();
            }

            @Override
            public void onFailure(String error) {
                btnLogin.setEnabled(true);
                Toast.makeText(LoginActivity.this, "Login failed: " + error, Toast.LENGTH_LONG).show();
            }
        });
    }
}

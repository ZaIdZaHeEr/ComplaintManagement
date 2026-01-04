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

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText etEmail;
    private Button btnSendReset;
    private TextView tvBackToLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        etEmail = findViewById(R.id.etEmail);
        btnSendReset = findViewById(R.id.btnSendResetLink);
        tvBackToLogin = findViewById(R.id.tvBackToLogin);

        // Underline
        SpannableString underlineBack = new SpannableString(tvBackToLogin.getText());
        underlineBack.setSpan(new UnderlineSpan(), 0, underlineBack.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvBackToLogin.setText(underlineBack);
        tvBackToLogin.setMovementMethod(LinkMovementMethod.getInstance());

        // Navigation back to login
        tvBackToLogin.setOnClickListener(v -> {
            startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
            finish();
        });

        // For demo: Just show a toast. (No real reset logic)
        btnSendReset.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            if (TextUtils.isEmpty(email)) {
                Toast.makeText(this, "Please enter your email address", Toast.LENGTH_SHORT).show();
                return;
            }
            Toast.makeText(this, "Password reset link sent (mock UI)!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
            finish();
        });
    }
}

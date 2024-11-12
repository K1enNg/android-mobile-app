package com.example.homeal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ResetPasswordActivity extends AppCompatActivity {

    EditText etResetEmail, etPassword, etConfirmPassword;
    Button btnSubmitReset;
    TextView BackToLogin;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reset_password);

        etResetEmail = findViewById(R.id.etResetEmail);
        btnSubmitReset = findViewById(R.id.btnSubmitReset);
        BackToLogin = findViewById(R.id.BackToLogin);

        auth = FirebaseAuth.getInstance();

        btnSubmitReset.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  resetPassword();
              }
        });

        BackToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void resetPassword(){
        String email = etResetEmail.getText().toString().trim();
        if (email.isEmpty()) {
            Toast.makeText(ResetPasswordActivity.this, "Please fill email field", Toast.LENGTH_SHORT).show();
            return;
        }

        auth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                Toast.makeText(this, "Reset link sent to your email.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to send reset email.", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
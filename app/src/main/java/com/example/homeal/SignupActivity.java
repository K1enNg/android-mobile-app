package com.example.homeal;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.HashMap;

public class SignupActivity extends AppCompatActivity {
    EditText confirmPassword, name, email, password;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);

        name = findViewById(R.id.etSignupName);
        email = findViewById(R.id.etSignupEmail);
        password = findViewById(R.id.etSignupPassword);
        confirmPassword = findViewById(R.id.etSignupConfirmPassword);
        mAuth = FirebaseAuth.getInstance();

        Button registerButton = findViewById(R.id.btnSignup);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

    }

    private void registerUser() {

        String userName = name.getText().toString().trim();
        String userEmail = email.getText().toString().trim();
        String userPassword = password.getText().toString().trim();
        String userConfirmPassword = confirmPassword.getText().toString().trim();


        if (userName.isEmpty()) {
            name.setError("Name is required");
            name.requestFocus();
            return;
        }

        if (userEmail.isEmpty()) {
            email.setError("Email is required");
            email.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
            email.setError("Please enter a valid email");
            email.requestFocus();
            return;
        }

        if (userPassword.isEmpty()) {
            password.setError("Password is required");
            password.requestFocus();
            return;
        }

        if (userPassword.length() < 6) {
            password.setError("Password must be at least 6 characters");
            password.requestFocus();
            return;
        }

        if (userConfirmPassword.isEmpty()) {
            confirmPassword.setError("Confirm Password is required");
            confirmPassword.requestFocus();
            return;
        }

        if (!userPassword.equals(userConfirmPassword)) {
            confirmPassword.setError("Passwords do not match");
            confirmPassword.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {

                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {

                            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(user.getUid());


                            HashMap<String, Object> userData = new HashMap<>();
                            userData.put("userType", "buyer");
                            userData.put("store", null);


                            userRef.setValue(userData).addOnCompleteListener(saveTask -> {
                                if (saveTask.isSuccessful()) {
                                    Toast.makeText(SignupActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();

                                    startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                                    finish();
                                } else {
                                    Toast.makeText(SignupActivity.this, "Failed to save additional data", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    } else {

                        Toast.makeText(SignupActivity.this, "Registration Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });




    }


}
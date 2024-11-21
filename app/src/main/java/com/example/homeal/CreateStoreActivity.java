package com.example.homeal;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class CreateStoreActivity extends AppCompatActivity {

    EditText etStoreName, etStoreAddress, etStoreDescription, etStoreContact;
    Button btnSubmitStore;
    DatabaseReference database;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_store);

        etStoreName = findViewById(R.id.etStoreName);
        etStoreAddress = findViewById(R.id.etStoreAddress);
        etStoreDescription = findViewById(R.id.etStoreDescription);
        etStoreContact = findViewById(R.id.etStoreContact);
        btnSubmitStore = findViewById(R.id.btnSubmitStore);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference();

        btnSubmitStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitStore();
            }
        });
    }

    private void submitStore() {
        String storeName = etStoreName.getText().toString().trim();
        String storeAddress = etStoreAddress.getText().toString().trim();
        String storeDescription = etStoreDescription.getText().toString().trim();
        String storeContact = etStoreContact.getText().toString().trim();

        if (storeName.isEmpty() || storeAddress.isEmpty() || storeDescription.isEmpty() || storeContact.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }



        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            String storeId = database.child("stores").push().getKey();

            if (storeId != null) {
                HashMap<String, Object> storeData = new HashMap<>();
                storeData.put("storeName", storeName);
                storeData.put("storeAddress", storeAddress);
                storeData.put("storeDescription", storeDescription);
                storeData.put("storeContact", storeContact);

                database.child("stores").child(storeId).setValue(storeData).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        //update user with store id
                        database.child("users").child(userId).child("store").setValue(storeId).addOnCompleteListener(updateTask -> {
                            if (updateTask.isSuccessful()) {
                                Toast.makeText(this, "Store created successfully", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(this, "Failed to update user", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Toast.makeText(this, "Failed to create store", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }
}
package com.example.homeal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class StoreInfo extends AppCompatActivity {

    ImageView ivBack;
    TextView tvStoreName;
    EditText etStoreName, etStoreAddress, etStoreDescription, etStoreContact;
    Button btnUpdateStore;
    DatabaseReference database;
    String storeId ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_store_info);

        ivBack = findViewById(R.id.ivBack);
        tvStoreName = findViewById(R.id.tvStoreName);
        etStoreName = findViewById(R.id.etStoreName);
        etStoreAddress = findViewById(R.id.etStoreAddress);
        etStoreDescription = findViewById(R.id.etStoreDescription);
        etStoreContact = findViewById(R.id.etStoreContact);
        btnUpdateStore = findViewById(R.id.btnUpdateStore);
        storeId = getIntent().getStringExtra("STORE");

        database = FirebaseDatabase.getInstance().getReference("stores");

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnUpdateStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateStore();
            }
        });

        updateFields();
    }

    private void updateFields(){
        database.child(storeId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String storeName = snapshot.child("storeName").getValue(String.class);
                    String storeAddress = snapshot.child("storeAddress").getValue(String.class);
                    String storeDescription = snapshot.child("storeDescription").getValue(String.class);
                    String storeContact = snapshot.child("storeContact").getValue(String.class);

                    tvStoreName.setText(storeName);
                    etStoreName.setText(storeName);
                    etStoreAddress.setText(storeAddress);
                    etStoreDescription.setText(storeDescription);
                    etStoreContact.setText(storeContact);
                }
                else {
                    Toast.makeText(StoreInfo.this, "Store not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(StoreInfo.this, "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateStore(){
        String storeName = etStoreName.getText().toString().trim();
        String storeAddress = etStoreAddress.getText().toString().trim();
        String storeDescription = etStoreDescription.getText().toString().trim();
        String storeContact = etStoreContact.getText().toString().trim();

        if (storeName.isEmpty() || storeAddress.isEmpty() || storeDescription.isEmpty() || storeContact.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        HashMap<String, Object> storeData = new HashMap<>();
        storeData.put("storeName", storeName);
        storeData.put("storeAddress", storeAddress);
        storeData.put("storeDescription", storeDescription);
        storeData.put("storeContact", storeContact);

        database.child(storeId).setValue(storeData).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "Store updated successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed to create store", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
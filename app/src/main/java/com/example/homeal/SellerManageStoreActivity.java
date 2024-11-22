package com.example.homeal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SellerManageStoreActivity extends AppCompatActivity {

    TextView tvStoreName;
    Button btnManageInfo, btnAddProduct;
    RecyclerView productList;
    ProductManageStoreAdapter adapter;
    FirebaseAuth auth;
    DatabaseReference database;
    String storeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_seller_manage_store);

        tvStoreName = findViewById(R.id.tvStoreName);
        btnManageInfo = findViewById(R.id.btnManageInfo);
        btnAddProduct = findViewById(R.id.btnAddProduct);
        productList = findViewById(R.id.productList);
        storeId = getIntent().getStringExtra("STORE");

        adapter = new ProductManageStoreAdapter();
        productList.setLayoutManager(new LinearLayoutManager(this));
        productList.setAdapter(adapter);

        // Initialize Firebase Authentication and database reference
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference("stores");

        // Set click listeners for the buttons
        btnManageInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SellerManageStoreActivity.this, StoreInfo.class);
                intent.putExtra("STORE", storeId);
                startActivity(intent);
            }
        });

        btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SellerManageStoreActivity.this, AddProductActivity.class);
                startActivity(intent);
            }
        });

        updateRecycle();
    }

    private void updateRecycle(){


        if (storeId != null) {
            database.child(storeId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String storeName = snapshot.child("storeName").getValue(String.class);
                        tvStoreName.setText(storeName);
                    } else {
                        Toast.makeText(SellerManageStoreActivity.this, "Store not found", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(SellerManageStoreActivity.this, "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        adapter.notifyDataSetChanged();
    }

}
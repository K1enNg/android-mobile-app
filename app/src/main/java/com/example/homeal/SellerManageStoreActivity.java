package com.example.homeal;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SellerManageStoreActivity extends AppCompatActivity {

    TextView tvStoreName;
    Button btnManageInfo, btnAddProduct;
    ListView listView;
    ProductManageStoreAdapter adapter;
    List<Product> productList;
    FirebaseAuth auth;
    DatabaseReference database;
    String storeId;

    private ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_seller_manage_store);

        tvStoreName = findViewById(R.id.tvStoreName);
        btnManageInfo = findViewById(R.id.btnManageInfo);
        btnAddProduct = findViewById(R.id.btnAddProduct);
        listView = findViewById(R.id.listView);
        storeId = getIntent().getStringExtra("STORE");

        productList = new ArrayList<>();

        adapter = new ProductManageStoreAdapter(this,productList);
        listView.setAdapter(adapter);

        // Initialize Firebase Authentication and database reference
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference();

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
                intent.putExtra("STORE", storeId);
                startActivity(intent);
            }
        });

        updatePage();
    }

    private void updatePage(){
        if (storeId != null) {
            database.child("stores").child(storeId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String storeName = snapshot.child("storeName").getValue(String.class);
                        tvStoreName.setText(storeName);

                        for (DataSnapshot productSnapshot : snapshot.child("products").getChildren()) {
                            String productId = productSnapshot.getKey();
                            if (productId != null){
                                updateList(productId);
                            }
                        }
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
    }

    private void updateList(String productId) {
        database.child("products").child(productId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    String name = snapshot.child("name").getValue(String.class);
                    double price = snapshot.child("price").getValue(Double.class);
                    String description = snapshot.child("description").getValue(String.class);
                    String id = snapshot.getKey();

                    Product product = new Product(name, price, description, id, storeId);

                    productList.add(product);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SellerManageStoreActivity.this, "Error updating list: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
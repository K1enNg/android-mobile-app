package com.example.homeal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BuyerStoreViewActivity extends AppCompatActivity {

    ImageView ivStoreImage;
    TextView tvStoreName;
    ListView listView;
    Button btnChekout, btnLeaveStore;
    List<Product> productList;
    ProductBuyerViewAdapter adapter;
    DatabaseReference database;
    FirebaseAuth auth;
    String storeId;
    String storeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_buyer_store_view);

        ivStoreImage = findViewById(R.id.ivStoreImage);
        tvStoreName = findViewById(R.id.tvStoreName);
        listView = findViewById(R.id.listView);
        btnChekout = findViewById(R.id.btnCheckout);
        btnLeaveStore = findViewById(R.id.btnLeaveStore);
        storeId = getIntent().getStringExtra("STORE");

        // Initialize Firebase database reference
        database = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();


        productList = new ArrayList<>();
        adapter = new ProductBuyerViewAdapter(this, productList, auth.getUid(), database);
        listView.setAdapter(adapter);

        // Update the page content based on the store ID
        updatePage();

        btnChekout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BuyerStoreViewActivity.this, CheckoutActivity.class);
                intent.putExtra("STORE_NAME", storeName);
                startActivity(intent);
            }
        });

        btnLeaveStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void updatePage(){
        if (storeId != null) {
            database.child("stores").child(storeId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        storeName = snapshot.child("storeName").getValue(String.class);
                        tvStoreName.setText(storeName);

                        for (DataSnapshot productSnapshot : snapshot.child("products").getChildren()) {
                            String productId = productSnapshot.getKey();
                            if (productId != null){
                                updateList(productId);
                            }
                        }
                    } else {
                        Toast.makeText(BuyerStoreViewActivity.this, "Store not found", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(BuyerStoreViewActivity.this, "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(BuyerStoreViewActivity.this, "Error updating list: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
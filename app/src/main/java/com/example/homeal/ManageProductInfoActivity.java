package com.example.homeal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

public class ManageProductInfoActivity extends AppCompatActivity {

    ImageView ivBack;
    EditText etProductName, etProductPrice, etProductDescription;
    Button btnUpdateProduct;
    DatabaseReference database;
    String productId;
    String storeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_manage_product_info);

        ivBack = findViewById(R.id.ivBack);
        etProductName = findViewById(R.id.etProductName);
        etProductPrice = findViewById(R.id.etProductPrice);
        etProductDescription = findViewById(R.id.etProductDescription);
        btnUpdateProduct = findViewById(R.id.btnUpdateProduct);
        productId = getIntent().getStringExtra("PRODUCT");
        storeId = getIntent().getStringExtra("STORE");

        database = FirebaseDatabase.getInstance().getReference("products");

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnUpdateProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProduct();
            }
        });

        updateFields();

    }

    private void updateFields(){
        database.child(productId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String productName = snapshot.child("name").getValue(String.class);
                    double productPrice = snapshot.child("price").getValue(Double.class);
                    String productDescription = snapshot.child("description").getValue(String.class);

                    etProductName.setText(productName);
                    etProductPrice.setText(String.valueOf(productPrice));
                    etProductDescription.setText(productDescription);
                }
                else {
                    Toast.makeText(ManageProductInfoActivity.this, "Product not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ManageProductInfoActivity.this, "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateProduct(){
        String productName = etProductName.getText().toString().trim();
        double productPrice = Double.parseDouble(etProductPrice.getText().toString().trim());
        String productDescription = etProductDescription.getText().toString().trim();

        if (productName.isEmpty() || productPrice <= 0 || productDescription.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Product product = new Product(productName, productPrice, productDescription);

        database.child(productId).setValue(product).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "Product updated successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ManageProductInfoActivity.this, SellerManageStoreActivity.class);
                intent.putExtra("STORE", storeId);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Failed to update product", Toast.LENGTH_SHORT).show();

            }
        });
    }
}
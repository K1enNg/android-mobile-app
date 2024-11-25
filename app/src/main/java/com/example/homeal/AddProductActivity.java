package com.example.homeal;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddProductActivity extends AppCompatActivity {

    private ImageView ivBack;
    private EditText etProductName, etProductPrice, etProductDescription;
    private Button btnAddProduct;
    private DatabaseReference database;
    private String storeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        ivBack = findViewById(R.id.ivBack);
        etProductName = findViewById(R.id.etProductName);
        etProductPrice = findViewById(R.id.etProductPrice);
        etProductDescription = findViewById(R.id.etProductDescription);
        btnAddProduct = findViewById(R.id.btnAddProduct);
        storeId = getIntent().getStringExtra("STORE");


        database = FirebaseDatabase.getInstance().getReference();


        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProductToFirebase();
            }
        });
    }

    private void addProductToFirebase() {

        String name = etProductName.getText().toString().trim();
        String price = etProductPrice.getText().toString().trim();
        String description = etProductDescription.getText().toString().trim();


        if (TextUtils.isEmpty(name)) {
            etProductName.setError("Product name is required");
            return;
        }
        if (TextUtils.isEmpty(price)) {
            etProductPrice.setError("Product price is required");
            return;
        }
        if (TextUtils.isEmpty(description)) {
            etProductDescription.setError("Product description is required");
            return;
        }


        String productId = database.child("products").push().getKey();


        if (productId != null) {
            Product product = new Product(productId, name, Double.parseDouble(price), description);


            database.child("products").child(productId).setValue(product).addOnSuccessListener(aVoid -> {
                // Clear input fields
                etProductName.setText("");
                etProductPrice.setText("");
                etProductDescription.setText("");

                // Add product to store
                database.child("stores").child(storeId).child("products").child(productId).setValue(true).addOnSuccessListener(aVoid1 -> {
                    Toast.makeText(AddProductActivity.this, "Product added to store successfully!", Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(e -> Toast.makeText(AddProductActivity.this, "Failed to add product to store: " + e.getMessage(), Toast.LENGTH_SHORT).show());

            }).addOnFailureListener(e -> Toast.makeText(AddProductActivity.this, "Failed to add product: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        }
    }

}
package com.example.homeal;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class AddProductActivity extends AppCompatActivity {
    private EditText etProductName, etProductPrice, etProductDescription;
    private Button btnAddProduct;
    private DatabaseReference databaseProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);


        etProductName = findViewById(R.id.etProductName);
        etProductPrice = findViewById(R.id.etProductPrice);
        etProductDescription = findViewById(R.id.etProductDescription);
        btnAddProduct = findViewById(R.id.btnAddProduct);


        databaseProducts = FirebaseDatabase.getInstance().getReference("products");


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


        String productId = databaseProducts.push().getKey();


        if (productId != null) {
            Product product = new Product(productId, name, Double.parseDouble(price), description);


            databaseProducts.child(productId)
                    .setValue(product)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(AddProductActivity.this, "Product added successfully!", Toast.LENGTH_SHORT).show();
                        // Clear input fields
                        etProductName.setText("");
                        etProductPrice.setText("");
                        etProductDescription.setText("");
                    })
                    .addOnFailureListener(e -> Toast.makeText(AddProductActivity.this, "Failed to add product: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        }
    }

}
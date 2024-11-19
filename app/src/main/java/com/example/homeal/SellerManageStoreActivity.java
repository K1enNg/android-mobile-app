package com.example.homeal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SellerManageStoreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_seller_manage_store);
    }

    // Method for handling the Add Products button click
    public void onAddProductClick(View view) {
        Intent intent = new Intent(SellerManageStoreActivity.this, AddProductActivity.class);
        startActivity(intent);
    }
}
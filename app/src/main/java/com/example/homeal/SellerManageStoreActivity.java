package com.example.homeal;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SellerManageStoreActivity extends AppCompatActivity {

    TextView StoreName;
    Button btnManageInfo, btnAddProduct;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_seller_manage_store);

        StoreName = findViewById(R.id.StoreName);
        btnManageInfo = findViewById(R.id.btnManageInfo);


        btnManageInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SellerManageStoreActivity.this, "Manage Info button clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
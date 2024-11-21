package com.example.homeal;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class StoreInfo extends AppCompatActivity {

    ImageView ivBack;
    TextView tvStoreName;
    EditText etStoreName, etStoreAddress, etStoreDescription, etStoreContact;
    Button btnUpdateStore;

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


    }
}
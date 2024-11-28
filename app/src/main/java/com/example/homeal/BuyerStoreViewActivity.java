package com.example.homeal;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class BuyerStoreViewActivity extends AppCompatActivity {

    ImageView storeImage;
    TextView storeName;
    ListView listView;
    Button btnLeaveStore;
    List<Product> productList;
    ProductBuyerViewAdapter adapter;
    DatabaseReference database;
    String storeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_buyer_store_view);

        storeImage = findViewById(R.id.ivStoreImage);
        storeName = findViewById(R.id.tvStoreName);
        listView = findViewById(R.id.listView);
        btnLeaveStore = findViewById(R.id.btnLeaveStore);
        storeId = getIntent().getStringExtra("STORE");

        // Initialize Firebase database reference
        database = FirebaseDatabase.getInstance().getReference();

        productList = new ArrayList<>();
        adapter = new ProductBuyerViewAdapter(this, productList);
        listView.setAdapter(adapter);

        btnLeaveStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        updatePage();

    }

    private void updatePage(){

    }
}
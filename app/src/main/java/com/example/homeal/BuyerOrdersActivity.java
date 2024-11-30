package com.example.homeal;

import android.os.Bundle;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class BuyerOrdersActivity extends AppCompatActivity {

    ListView listView;
    DatabaseReference database;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_buyer_orders);

        listView = findViewById(R.id.listView);

        // Initialize Firebase
        database = FirebaseDatabase.getInstance().getReference("orders");
        auth = FirebaseAuth.getInstance();

        fetchOrders();
    }

    private void fetchOrders(){

    }
}
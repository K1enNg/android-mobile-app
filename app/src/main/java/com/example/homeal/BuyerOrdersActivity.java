package com.example.homeal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BuyerOrdersActivity extends AppCompatActivity {

    ImageView ivBack;
    ListView listView;
    OrdersAdapter adapter;
    List<Order> orderList;

    DatabaseReference database;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_buyer_orders);

        ivBack = findViewById(R.id.ivBack);
        listView = findViewById(R.id.listView);
        orderList = new ArrayList<>();
        adapter = new OrdersAdapter(this, orderList);
        listView.setAdapter(adapter);

        // Initialize Firebase
        database = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        fetchOrders();
    }

    private void fetchOrders(){
        if (auth.getUid() != null){
            database.child("users").child(auth.getUid()).child("orders").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        for (DataSnapshot orderSnapshot : snapshot.getChildren()){
                            String orderId = orderSnapshot.getKey();
                            if (orderId != null){
                                updateList(orderId);
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(BuyerOrdersActivity.this, "Could not find user path", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void updateList(String orderId){



        database.child("orders").child(orderId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String storeName = snapshot.child("storeName").getValue(String.class);

                    // Fix for cartItems
                    List<CartItem> cartItems = new ArrayList<>();
                    for (DataSnapshot itemSnapshot : snapshot.child("cartItems").getChildren()) {
                        CartItem cartItem = itemSnapshot.getValue(CartItem.class); // Deserialize into CartItem
                        if (cartItem != null) {
                            cartItems.add(cartItem);
                        }
                    }

                    double totalPrice = snapshot.child("totalPrice").getValue(Double.class);
                    String status = snapshot.child("status").getValue(String.class);
                    String date = snapshot.child("date").getValue(String.class);
                    String payment = snapshot.child("payment").getValue(String.class);

                    Order order = new Order(storeName, cartItems, totalPrice, status, date, payment);

                    orderList.add(order);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(BuyerOrdersActivity.this, "Could not find Order", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
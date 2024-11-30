package com.example.homeal;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.paymentsheet.PaymentSheet;
import com.stripe.android.paymentsheet.PaymentSheetResult;
import com.stripe.android.Stripe;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CheckoutActivity extends AppCompatActivity {

    TextView tvTotalPrice;
    ListView listView;
    BuyerCartViewAdapter adapter;
    List<CartItem> cartItems;

    DatabaseReference database;
    FirebaseAuth auth;

    private PaymentSheet paymentSheet;
    private String clientSecret;
    private Stripe stripe;
    String amount;
    Double int_amount = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_checkout);

        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        listView = findViewById(R.id.listView);
        cartItems = new ArrayList<>();
        adapter = new BuyerCartViewAdapter(this, cartItems);
        listView.setAdapter(adapter);

        database = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();

        if (auth.getUid() != null){
            updateCartDisplay();


            PaymentConfiguration.init(
                    this,
                    "pk_test_51QP35YIZR38B6NnSwkRqbErxpfQBIwy0ed4AjwAdcdflfNMqKOO2PRqIrYkd6HQTWwpfpQuDqTRLAXWl59HF2YAU00YudBPyTc" // Replace with your actual publishable key
            );

            stripe = new Stripe(
                    this,
                    PaymentConfiguration.getInstance(this).getPublishableKey()
            );

            paymentSheet = new PaymentSheet(this, this::onPaymentResult);

            Button btnPayNow = findViewById(R.id.btnPayNow);
            btnPayNow.setOnClickListener(v -> fetchPaymentIntent());
        }
    }

    private void updateCartDisplay(){
        database.child("users").child(auth.getUid()).child("cart").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot productSnapshot : snapshot.getChildren()){
                        String productId = productSnapshot.getKey();
                        int quantity = productSnapshot.child("quantity").getValue(Integer.class);
                        fetchCartItem(productId, quantity);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CheckoutActivity.this, "Could not find Cart", Toast.LENGTH_SHORT).show();
            }
        });

        String totalDisplay = "$" + int_amount;
        //tvTotalPrice.setText(totalDisplay);
    }

    private void fetchCartItem(String productId, int quantity){

        database.child("products").child(productId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String name = snapshot.child("name").getValue(String.class);
                    double price = snapshot.child("price").getValue(Double.class);

                    double total = price * quantity;

                    CartItem item = new CartItem(name, quantity, total);

                    cartItems.add(item);
                    adapter.notifyDataSetChanged();

                    int_amount = int_amount + total;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CheckoutActivity.this, "Could not find Product", Toast.LENGTH_SHORT).show();
            }
        });
    }






    // Stripe Logic

    private void fetchPaymentIntent() {
        OkHttpClient client = new OkHttpClient();
        String backendUrl = "http://10.0.2.2:4242/create-payment-intent"; // Replace with your backend endpoint

        HashMap<String, Object> payload = new HashMap<>();
        int_amount *= 100;
        amount = Double.toString(int_amount);
        payload.put("amount", amount); // Amount in cents (e.g., $20.00)
        payload.put("currency", "usd");

        JSONObject jsonPayload = new JSONObject(payload);
        RequestBody body = RequestBody.create(
                jsonPayload.toString(),
                MediaType.get("application/json; charset=utf-8")
        );

        Request request = new Request.Builder()
                .url(backendUrl)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() ->
                        Toast.makeText(CheckoutActivity.this, e.toString(), Toast.LENGTH_SHORT).show()
                );
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        String responseBody = response.body().string();
                        JSONObject json = new JSONObject(responseBody);
                        clientSecret = json.getString("clientSecret");

                        runOnUiThread(() -> presentPaymentSheet(clientSecret));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    private void presentPaymentSheet(String clientSecret) {
        PaymentSheet.Configuration configuration = new PaymentSheet.Configuration.Builder("Homeal")
                .build();

        paymentSheet.presentWithPaymentIntent(clientSecret, configuration);
    }


    private void onPaymentResult(PaymentSheetResult paymentSheetResult) {
        if (paymentSheetResult instanceof PaymentSheetResult.Completed) {
            Toast.makeText(this, "Payment Successful", Toast.LENGTH_SHORT).show();
        } else if (paymentSheetResult instanceof PaymentSheetResult.Failed) {
            Toast.makeText(this, "Payment Failed", Toast.LENGTH_SHORT).show();
        } else if (paymentSheetResult instanceof PaymentSheetResult.Canceled) {
            Toast.makeText(this, "Payment Canceled", Toast.LENGTH_SHORT).show();
        }
    }
}
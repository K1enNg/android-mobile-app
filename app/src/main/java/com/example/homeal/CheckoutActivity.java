package com.example.homeal;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import android.widget.Button;
import android.widget.Toast;

import com.stripe.android.PaymentConfiguration;
import com.stripe.android.paymentsheet.PaymentSheet;
import com.stripe.android.paymentsheet.PaymentSheetResult;
import com.stripe.android.Stripe;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CheckoutActivity extends AppCompatActivity {
    private PaymentSheet paymentSheet;
    private String clientSecret;
    private Stripe stripe;
    String amount;
    int int_amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_checkout);

        Intent intent = getIntent();
        if (intent != null){
            amount = intent.getStringExtra("amount");
        }


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


    private void fetchPaymentIntent() {
        OkHttpClient client = new OkHttpClient();
        String backendUrl = "http://10.0.2.2:4242/create-payment-intent"; // Replace with your backend endpoint

        HashMap<String, Object> payload = new HashMap<>();
        int_amount = Integer.parseInt(amount);
        int_amount *= 100;
        amount = Integer.toString(int_amount);
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
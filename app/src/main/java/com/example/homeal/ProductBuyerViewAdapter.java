package com.example.homeal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class ProductBuyerViewAdapter extends ArrayAdapter<Product> {
    private final Context context;
    private final List<Product> products;
    private DatabaseReference database;
    private String userId;

    public ProductBuyerViewAdapter(Context context, List<Product> products, String userId, DatabaseReference database) {
        super(context, R.layout.activity_item_product, products);
        this.context = context;
        this.products = products;
        this.userId = userId;
        this.database = database;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_item_product, parent, false);
        }

        Product product = products.get(position);

        //get TextViews from layout
        TextView tvProductName = convertView.findViewById(R.id.tvProductName);
        TextView tvProductDescription = convertView.findViewById(R.id.tvProductDescription);
        TextView tvProductPrice = convertView.findViewById(R.id.tvProductPrice);
        TextView tvQuantityValue = convertView.findViewById(R.id.tvQuantityValue);

        //get Buttons from layout
        Button btnQuantityMinus = convertView.findViewById(R.id.btnQuantityMinus);
        Button btnQuantityPlus = convertView.findViewById(R.id.btnQuantityPlus);
        Button btnAddToCart = convertView.findViewById(R.id.btnAddToCart);
        Button btnRemove = convertView.findViewById(R.id.btnRemove);

        //set values for text views
        tvProductName.setText(product.getName());
        tvProductDescription.setText(product.getDescription());
        tvProductPrice.setText("$" + product.getPrice());

        // plus/minus buttons logic

        btnQuantityMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentQuantity = Integer.parseInt(tvQuantityValue.getText().toString());
                if (currentQuantity > 0) {
                    tvQuantityValue.setText(String.valueOf(currentQuantity - 1));
                }
            }
        });

        btnQuantityPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentQuantity = Integer.parseInt(tvQuantityValue.getText().toString());
                tvQuantityValue.setText(String.valueOf(currentQuantity + 1));
            }
        });

        // add to cart button logic
        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Integer.parseInt(tvQuantityValue.getText().toString()) > 0) {
                    DatabaseReference cartRef = database.child("users").child(userId).child("cart");

                    cartRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild(product.getId())) {
                                DataSnapshot itemSnapshot = snapshot.child(product.getId());
                                int currentQuantity = itemSnapshot.child("quantity").getValue(Integer.class);
                                int newQuantity = currentQuantity + Integer.parseInt(tvQuantityValue.getText().toString());
                                itemSnapshot.getRef().child("quantity").setValue(newQuantity);
                            } else {
                                cartRef.child(product.getId()).child("quantity").setValue(Integer.parseInt(tvQuantityValue.getText().toString()));
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(context, "Failed to add to cart", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(context, "product quantity cannot be 0", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // remove button logic
        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (product.getId() != null) {
                    DatabaseReference cartRef = database.child("users").child(userId).child("cart");

                    cartRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild(product.getId())) {
                                cartRef.child(product.getId()).removeValue();
                            }
                            else {
                                Toast.makeText(context, "Item is not in cart", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(context, "Database Error", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(context, "Invalid product", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return convertView;
    }
}

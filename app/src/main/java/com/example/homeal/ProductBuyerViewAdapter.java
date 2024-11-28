package com.example.homeal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class ProductBuyerViewAdapter extends ArrayAdapter<Product> {
    private final Context context;
    private final List<Product> products;
    private DatabaseReference database;

    public ProductBuyerViewAdapter(Context context, List<Product> products) {
        super(context, R.layout.activity_item_product, products);
        this.context = context;
        this.products = products;
        database = FirebaseDatabase.getInstance().getReference("products");
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_item_product, parent, false);
        }

        Product product = products.get(position);

        TextView tvProductName = convertView.findViewById(R.id.tvProductName);
        TextView tvProductDescription = convertView.findViewById(R.id.tvProductDescription);
        TextView tvProductPrice = convertView.findViewById(R.id.tvProductPrice);
        Button btnAddToCart = convertView.findViewById(R.id.btnAddToCart);

        tvProductName.setText(product.getName());
        tvProductDescription.setText(product.getDescription());
        tvProductPrice.setText("$" + product.getPrice());

        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        return convertView;
    }
}

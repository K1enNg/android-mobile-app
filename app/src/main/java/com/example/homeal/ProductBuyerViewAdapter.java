package com.example.homeal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

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



        return convertView;
    }
}

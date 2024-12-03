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
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class BuyerCartViewAdapter extends ArrayAdapter<CartItem> {
    private final Context context;
    private final List<CartItem> cartItems;

    public BuyerCartViewAdapter(Context context, List<CartItem> cartItems) {
        super(context, R.layout.cart_list_custom, cartItems);
        this.context = context;
        this.cartItems = cartItems;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.cart_list_custom, parent, false);
        }

        CartItem cartItem = cartItems.get(position);

        TextView tvProductName = convertView.findViewById(R.id.tvProductName);
        TextView tvProductQuantity = convertView.findViewById(R.id.tvProductQuantity);
        TextView tvProductPrice = convertView.findViewById(R.id.tvProductPrice);


        String quantity = "Quantity: " + cartItem.getQuantity();
        String totalPrice = "$" + cartItem.getTotalPrice();

        tvProductName.setText(cartItem.getName());
        tvProductQuantity.setText(quantity);
        tvProductPrice.setText(totalPrice);

        return convertView;
    }
}

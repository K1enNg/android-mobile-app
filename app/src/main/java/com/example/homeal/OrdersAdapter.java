package com.example.homeal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class OrdersAdapter extends ArrayAdapter<Order> {

    private final Context context;
    private final List<Order> orders;
    private DatabaseReference database;

    public OrdersAdapter(Context context, List<Order> orders) {
        super(context, R.layout.activity_item_product, orders);
        this.context = context;
        this.orders = orders;
        this.database = database;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.order_list_custom, parent, false);
        }

        Order order = orders.get(position);

        TextView tvStoreName = convertView.findViewById(R.id.tvStoreName);
        TextView tvProductInfo = convertView.findViewById(R.id.tvProductInfo);
        TextView tvTotalPrice = convertView.findViewById(R.id.tvTotalPrice);
        TextView tvPaymentMethod = convertView.findViewById(R.id.tvPaymentMethod);
        TextView tvDate = convertView.findViewById(R.id.tvDate);
        TextView tvStatus = convertView.findViewById(R.id.tvStatus);

        tvStoreName.setText(order.getStoreName());
        tvTotalPrice.setText("Total: $" + order.getTotalPrice());
        tvPaymentMethod.setText("Payment Method: " + order.getPayment());
        tvDate.setText("Date: " + order.getDate());
        tvStatus.setText("Status: " + order.getStatus());


        StringBuilder productInfoBuilder = new StringBuilder();

        for (CartItem item : order.getCartItems()) {
            productInfoBuilder.append(item.getName()).append("\t Quantity: ").append(item.getQuantity()).append("\t Total: ").append(item.getTotalPrice()).append("\n");
        }

        tvProductInfo.setText(productInfoBuilder.toString());

        return convertView;
    }
}

package com.example.homeal;

import static android.content.Intent.getIntent;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class ProductManageStoreAdapter extends ArrayAdapter<Product> {

    private final Context context;
    private final List<Product> productList;
    private DatabaseReference database;

    public ProductManageStoreAdapter(Context context, List<Product> productList) {
        super(context, R.layout.product_list_custom, productList);
        this.context = context;
        this.productList = productList;
        this.database = FirebaseDatabase.getInstance().getReference("products");
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.product_list_custom, parent, false);
        }

        Product product = productList.get(position);

        TextView tvProductInfo = convertView.findViewById(R.id.tvProductInfo);
        Button btnEditProduct = convertView.findViewById(R.id.btnEditProduct);
        Button btnDeleteProduct = convertView.findViewById(R.id.btnDeleteProduct);

        tvProductInfo.setText(product.getName() + " - $" + product.getPrice());

        btnEditProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, ManageProductInfoActivity.class);
                intent.putExtra("PRODUCT", product.getId());
                intent.putExtra("STORE", product.getStoreId());
                context.startActivity(intent);
            }
        });

        btnDeleteProduct.setOnClickListener(view -> ShowDeletionConfirmationDialog(product));

        return convertView;
    }

    private void ShowDeletionConfirmationDialog (Product product) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Delete Product");
        builder.setMessage("Are you sure you want to delete this product?");
        builder.setPositiveButton("Yes", (dialog, which) -> deleteProduct(product.getId()));
        builder.setNegativeButton("No", null);
        builder.show();
    }

    private void deleteProduct(String productId) {
        database.child(productId).removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                productList.removeIf(product -> product.getId().equals(productId));
                notifyDataSetChanged();
            } else {
                Toast.makeText(context, "Failed to delete product", Toast.LENGTH_SHORT).show();

            }
        });
    }
}

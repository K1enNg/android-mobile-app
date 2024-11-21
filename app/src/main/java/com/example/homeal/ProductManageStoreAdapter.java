package com.example.homeal;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ProductManageStoreAdapter extends RecyclerView.Adapter<ProductManageStoreAdapter.ProductViewHolder> {

    public ProductManageStoreAdapter() {}

    public static class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView tvProductInfo;
        Button btnEditProduct, btnDeleteProduct;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductInfo = itemView.findViewById(R.id.tvProductInfo);
            btnEditProduct = itemView.findViewById(R.id.btnEditProduct);
            btnDeleteProduct = itemView.findViewById(R.id.btnDeleteProduct);
        }
    }

    @NonNull
    @Override
    public ProductManageStoreAdapter.ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_list_custom, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductManageStoreAdapter.ProductViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}

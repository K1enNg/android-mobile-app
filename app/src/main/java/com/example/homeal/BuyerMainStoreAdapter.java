package com.example.homeal;

import android.content.Context;
import android.content.Intent;
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

public class BuyerMainStoreAdapter extends ArrayAdapter<Store> {
    private final Context context;
    private final List<Store> storeList;
    private DatabaseReference database;

    public BuyerMainStoreAdapter(Context context, List<Store> storeList) {
        super(context, R.layout.activity_item_store, storeList);
        this.context = context;
        this.storeList = storeList;
        this.database = FirebaseDatabase.getInstance().getReference("stores");
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_item_store, parent, false);
        }

        Store store = storeList.get(position);

        TextView tvStoreName = convertView.findViewById(R.id.tvStoreName);
        TextView tvStoreInfo = convertView.findViewById(R.id.tvStoreInfo);
        Button btnViewStore = convertView.findViewById(R.id.btnViewStore);

        tvStoreName.setText(store.getStoreName());
        tvStoreInfo.setText(store.getStoreDescription() + " - " + store.getStoreAddress());

        btnViewStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ManageProductInfoActivity.class);
                intent.putExtra("STORE", store.getStoreId());
                context.startActivity(intent);
            }
        });

        return convertView;
    }
}

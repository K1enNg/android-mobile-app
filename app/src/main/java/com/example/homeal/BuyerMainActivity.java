package com.example.homeal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.location.Geocoder;
import android.location.Address;

public class BuyerMainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private DatabaseReference databaseReference;
    private FirebaseAuth auth;
    private ListView listView;
    BuyerMainStoreAdapter adapter;
    List<Store> storeList;

    // List to store store addresses and names
    private List<Map<String, String>> storeData = new ArrayList<>();
    private static final String TAG = "BuyerMainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_main);

        //initializing store display components
        listView = findViewById(R.id.listView);
        storeList = new ArrayList<>();
        adapter = new BuyerMainStoreAdapter(this, storeList);
        listView.setAdapter(adapter);

        // Initialize the map fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // Initialize Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("stores");
        auth = FirebaseAuth.getInstance();

        // Check if the user is logged in
        if (auth.getCurrentUser() != null) {
            // Fetch store addresses and names from Firebase
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // Clear the list to avoid duplicates
                    storeData.clear();

                    // Load store data
                    for (DataSnapshot storeSnapshot : dataSnapshot.getChildren()) {
                        String address = storeSnapshot.child("storeAddress").getValue(String.class);
                        String name = storeSnapshot.child("storeName").getValue(String.class);

                        if (address != null && name != null) {
                            Log.d(TAG, "Store found: " + name + " at address: " + address);

                            // Store address and name in a map
                            Map<String, String> storeInfo = new HashMap<>();
                            storeInfo.put("address", address);
                            storeInfo.put("name", name);
                            storeData.add(storeInfo);
                        }
                    }

                    // Update the map after loading all data
                    if (mMap != null) {
                        updateMapMarkers();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e(TAG, "Error accessing Firebase: " + databaseError.getMessage());
                }
            });

            // Store items logic
            updateListView();
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // Enable zoom controls
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);

        // If data is already loaded, add markers
        if (!storeData.isEmpty()) {
            updateMapMarkers();
        }

        // Optional: Move the camera to an initial location, like Montreal
        LatLng montrealAddress = new LatLng(45.4925, -73.5781);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(montrealAddress, 12));
    }

    // Updates map markers with the loaded data
    private void updateMapMarkers() {
        Log.d(TAG, "Starting map marker update.");

        // Clear existing markers
        mMap.clear();

        // Add new markers for each store
        for (Map<String, String> storeInfo : storeData) {
            // Use geocoding to convert the address to coordinates
            String address = storeInfo.get("address");
            String name = storeInfo.get("name");

            // Log each loaded address and store name
            Log.d(TAG, "Attempting to add store: " + name + " at address: " + address);

            LatLng coordinates = getCoordinatesFromAddress(address);

            if (coordinates != null) {
                Log.d(TAG, "Adding marker: " + name + " at coordinates: " + coordinates.toString());
                // Add a marker for each store, using the store's name as the title
                mMap.addMarker(new MarkerOptions().position(coordinates).title(name));
            } else {
                Log.e(TAG, "Failed to convert address to coordinates: " + address);
            }
        }

        // Show a Toast with the number of updated stores
        Toast.makeText(this, "Map updated with " + storeData.size() + " stores", Toast.LENGTH_SHORT).show();

        // Center the camera on the last added marker, if it exists
        if (!storeData.isEmpty()) {
            Map<String, String> lastStoreInfo = storeData.get(storeData.size() - 1);
            LatLng lastCoordinates = getCoordinatesFromAddress(lastStoreInfo.get("address"));
            if (lastCoordinates != null) {
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastCoordinates, 12));
            }
        }
    }

    // Function to convert an address into coordinates using Geocoder
    private LatLng getCoordinatesFromAddress(String address) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocationName(address, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address location = addresses.get(0);
                return new LatLng(location.getLatitude(), location.getLongitude());
            } else {
                Log.e(TAG, "Geocoder could not find coordinates for address: " + address);
            }
        } catch (IOException e) {
            Log.e(TAG, "Error converting address to coordinates: " + e.getMessage());
        }
        return null;
    }

    private void updateListView(){
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot storeSnapshot : snapshot.getChildren()){
                        String storeId = storeSnapshot.getKey();
                        String storeName = storeSnapshot.child("storeName").getValue(String.class);
                        String storeAddress = storeSnapshot.child("storeAddress").getValue(String.class);
                        String storeDescription = storeSnapshot.child("storeDescription").getValue(String.class);
                        String storeContact = storeSnapshot.child("storeContact").getValue(String.class);

                        Store store = new Store(storeId, storeName, storeAddress, storeDescription, storeContact);
                        storeList.add(store);
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    Toast.makeText(BuyerMainActivity.this, "No stores found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(BuyerMainActivity.this, "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}

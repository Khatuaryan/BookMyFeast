package com.example.bookmyfeast;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RestaurantListActivity extends AppCompatActivity {
    private String mealType, establishmentType, cuisineType;
    private List<Map<String, Object>> restaurants;

    @Override
    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_restaurant_list);

        String phone = getIntent().getStringExtra("phone");
        mealType = getIntent().getStringExtra("meal_type");
        cuisineType = getIntent().getStringExtra("cuisine_type");
        establishmentType = getIntent().getStringExtra("establishment_type");

        ListView restaurantListView = findViewById(R.id.rest_items);
        restaurants = fetchRestaurants();
        SimpleAdapter adapter = createSimpleAdapter(restaurants, R.layout.rest_list_items);
        restaurantListView.setAdapter(adapter);

        ImageButton btn = findViewById(R.id.back);
        btn.setOnClickListener(v -> startActivity(new Intent(RestaurantListActivity.this, HomeActivity.class)));

        restaurantListView.setOnItemClickListener((AdapterView<?> parent, android.view.View view, int position, long id) -> {
            Map<String, Object> selectedRestaurant = restaurants.get(position);
            Intent I = new Intent(RestaurantListActivity.this, RestaurantPage.class);
            I.putExtra("restName", (String) selectedRestaurant.get("name"));
            I.putExtra("restLoc", (String) selectedRestaurant.get("locality"));
            I.putExtra("restPrice", (String) selectedRestaurant.get("price"));
            I.putExtra("restRating", (String) selectedRestaurant.get("rating"));
            I.putExtra("restImg", (int) selectedRestaurant.get("image"));
            I.putExtra("restHighlights", (String) selectedRestaurant.get("highlights"));
            I.putExtra("restAddress", (String) selectedRestaurant.get("address"));
            I.putExtra("restRatingText", (String) selectedRestaurant.get("ratingText"));
            I.putExtra("restLat", (Double) selectedRestaurant.get("lat"));
            I.putExtra("restLong", (Double) selectedRestaurant.get("long"));
            I.putExtra("restLocVerb", (String) selectedRestaurant.get("locVerb"));
            I.putExtra("phone", phone);
            startActivity(I);
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @SuppressLint({"Range", "DiscouragedApi"})
    private List<Map<String, Object>> fetchRestaurants() {
        List<Map<String, Object>> data = new ArrayList<>();
        String query;
        String[] args;

        if (mealType != null) {
            query = "SELECT * FROM restaurants WHERE meal_options LIKE ?";
            args = new String[]{"%" + mealType + "%"};
        } else if (cuisineType != null) {
            query = "SELECT * FROM restaurants WHERE cuisines LIKE ?";
            args = new String[]{"%" + cuisineType + "%"};
        } else if (establishmentType != null) {
            query = "SELECT * FROM restaurants WHERE establishment=?";
            args = new String[]{"%" + establishmentType + "%"};
        } else
            return data;

        try (RestDBHelper dbHelper = new RestDBHelper(this);
             SQLiteDatabase db = dbHelper.getReadableDatabase();
             Cursor cursor = db.rawQuery(query, args)) {
            while (cursor.moveToNext()) {
                Map<String, Object> restaurant = new HashMap<>();
                restaurant.put("name", cursor.getString(cursor.getColumnIndex("name")));
                restaurant.put("locality", cursor.getString(cursor.getColumnIndex("locality_verbose")));
                restaurant.put("price", "₹" + cursor.getInt(cursor.getColumnIndex("cost_for_two")));
                restaurant.put("rating", cursor.getString(cursor.getColumnIndex("rating")));
                restaurant.put("cuisine", cursor.getString(cursor.getColumnIndex("cuisines")));
                restaurant.put("image", getResources().getIdentifier(cursor.getString(cursor.getColumnIndex("pictures")).trim(), "drawable", getPackageName()));
                restaurant.put("highlights", cursor.getString(cursor.getColumnIndex("highlights")));
                restaurant.put("address", cursor.getString(cursor.getColumnIndex("address")));
                restaurant.put("ratingText", cursor.getString(cursor.getColumnIndex("rating_text")));
                restaurant.put("lat", cursor.getDouble(cursor.getColumnIndex("latitude")));
                restaurant.put("long", cursor.getDouble(cursor.getColumnIndex("longitude")));
                restaurant.put("locVerb", cursor.getString(cursor.getColumnIndex("locality_verbose")));
                data.add(restaurant);
                Log.d("Data Retrieval", "Fetching restaurants for establishment type: " + data.size());
            }
        }
        return data;
    }

    private SimpleAdapter createSimpleAdapter(List<Map<String, Object>> data, int layoutId) {
        String[] from = {"name", "locality", "price", "rating", "cuisine", "image"};
        int[] to = {R.id.restaurantName, R.id.restaurantLocation, R.id.restaurantPrice, R.id.restaurantRating, R.id.restaurantCuisine, R.id.backgroundImage};
        return new SimpleAdapter(this, data, layoutId, from, to);
    }
}
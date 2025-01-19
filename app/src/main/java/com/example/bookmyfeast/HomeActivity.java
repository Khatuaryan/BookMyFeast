package com.example.bookmyfeast;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeActivity extends AppCompatActivity implements RecomRestAdapter.RestaurantClickListener {

    private static final int RANDOM_RESTAURANT_COUNT = 5;
    String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        phone = getIntent().getStringExtra("phone");

        TextView greet = findViewById(R.id.greeting_text);
        String username = getUsername();
        greet.setText(String.format("Hey %s, What’s cooking in your mind?", username));

        // MEAL TYPE
        RecyclerView mtRv = findViewById(R.id.meal_type_items);
        mtRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        List<MealTypeModel> mtItems = new ArrayList<>();
        mtItems.add(new MealTypeModel(R.drawable.breakfast, "Breakfast"));
        mtItems.add(new MealTypeModel(R.drawable.lunch, "Lunch"));
        mtItems.add(new MealTypeModel(R.drawable.fastfood, "Fast Food"));
        mtItems.add(new MealTypeModel(R.drawable.dinner, "Dinner"));
        mtItems.add(new MealTypeModel(R.drawable.nearme, "Near Me"));
        mtRv.setAdapter(new MealTypeAdapter(mtItems, this, phone));

        // RECOMMENDED RESTAURANT
        RecyclerView rrRv = findViewById(R.id.recom_rest_items);
        rrRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        List<RecomRestModel> rrItems = fetchRestaurantData();
        Collections.shuffle(rrItems);
        rrItems = rrItems.subList(0, Math.min(RANDOM_RESTAURANT_COUNT, rrItems.size()));
        Log.d("HomeActivity", "Recommended restaurants count: " + rrItems.size());
        rrRv.setAdapter(new RecomRestAdapter(this, rrItems, this));

        //POPULAR CUISINE
        RecyclerView ppRv = findViewById(R.id.pop_cuisine_items);
        ppRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        List<PopCuisineModel> pcItems = new ArrayList<>();
        pcItems.add(new PopCuisineModel(R.drawable.italian, "Italian"));
        pcItems.add(new PopCuisineModel(R.drawable.mediterranean, "Mediterranean"));
        pcItems.add(new PopCuisineModel(R.drawable.chinese, "Chinese"));
        pcItems.add(new PopCuisineModel(R.drawable.northindian, "North Indian"));
        pcItems.add(new PopCuisineModel(R.drawable.panasian, "Pan-Asian"));
        pcItems.add(new PopCuisineModel(R.drawable.southindian, "South Indian"));
        ppRv.setAdapter(new PopCuisineAdapter(pcItems, this, phone));

        //BEST BUFFET
        ListView bbLv = findViewById(R.id.best_buffet_lv);
        SimpleAdapter adapter = createSimpleAdapter(fetchRestaurantListData("Buffet"), R.layout.best_buffet_items);
        bbLv.setAdapter(adapter);
        Button view = findViewById(R.id.bb_view_all);
        view.setOnClickListener(v -> {
            Intent I = new Intent(HomeActivity.this, RestaurantListActivity.class);
            I.putExtra("establishmentType", "Buffet");
            I.putExtra("phone", phone);
            startActivity(I);
        });

        //FINE DINE
        ListView fdLv = findViewById(R.id.fine_dine_lv);
        SimpleAdapter fineDineAdapter = createSimpleAdapter(fetchRestaurantListData("Fine Dining"), R.layout.fine_dine_items);
        fdLv.setAdapter(fineDineAdapter);

        Button search = findViewById(R.id.search);
        search.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, SearchActivity.class).putExtra("phone", phone)));

        FloatingActionButton pay_bill = findViewById(R.id.fab_pay_bill);
        pay_bill.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, SearchActivity.class).putExtra("phone", phone)));

        BottomNavigationView nav_bar = findViewById(R.id.bottom_navigation);
        nav_bar.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_home) {
                return true;
            } else if (item.getItemId() == R.id.nav_profile) {
                Intent I = new Intent(HomeActivity.this, ProfileActivity.class);
                I.putExtra("phone", phone);
                startActivity(I);
                return true;
            } else {
                return false;
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @SuppressLint("Range")
    private String getUsername() {
        String username = "";
        try (UserDBHelper dbHelper = new UserDBHelper(this);
             SQLiteDatabase db = dbHelper.getReadableDatabase();
             Cursor cursor = db.rawQuery("SELECT Name FROM users WHERE Phone=?", new String[]{phone})) {
            if (cursor != null && cursor.moveToFirst())
                username = cursor.getString(cursor.getColumnIndex("Name"));
        }
        username = username.split(" ")[0];
        return username;
    }

    @SuppressLint({"Range", "DiscouragedApi"})
    private List<RecomRestModel> fetchRestaurantData() {
        List<RecomRestModel> restaurantList = new ArrayList<>();
        try (RestDBHelper dbHelper = new RestDBHelper(this);
             SQLiteDatabase db = dbHelper.getReadableDatabase();
             Cursor cursor = db.rawQuery("SELECT * FROM restaurants", null)) {
            if (cursor != null && cursor.moveToFirst()) {
                Log.d("HomeActivity", "Cursor returned " + cursor.getCount() + " rows.");
                do {
                    String name = cursor.getString(cursor.getColumnIndex("name"));
                    String address = cursor.getString(cursor.getColumnIndex("address"));
                    String locality = cursor.getString(cursor.getColumnIndex("locality_verbose"));
                    String price = cursor.getString(cursor.getColumnIndex("cost_for_two"));
                    String highlights = cursor.getString(cursor.getColumnIndex("highlights"));
                    String rating = cursor.getString(cursor.getColumnIndex("rating"));
                    int pictureResId = getResources().getIdentifier(cursor.getString(cursor.getColumnIndex("pictures")).trim(), "drawable", getPackageName());
                    String ratingText = cursor.getString(cursor.getColumnIndex("rating_text"));
                    Double lat = cursor.getDouble(cursor.getColumnIndex("latitude"));
                    Double lon = cursor.getDouble(cursor.getColumnIndex("longitude"));
                    String locVerb = cursor.getString(cursor.getColumnIndex("locality_verbose"));
                    restaurantList.add(new RecomRestModel(name, locality, pictureResId, rating, highlights, price, address, ratingText, lat, lon, locVerb));
                } while (cursor.moveToNext());
            }
        }
        Log.d("HomeActivity", "Number of recommended restaurants fetched: " + restaurantList.size());
        return restaurantList;
    }

    @SuppressLint({"Range", "DiscouragedApi"})
    private List<Map<String, Object>> fetchRestaurantListData(String establishmentType) {
        List<Map<String, Object>> data = new ArrayList<>();
        String query = "SELECT * FROM restaurants WHERE establishment LIKE ? ORDER BY rating DESC LIMIT 3";
        String likeQuery = "%" + establishmentType + "%";
        try (RestDBHelper dbHelper = new RestDBHelper(this);
             SQLiteDatabase db = dbHelper.getReadableDatabase();
             Cursor cursor = db.rawQuery(query, new String[]{likeQuery})) {
            while (cursor.moveToNext()) {
                Map<String, Object> restaurant = new HashMap<>();
                restaurant.put("name", cursor.getString(cursor.getColumnIndex("name")));
                restaurant.put("locality", cursor.getString(cursor.getColumnIndex("locality_verbose")));
                restaurant.put("price", cursor.getInt(cursor.getColumnIndex("cost_for_two")) + " for two");
                restaurant.put("ratingText", cursor.getString(cursor.getColumnIndex("rating_text")));
                int imageId = getResources().getIdentifier(cursor.getString(cursor.getColumnIndex("pictures")).trim(), "drawable", getPackageName());
                restaurant.put("lat", cursor.getDouble(cursor.getColumnIndex("latitude")));
                restaurant.put("lon", cursor.getDouble(cursor.getColumnIndex("longitude")));
                restaurant.put("rating", cursor.getString(cursor.getColumnIndex("rating")));
                restaurant.put("locVerb", cursor.getString(cursor.getColumnIndex("locality_verbose")));
                restaurant.put("image", imageId);
                data.add(restaurant);
            }
        }
        return data;
    }

    private SimpleAdapter createSimpleAdapter(List<Map<String, Object>> data, int layoutId) {
        String[] from = {"name", "locality", "price", "image"};
        int[] to = {R.id.rest_name, R.id.rest_loc, R.id.rest_price, R.id.rest_img};
        SimpleAdapter adapter = new SimpleAdapter(this, data, layoutId, from, to) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                ImageButton imageButton = view.findViewById(R.id.next_btn);
                imageButton.setOnClickListener(v -> {
                    Map<String, Object> restaurantData = data.get(position);
                    Intent I = new Intent(HomeActivity.this, RestaurantPage.class);
                    I.putExtra("restName", (String) restaurantData.get("name"));
                    I.putExtra("restLoc", (String) restaurantData.get("locality"));
                    I.putExtra("restImg", (int) restaurantData.get("image"));
                    I.putExtra("restPrice", (String) restaurantData.get("price"));
                    I.putExtra("restLat", (double) restaurantData.get("lat"));
                    I.putExtra("restLon", (double) restaurantData.get("lon"));
                    I.putExtra("restLocVerb", (String) restaurantData.get("locVerb"));
                    I.putExtra("restRating", (String) restaurantData.get("rating"));
                    I.putExtra("phone", phone);

                    startActivity(I);
                });

                return view;
            }
        };
        return adapter;
    }

    @Override
    public void onRestaurantClick(RecomRestModel recomRestModel) {
        Intent intent = new Intent(HomeActivity.this, RestaurantPage.class);
        intent.putExtra("restName", recomRestModel.getName());
        intent.putExtra("restLoc", recomRestModel.getAddress());
        intent.putExtra("restImg", recomRestModel.getPictureResId());
        intent.putExtra("restPrice", recomRestModel.getPrice());
        intent.putExtra("restLat", recomRestModel.getLat());
        intent.putExtra("restLon", recomRestModel.getLon());
        intent.putExtra("restLocVerb", recomRestModel.getLocVerb());
        intent.putExtra("phone", phone);
        startActivity(intent);
    }
}
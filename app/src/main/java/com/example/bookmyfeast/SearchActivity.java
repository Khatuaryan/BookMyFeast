package com.example.bookmyfeast;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SearchActivity extends AppCompatActivity {

    String name, address, locality, price, highlights, rating, ratingText, locVerb;
    double lat, lon;
    int pictureResId;
    @Override
    @SuppressLint({"Recycle", "Range", "MissingInflatedId"})
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search);

        ImageButton back = findViewById(R.id.back);
        back.setOnClickListener(view -> startActivity(new Intent(SearchActivity.this, HomeActivity.class).putExtra("phone", getIntent().getStringExtra("phone"))));

        EditText ed = findViewById(R.id.search);
        Button btn = findViewById(R.id.search_btn);
        btn.setOnClickListener(v -> {
            String query = ed.getText().toString().trim();
            SQLiteDatabase db = openOrCreateDatabase("restaurants.db", MODE_PRIVATE, null);
            Cursor cursor = db.rawQuery("SELECT * FROM restaurants WHERE name LIKE ?", new String[]{query});
            if (cursor != null && cursor.moveToFirst()) {
                name = cursor.getString(cursor.getColumnIndex("name"));
                address = cursor.getString(cursor.getColumnIndex("address"));
                locality = cursor.getString(cursor.getColumnIndex("locality_verbose"));
                price = cursor.getString(cursor.getColumnIndex("cost_for_two"));
                highlights = cursor.getString(cursor.getColumnIndex("highlights"));
                rating = cursor.getString(cursor.getColumnIndex("rating"));
                pictureResId = getResources().getIdentifier(cursor.getString(cursor.getColumnIndex("pictures")).trim(), "drawable", getPackageName());
                ratingText = cursor.getString(cursor.getColumnIndex("rating_text"));
                lat = cursor.getDouble(cursor.getColumnIndex("latitude"));
                lon = cursor.getDouble(cursor.getColumnIndex("longitude"));
                locVerb = cursor.getString(cursor.getColumnIndex("locality_verbose"));
            }
            Intent I = new Intent(SearchActivity.this, RestaurantPage.class);
            I.putExtra("phone", getIntent().getStringExtra("phone"));
            I.putExtra("restName", name);
            I.putExtra("restAddress", address);
            I.putExtra("restLocality", locality);
            I.putExtra("restPrice", price);
            I.putExtra("restHighlights", highlights);
            I.putExtra("restRating", rating);
            I.putExtra("restRatingText", ratingText);
            I.putExtra("restImg", pictureResId);
            I.putExtra("restLat", lat);
            I.putExtra("restLon", lon);
            I.putExtra("restLocVerb", locVerb);
            startActivity(I);
        });

        Button scan = findViewById(R.id.scan);
        scan.setOnClickListener(v -> {
            Intent intent = getPackageManager().getLaunchIntentForPackage("com.google.android.apps.nbu.paisa.user");
            if (intent != null) {
                startActivity(intent);
            } else {
                Intent playStoreIntent = new Intent(Intent.ACTION_VIEW);
                playStoreIntent.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.nbu.paisa.user"));
                startActivity(playStoreIntent);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
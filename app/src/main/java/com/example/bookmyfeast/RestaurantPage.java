package com.example.bookmyfeast;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RestaurantPage extends AppCompatActivity {

    private Spinner dateSpinner, timeSpinner, guestSpinner;
    private String selectedDate = "";
    private String selectedTime = "";
    private String selectedGuests = "";

    @SuppressLint({"SetTextI18n", "Range", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_page);

        String phone = getIntent().getStringExtra("phone");

        ImageView cafeImage = findViewById(R.id.cafeImage);
        cafeImage.setImageResource(getIntent().getIntExtra("restImg", R.drawable.nearme));

        TextView restaurantName = findViewById(R.id.restaurantName);
        restaurantName.setText(getIntent().getStringExtra("restName"));

        TextView location = findViewById(R.id.location);
        location.setText(getIntent().getStringExtra("restLoc"));

        TextView rating = findViewById(R.id.rating);
        rating.setText(getIntent().getStringExtra("restRating"));

        TextView ratingText = findViewById(R.id.ratingText);
        ratingText.setText(getIntent().getStringExtra("restRatingText"));

        TextView cost = findViewById(R.id.cost);
        cost.setText("approx. ₹" + getIntent().getStringExtra("restPrice")+" for two");

        Button address = findViewById(R.id.address);
        address.setText("Location – " + getIntent().getStringExtra("restAddress"));
        address.setOnClickListener(v -> startActivity(
                new Intent(RestaurantPage.this, MapsActivity.class).
                        putExtra("restLat", getIntent().getDoubleExtra("restLat", 0.0)).
                        putExtra("restLong", getIntent().getDoubleExtra("restLong", 0.0)).
                        putExtra("restName", getIntent().getStringExtra("restName"))));

        dateSpinner = findViewById(R.id.dateSpinner);
        timeSpinner = findViewById(R.id.timeSpinner);
        guestSpinner = findViewById(R.id.guestSpinner);
        Button payBillButton = findViewById(R.id.payBillButton);
        Button bookNowButton = findViewById(R.id.bookNowButton);

        setupDateSpinner();
        setupTimeSpinner();
        setupGuestSpinner();

        ImageButton back = findViewById(R.id.back);
        back.setOnClickListener(view -> startActivity(new Intent(RestaurantPage.this, HomeActivity.class).putExtra("phone", phone)));

        payBillButton.setOnClickListener(v -> {
            Intent I = new Intent(RestaurantPage.this, PayBillActivity.class);
            I.putExtra("phone", phone);
            I.putExtra("restName", getIntent().getStringExtra("restName"));
            I.putExtra("restAddress", getIntent().getStringExtra("restAddress"));
            I.putExtra("restLoc", getIntent().getStringExtra("restLoc"));
            I.putExtra("restRating", getIntent().getStringExtra("restRating"));
            I.putExtra("restPrice", getIntent().getStringExtra("restPrice"));
            I.putExtra("restImg", getIntent().getIntExtra("restImg", R.drawable.nearme));
            I.putExtra("restLocVerb", getIntent().getStringExtra("restLocVerb"));
            startActivity(I);
        });

        bookNowButton.setOnClickListener(v -> {
            if (selectedDate.isEmpty() || selectedTime.isEmpty() || selectedGuests.isEmpty()) {
                Toast.makeText(RestaurantPage.this, "Please select date, time, and guests.", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean bookingSaved = false;
            try (BookDBHelper bookDBHelper = new BookDBHelper(this);
                 SQLiteDatabase db = bookDBHelper.getWritableDatabase()) {

                ContentValues values = new ContentValues();
                values.put(BookDBHelper.COLUMN_DATE, selectedDate);
                values.put(BookDBHelper.COLUMN_TIME, selectedTime);
                values.put(BookDBHelper.COLUMN_NAME, restaurantName.getText().toString());
                values.put(BookDBHelper.COLUMN_ADDRESS, address.getText().toString());
                values.put(BookDBHelper.COLUMN_GUESTS, Integer.parseInt(selectedGuests));
                values.put(BookDBHelper.COLUMN_USER_ID, phone);

                long newRowId = db.insert(BookDBHelper.TABLE_NAME, null, values);
                if (newRowId != -1) {
                    Log.d("Booking", "Booking saved successfully.");
                    bookingSaved = true;
                } else
                    Log.d("Booking", "Failed to save booking.");
            }

            if (bookingSaved) {
                Intent I = new Intent(RestaurantPage.this, BookingConfirmedActivity.class);
                I.putExtra("date", selectedDate);
                I.putExtra("time", selectedTime);
                I.putExtra("guest", selectedGuests);
                I.putExtra("name", restaurantName.getText().toString());
                I.putExtra("address", address.getText().toString());
                I.putExtra("phone", phone);
                startActivity(I);

                try (UserDBHelper dbHelper = new UserDBHelper(this);
                     SQLiteDatabase db = dbHelper.getWritableDatabase();
                     Cursor cursor = db.rawQuery("SELECT Points FROM users WHERE Phone=?", new String[]{phone})) {
                    if (cursor != null && cursor.moveToFirst()) {
                        int pts = Integer.parseInt(cursor.getString(cursor.getColumnIndex("Points")));
                        pts += 100;
                        ContentValues val = new ContentValues();
                        val.put("Points", pts);
                        db.update("users", val, "Phone=?", new String[]{phone});
                        Log.d("Points", "Points updated successfully.");
                    }
                }
            }
        });
    }

    private void setupDateSpinner() {
        String[] dates = {"Select Date", "Today", "Tomorrow", "Day After Tomorrow"};
        ArrayAdapter<String> dateAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, dates);
        dateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dateSpinner.setAdapter(dateAdapter);
        dateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0)
                    selectedDate = parent.getItemAtPosition(position).toString();
            }
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void setupTimeSpinner() {
        String[] times = {
                "Select Time", "11:30 AM", "12:00 PM" , "12:30 PM", "01:00 PM", "01:30 PM",
                "02:00 PM" , "02:30 PM", "03:00 PM", "03:30 PM", "04:00 PM" , "04:30 PM",
                "05:00 PM" , "05:30 PM" , "06:00 PM" , "06:30 PM" , "07:00 PM" , "07:30 PM" ,
                "08:00 PM" , "08:30 PM" , "09:00 PM" , "09:30 PM" , "10:00 PM" , "10:30 PM" ,
                "11:00 PM" , "11:30 PM" , "12:00 AM"};
        ArrayAdapter<String> timeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, times);
        timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeSpinner.setAdapter(timeAdapter);
        timeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0)
                    selectedTime = parent.getItemAtPosition(position).toString();
            }
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void setupGuestSpinner() {
        String[] guests = {"Select Guests", "1", "2", "3", "4", "5" , "6" , "7" , "8" , "9" , "10"};
        ArrayAdapter<String> guestAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, guests);
        guestAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        guestSpinner.setAdapter(guestAdapter);
        guestSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0)
                    selectedGuests = parent.getItemAtPosition(position).toString();
            }
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }
}
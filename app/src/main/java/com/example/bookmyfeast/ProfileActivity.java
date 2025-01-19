package com.example.bookmyfeast;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    private TextView userName, userEmail;
    private String phone;

    @SuppressLint({"Range", "MissingInflatedId", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        phone = getIntent().getStringExtra("phone");

        userName = findViewById(R.id.user_name);
        TextView userPhone = findViewById(R.id.user_phone);
        userEmail = findViewById(R.id.user_email);
        TextView userPoints = findViewById(R.id.user_points);

        userPhone.setText("Phone: +91"+phone);

        try (UserDBHelper dbHelper = new UserDBHelper(this);
             SQLiteDatabase db = dbHelper.getWritableDatabase();
             Cursor cursor = db.rawQuery("SELECT Email, Name, Points FROM users WHERE Phone=?", new String[]{phone})) {
            if (cursor != null && cursor.moveToFirst()) {
                userName.setText(cursor.getString(cursor.getColumnIndex("Name")));
                userEmail.setText("Email: "+cursor.getString(cursor.getColumnIndex("Email")));
                userPoints.setText("Points: "+cursor.getString(cursor.getColumnIndex("Points")));
            }
        }

        ImageButton back = findViewById(R.id.back);
        back.setOnClickListener(v -> startActivity(new Intent(ProfileActivity.this, HomeActivity.class).putExtra("phone", phone)));

        Button btnEditProfile = findViewById(R.id.btn_edit_profile);
        btnEditProfile.setOnClickListener(v -> showEditProfileDialog());

        Button btnBooking = findViewById(R.id.btn_booking);
        btnBooking.setOnClickListener(v -> openBookingDialog());

        Button btnReviews = findViewById(R.id.btn_reviews);
        btnReviews.setOnClickListener(v -> openReviewsDialog());

        Button btnRateUs = findViewById(R.id.btn_rate_us);
        btnRateUs.setOnClickListener(v -> {
            String rickRollUrl = "https://www.youtube.com/watch?v=dQw4w9WgXcQ";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(rickRollUrl));
            startActivity(intent);
        });

        Button btnLogout = findViewById(R.id.btn_logout);
        btnLogout.setOnClickListener(v -> {
            Toast.makeText(ProfileActivity.this, "Logged Out", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(ProfileActivity.this, MainActivity.class));
            finish();
        });
    }

    @SuppressLint("Range")
    private void showEditProfileDialog() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_edit_profile);

        TextView editName = dialog.findViewById(R.id.edit_name);
        TextView editEmail = dialog.findViewById(R.id.edit_email);
        Button btnSave = dialog.findViewById(R.id.btn_save);

        editName.setText(userName.getText());
        editEmail.setText(userEmail.getText());

        btnSave.setOnClickListener(v -> {
            String newName = editName.getText().toString();
            String newEmail = editEmail.getText().toString();

            try (UserDBHelper dbHelper = new UserDBHelper(this);
                 SQLiteDatabase db = dbHelper.getWritableDatabase()) {
                ContentValues val = new ContentValues();
                val.put("Name", newName);
                val.put("Email", newEmail);
                db.update("users", val, "Phone=?", new String[]{phone});
            }

            userName.setText(newName);
            userEmail.setText(newEmail);
            Toast.makeText(ProfileActivity.this, "Profile Updated", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        dialog.show();
    }

    @SuppressLint("Range")
    private void openBookingDialog() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_my_bookings);
        Window window = dialog.getWindow();
        if (window != null) {
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        TextView bookingDetails = dialog.findViewById(R.id.booking_details);
        Button closeBtn = dialog.findViewById(R.id.btn_close);

        StringBuilder bookingsInfo = new StringBuilder();

        try (BookDBHelper bookDBHelper = new BookDBHelper(this);
             SQLiteDatabase db = bookDBHelper.getReadableDatabase();
             Cursor cursor = db.rawQuery("SELECT * FROM bookings WHERE user_id=?", new String[]{phone})) {

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    String date = cursor.getString(cursor.getColumnIndex(BookDBHelper.COLUMN_DATE));
                    String time = cursor.getString(cursor.getColumnIndex(BookDBHelper.COLUMN_TIME));
                    String name = cursor.getString(cursor.getColumnIndex(BookDBHelper.COLUMN_NAME));
                    String address = cursor.getString(cursor.getColumnIndex(BookDBHelper.COLUMN_ADDRESS));
                    int guests = cursor.getInt(cursor.getColumnIndex(BookDBHelper.COLUMN_GUESTS));

                    bookingsInfo.append("Date: ").append(date).append("\n")
                            .append("Time: ").append(time).append("\n")
                            .append("Name: ").append(name).append("\n")
                            .append("Address: ").append(address).append("\n")
                            .append("Guests: ").append(guests).append("\n\n");

                } while (cursor.moveToNext());
            } else {
                bookingsInfo.append("No bookings found.");
            }
        }

        bookingDetails.setText(bookingsInfo.toString());

        closeBtn.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    private void openReviewsDialog() {
    }
}
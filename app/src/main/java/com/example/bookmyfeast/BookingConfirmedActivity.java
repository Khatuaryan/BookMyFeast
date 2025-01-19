package com.example.bookmyfeast;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class BookingConfirmedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_booking_confirmed);

        TextView date = findViewById(R.id.booking_date);
        TextView time = findViewById(R.id.booking_time);
        TextView guest = findViewById(R.id.guest_count);
        TextView name = findViewById(R.id.restaurant_name);
        TextView address = findViewById(R.id.restaurant_address);

        date.setText(getIntent().getStringExtra("date"));
        time.setText(getIntent().getStringExtra("time"));
        guest.setText(getIntent().getStringExtra("guest"));
        name.setText(getIntent().getStringExtra("name"));
        address.setText(getIntent().getStringExtra("address"));

        Button help = findViewById(R.id.need_help);
        help.setOnClickListener(v -> {
            String phoneNumber = "tel:9503634969";
            Intent I = new Intent(Intent.ACTION_DIAL, Uri.parse(phoneNumber));
            startActivity(I);
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void backToHome(View view) {
        Intent I = new Intent(BookingConfirmedActivity.this, HomeActivity.class);
        I.putExtra("phone", getIntent().getStringExtra("phone"));
        startActivity(I);
    }
}
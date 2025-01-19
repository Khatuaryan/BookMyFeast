package com.example.bookmyfeast;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class PaymentSuccessActivity extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_payment_success);

        double amount = getIntent().getDoubleExtra("amount", -1);
        double discount = getIntent().getDoubleExtra("discount", -1);
        double tax = getIntent().getDoubleExtra("tax", -1);
        double total = getIntent().getDoubleExtra("total", -1);
        TextView tv = findViewById(R.id.restaurant_info);
        tv.setText(getIntent().getStringExtra("restName")+"\n"+getIntent().getStringExtra("restAddress"));

        if (amount == -1 || discount == -1 || tax == -1 || total == -1) {
            Toast.makeText(this, "Error retrieving payment details.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        TextView bill_amt = findViewById(R.id.item_total_amount);
        bill_amt.setText("₹" + amount);

        TextView discount_amt = findViewById(R.id.discount_amount);
        discount_amt.setText("-₹" + discount);

        TextView tax_amt = findViewById(R.id.tax_amount);
        tax_amt.setText("₹" + tax);

        TextView total_amt = findViewById(R.id.total_amount);
        total_amt.setText("₹" + total);

        ImageButton back = findViewById(R.id.back);
        back.setOnClickListener(view -> startActivity(new Intent(PaymentSuccessActivity.this, HomeActivity.class)));

        Button continueButton = findViewById(R.id.continue_button);
        continueButton.setOnClickListener(view -> startActivity(new Intent(PaymentSuccessActivity.this, HomeActivity.class)));

        Button needhelp = findViewById(R.id.need_help);
        needhelp.setOnClickListener(view -> {
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
}
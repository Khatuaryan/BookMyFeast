package com.example.bookmyfeast;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class PayBillActivity extends AppCompatActivity {

    @Override
    @SuppressLint({"MissingInflatedId", "Range"})
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pay_bill);

        String phone = getIntent().getStringExtra("phone");
        TextView name = findViewById(R.id.restName);
        name.setText(getIntent().getStringExtra("restName"));

        TextView loc = findViewById(R.id.restLocverb);
        loc.setText(getIntent().getStringExtra("restLocVerb"));

        ImageButton back = findViewById(R.id.back);
        back.setOnClickListener(view -> startActivity(new Intent(PayBillActivity.this, HomeActivity.class).putExtra("phone", phone)));

        EditText ed = findViewById(R.id.amount);
        Button payButton = findViewById(R.id.payButton);
        payButton.setOnClickListener(v -> {
            String amountText = ed.getText().toString().trim();
            if (amountText.isEmpty()) {
                Toast.makeText(this, "Please enter an amount", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                Double amount = Double.parseDouble(amountText);
                Double discount = 0.15 * amount;
                Double tax = 0.05 * amount;
                Double total = amount - discount + tax;

                Intent I = new Intent(PayBillActivity.this, PaymentSuccessActivity.class);
                I.putExtra("amount", amount);
                I.putExtra("discount", discount);
                I.putExtra("tax", tax);
                I.putExtra("total", total);
                I.putExtra("restName", getIntent().getStringExtra("restName"));
                I.putExtra("restAddress", getIntent().getStringExtra("restAddress"));

                try (UserDBHelper dbHelper = new UserDBHelper(this);
                     SQLiteDatabase db = dbHelper.getWritableDatabase();
                     Cursor cursor = db.rawQuery("SELECT Points FROM users WHERE Phone=?", new String[]{phone})) {
                    if (cursor != null && cursor.moveToFirst()) {
                        int pts = Integer.parseInt(cursor.getString(cursor.getColumnIndex("Points")));
                        pts += 50;
                        ContentValues val = new ContentValues();
                        val.put("Points", pts);
                        db.update("users", val, "Phone=?", new String[]{phone});
                        Toast.makeText(this, "50 points added to your account", Toast.LENGTH_LONG).show();
                    }
                }
                startActivity(I);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid amount entered", Toast.LENGTH_SHORT).show();
            }
        });

        Button view_rest = findViewById(R.id.viewRestaurant);
        view_rest.setOnClickListener(v -> {
            Intent I = new Intent(PayBillActivity.this, RestaurantPage.class);
            I.putExtra("restName", getIntent().getStringExtra("restName"));
            I.putExtra("restAddress", getIntent().getStringExtra("restAddress"));
            I.putExtra("restLoc", getIntent().getStringExtra("restLoc"));
            I.putExtra("restRating", getIntent().getStringExtra("restRating"));
            I.putExtra("restHighlights", getIntent().getStringExtra("restHighlights"));
            I.putExtra("restPrice", getIntent().getStringExtra("restPrice"));
            I.putExtra("restImg", getIntent().getIntExtra("restImg", R.drawable.nearme));
            I.putExtra("phone", phone);
            startActivity(I);

        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
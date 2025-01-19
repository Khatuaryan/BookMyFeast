package com.example.bookmyfeast;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private SQLiteDatabase db;
    private static final String PASSWORD_PATTERN = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$";
    private static final String SQL_SELECT_PASSWORD = "SELECT Password FROM users WHERE Phone=?";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        UserDBHelper objDatabase = new UserDBHelper(this);
        db = objDatabase.getReadableDatabase();
        setContentView(R.layout.activity_main);

        EditText ed1 = findViewById(R.id.phonenumber);
        EditText ed2 = findViewById(R.id.password);

        Button signup = findViewById(R.id.signup);
        signup.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, SignUpActivity.class)));

        Button login = findViewById(R.id.login);
        login.setOnClickListener(v -> {
            String phone = ed1.getText().toString().trim();
            String password = ed2.getText().toString().trim();

            if (!Patterns.PHONE.matcher(phone).matches()) {
                ed1.setError("Please enter a valid phone number.");
                ed1.requestFocus();
                return;
            }
            if (!Pattern.matches(PASSWORD_PATTERN, password)) {
                ed2.setError("Password must be at least 8 characters long, include uppercase, lowercase, numbers, and symbols.");
                ed2.requestFocus();
                return;
            }
            if (isPasswordCorrect(phone, password)) {
                Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, HomeActivity.class).putExtra("phone", phone));
            } else
                showUserNotFoundDialog();
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void showUserNotFoundDialog() {
        new AlertDialog.Builder(this).setTitle("User Not Found")
                .setMessage("Incorrect phone number or password. Please try again.")
                .setNeutralButton("OK", null)
                .create()
                .show();
    }

    private boolean isPasswordCorrect(String phone, String password) {
        String hashedPassword = hashPassword(password);
        if (hashedPassword == null) return false;

        try (Cursor cursor = db.rawQuery(SQL_SELECT_PASSWORD, new String[]{phone})) {
            if (cursor != null && cursor.moveToFirst()) {
                String storedHash = cursor.getString(0);
                return hashedPassword.equals(storedHash);
            }
        } catch (Exception e) {
            Log.e("MainActivity", "Database query failed", e);
        }
        return false;
    }

    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            Log.e("MainActivity", "Error hashing password", e);
            return null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (db != null && db.isOpen()) {
            db.close();
        }
    }
}
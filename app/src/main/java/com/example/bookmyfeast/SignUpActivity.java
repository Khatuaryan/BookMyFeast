package com.example.bookmyfeast;

import android.annotation.SuppressLint;
import android.content.ContentValues;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = "SignUpActivity";
    private final String passwordPattern = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$";
    private SQLiteDatabase db;

    @Override
    @SuppressLint({"CutPasteId", "MissingInflatedId"})
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        UserDBHelper dbHelper = new UserDBHelper(this);
        db = dbHelper.getWritableDatabase();

        setContentView(R.layout.activity_sign_up);

        EditText ed1 = findViewById(R.id.fullname);
        EditText ed2 = findViewById(R.id.ph_num);
        EditText ed3 = findViewById(R.id.email);
        EditText ed4 = findViewById(R.id.register_psd);

        Button login = findViewById(R.id.login);
        login.setOnClickListener(v -> startActivity(new Intent(SignUpActivity.this, MainActivity.class)));

        Button signup = findViewById(R.id.signup);
        signup.setOnClickListener(v -> {
            String name = ed1.getText().toString().trim();
            if (name.isEmpty()) {
                ed1.setError("Enter a name");
                ed1.requestFocus();
                return;
            }

            String phone = ed2.getText().toString();
            if (!Patterns.PHONE.matcher(phone).matches()) {
                ed2.setError("Invalid Phone Number");
                ed2.requestFocus();
                return;
            }

            String email = ed3.getText().toString().trim();
            if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                ed3.setError("Invalid Email");
                ed3.requestFocus();
                return;
            }

            String password = ed4.getText().toString().trim();
            if (!Pattern.matches(passwordPattern, password)) {
                ed4.setError("Enter password in proper format");
                ed4.requestFocus();
                return;
            }

            if (isPhoneExists(phone) || isEmailExists(email)) {
                Toast.makeText(SignUpActivity.this, "User already exists. Please login.", Toast.LENGTH_LONG).show();
                return;
            }

            String hashPassword = hashPassword(password);
            Log.d(TAG, "Hashed Password: " + hashPassword);

            if (hashPassword != null && addUserToDatabase(name, phone, email, hashPassword)) {
                Toast.makeText(SignUpActivity.this, "Signup successful!", Toast.LENGTH_SHORT).show();
                Intent I = new Intent(SignUpActivity.this, HomeActivity.class);
                I.putExtra("phone", phone);
                startActivity(I);
                finish();
            } else {
                Toast.makeText(SignUpActivity.this, "Signup failed. Please try again.", Toast.LENGTH_LONG).show();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private boolean isEmailExists(String email) {
        try (Cursor cursor = db.rawQuery("SELECT Phone FROM users WHERE Email=?", new String[]{email})) {
            boolean exists = cursor.getCount() > 0;
            Log.d(TAG, "User exists: " + exists);
            return exists;
        } catch (Exception e) {
            Log.e(TAG, "Error checking user existence", e);
            return false;
        }
    }

    private boolean isPhoneExists(String phone) {
        try (Cursor cursor = db.rawQuery("SELECT Phone FROM users WHERE Phone=?", new String[]{phone})) {
            boolean exists = cursor.getCount() > 0;
            Log.d(TAG, "User exists: " + exists);
            return exists;
        } catch (Exception e) {
            Log.e(TAG, "Error checking user existence", e);
            return false;
        }
    }

    private boolean addUserToDatabase(String name, String phone, String email, String hashedPassword) {
        ContentValues values = new ContentValues();
        values.put("Name", name);
        values.put("Phone", phone);
        values.put("Email", email);
        values.put("Password", hashedPassword);
        values.put("Points", 0);

        try {
            long id = db.insert("users", null, values);
            Log.d(TAG, "User insert ID: " + id);
            return id != -1;
        } catch (Exception e) {
            Log.e(TAG, "Error adding user to database", e);
            return false;
        }
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
            Log.e(TAG, "Error hashing password", e);
            return null;
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        if (db != null && db.isOpen()) {
            db.close();
        }
    }
}
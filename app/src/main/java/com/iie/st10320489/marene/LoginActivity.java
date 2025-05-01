package com.iie.st10320489.marene;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.*;
import android.widget.ImageView;


import com.iie.st10320489.marene.data.database.AppDatabase;
import com.iie.st10320489.marene.data.entities.User;

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextView registerNow = findViewById(R.id.registerNowText);
        EditText emailEditText = findViewById(R.id.emailEditText);
        EditText passwordEditText = findViewById(R.id.passwordEditText);
        Button loginButton = findViewById(R.id.loginButton);

        TextView forgotPassword = findViewById(R.id.forgotPassword);

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this, "Kindly contact us at info@cashoo.com to reset password", Toast.LENGTH_LONG).show();
            }
        });

        ImageView togglePasswordVisibility = findViewById(R.id.togglePasswordVisibility);

        final boolean[] isPasswordVisible = {false};

        togglePasswordVisibility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPasswordVisible[0]) {
                    // Hide password
                    passwordEditText.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    togglePasswordVisibility.setImageResource(R.drawable.ic_eye_off); // eye closed
                } else {
                    // Show password
                    passwordEditText.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    togglePasswordVisibility.setImageResource(R.drawable.ic_eye); // eye open
                }

                // Move cursor to end
                passwordEditText.setSelection(passwordEditText.getText().length());

                // Toggle the state
                isPasswordVisible[0] = !isPasswordVisible[0];
            }
        });

        registerNow.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, SignupActivity.class));
        });

        loginButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Please enter both email and password", Toast.LENGTH_SHORT).show();
            } else {
                loginUser(email, password);
            }
        });
    }

    //clear table
    private void clearAllData() {
        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                        AppDatabase.class, "cashoo_database")
                .allowMainThreadQueries()
                .build();

        // Clear each table manually
        db.userDao().deleteAllTableName();
        db.userSettingsDao().deleteAllTableName();
        db.transactionDao().deleteAllTableName();
        db.categoryDao().deleteAllTableName();
        db.subCategoryDao().deleteAllTableName();
        db.rewardDao().deleteAllTableName();
        db.rewardHistoryDao().deleteAllTableName();
        db.activityLogDao().deleteAllTableName();

        Toast.makeText(this, "All data deleted successfully!", Toast.LENGTH_SHORT).show();
    }


    private void loginUser(String email, String password) {
        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                        AppDatabase.class, "cashoo_database")
                .allowMainThreadQueries()
                .build();
        User user = db.userDao().findUserByEmail(email); // you'll create this in your DAO

        if (user != null && user.getPassword().equals(password)) {
            // Save login status and email to SharedPreferences
            SharedPreferences sharedPreferences = getSharedPreferences("UserPreferences", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("currentUserEmail", email); // Store email
            editor.putBoolean("isLoggedIn", true); // Flag to check if the user is logged in
            editor.apply(); // Commit the changes

            Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();

            // Go to the Home Fragment
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra("USER_NAME", user.getName());
            startActivity(intent);
            finish(); // close LoginActivity so user can't press back to login
        } else {
            Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show();
        }
    }
}

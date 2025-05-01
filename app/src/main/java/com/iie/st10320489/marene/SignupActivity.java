package com.iie.st10320489.marene;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.text.InputType;
import android.widget.ImageView;


import androidx.room.*;


import androidx.appcompat.app.AppCompatActivity;

import com.iie.st10320489.marene.data.database.AppDatabase;
import com.iie.st10320489.marene.data.entities.User;
import com.iie.st10320489.marene.ui.onboarding.OnboardingActivity;
import com.iie.st10320489.marene.ui.onboarding.OnboardingActivity2;

import java.util.List;

public class SignupActivity extends AppCompatActivity {
    private boolean isPasswordStrong(String password) {
        return password.length() >= 8 &&
                password.matches(".*[A-Z].*") &&
                password.matches(".*[0-9].*") &&
                password.matches(".*[!@#\\$%^&*].*");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        AppDatabase db;
        db = Room.databaseBuilder(getApplicationContext(),
                        AppDatabase.class, "cashoo_database")
                .allowMainThreadQueries()
                .build();

        Button signupButton = findViewById(R.id.signupLoginButton);
        EditText nameEditText = findViewById(R.id.nameEditText);
        EditText surnameEditText = findViewById(R.id.surnameEditText);
        EditText emailEditText = findViewById(R.id.signupEmailEditText);
        EditText passwordEditText = findViewById(R.id.createPasswordEditText);
        EditText confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        // View bindings
        ImageView toggleCreatePassword = findViewById(R.id.toggleCreatePassword);
        ImageView toggleConfirmPassword = findViewById(R.id.toggleConfirmPassword);
        EditText createPasswordEditText = findViewById(R.id.createPasswordEditText);
        //EditText confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);

        final boolean[] isCreatePasswordVisible = {false};
        final boolean[] isConfirmPasswordVisible = {false};

        toggleCreatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCreatePasswordVisible[0]) {
                    createPasswordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    toggleCreatePassword.setImageResource(R.drawable.ic_eye_off);
                } else {
                    createPasswordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    toggleCreatePassword.setImageResource(R.drawable.ic_eye);
                }
                createPasswordEditText.setSelection(createPasswordEditText.length());
                isCreatePasswordVisible[0] = !isCreatePasswordVisible[0];
            }
        });

        toggleConfirmPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isConfirmPasswordVisible[0]) {
                    confirmPasswordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    toggleConfirmPassword.setImageResource(R.drawable.ic_eye_off);
                } else {
                    confirmPasswordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    toggleConfirmPassword.setImageResource(R.drawable.ic_eye);
                }
                confirmPasswordEditText.setSelection(confirmPasswordEditText.length());
                isConfirmPasswordVisible[0] = !isConfirmPasswordVisible[0];
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEditText.getText().toString();
                String surname = surnameEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String confirmPassword = confirmPasswordEditText.getText().toString();

                if (name.isEmpty() || surname.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(SignupActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(SignupActivity.this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
                } else if (!password.equals(confirmPassword)) {
                    Toast.makeText(SignupActivity.this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
                } else if (!isPasswordStrong(password)) {
                    Toast.makeText(SignupActivity.this, "Password is not complex enough. Use 8+ chars, uppercase, number, and symbol.", Toast.LENGTH_LONG).show();
                } else {

                    new Thread(() -> {
                        User newUser = new User(0, name, surname, email, password, 0.0, true);
                        db.userDao().insertUserNow(newUser);

                        SharedPreferences sharedPreferences = getSharedPreferences("UserPreferences", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("currentUserEmail", newUser.getEmail());
                        editor.putInt("currentUserId", newUser.getUserId());
                        editor.apply();

                        runOnUiThread(() -> {
                            Toast.makeText(SignupActivity.this, "Account created!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SignupActivity.this, OnboardingActivity.class);
                            startActivity(intent);
                            finish();
                        });
                    }).start();
                }
            }
        });




        TextView signInText = findViewById(R.id.signUpText);
        signInText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
            }
        });


    }

}

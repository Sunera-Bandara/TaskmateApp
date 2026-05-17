package lk.example.taskmate.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import lk.example.taskmate.databinding.ActivityLoginBinding;
import lk.example.taskmate.viewmodels.UserViewModel;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private UserViewModel viewModel;
    private static final String PREF_NAME = "user_prefs";
    private static final String KEY_ID = "user_id";
    private static final String KEY_NAME = "user_name";
    private static final String KEY_EMAIL = "user_email";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(UserViewModel.class);

        binding.signInButton.setOnClickListener(v -> {
            String username = binding.usernameEditText.getText().toString().trim();
            String password = binding.passwordEditText.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            viewModel.login(username, password);
        });

        viewModel.getLoginResult().observe(this, user -> {
            if (user != null) {
                SharedPreferences prefs = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
                prefs.edit()
                        .putInt(KEY_ID, user.getId())
                        .putString(KEY_NAME, user.getUsername())
                        .putString(KEY_EMAIL, user.getEmail())
                        .apply();

                startActivity(new Intent(this, TodoActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.getErrorResult().observe(this, error -> {
            if (error != null) {
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
            }
        });

        binding.signUpLink.setOnClickListener(v -> {
            startActivity(new Intent(this, SignupActivity.class));
        });

        binding.forgotPassword.setOnClickListener(v -> {
            Toast.makeText(this, "Feature coming soon!", Toast.LENGTH_SHORT).show();
        });
    }
}

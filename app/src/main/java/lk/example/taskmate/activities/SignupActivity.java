package lk.example.taskmate.activities;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import lk.example.taskmate.databinding.ActivitySignupBinding;
import lk.example.taskmate.models.User;
import lk.example.taskmate.viewmodels.UserViewModel;

public class SignupActivity extends AppCompatActivity {

    private ActivitySignupBinding binding;
    private UserViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(UserViewModel.class);

        binding.signInLink.setOnClickListener(v -> finish());
        
        binding.signUpButton.setOnClickListener(v -> {
            String username = binding.usernameEditText.getText().toString().trim();
            String email = binding.emailEditText.getText().toString().trim();
            String password = binding.passwordEditText.getText().toString().trim();
            String confirmPassword = binding.confirmPasswordEditText.getText().toString().trim();

            if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(confirmPassword)) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            User newUser = new User(username, email, password);
            viewModel.register(newUser);
        });

        viewModel.getRegistrationSuccess().observe(this, success -> {
            if (success != null && success) {
                Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        viewModel.getErrorResult().observe(this, error -> {
            if (error != null) {
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}

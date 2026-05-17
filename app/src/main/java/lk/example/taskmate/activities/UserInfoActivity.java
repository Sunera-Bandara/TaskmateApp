package lk.example.taskmate.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import lk.example.taskmate.R;
import lk.example.taskmate.database.AppDatabase;
import lk.example.taskmate.databinding.ActivityUserInfoBinding;
import lk.example.taskmate.models.User;

public class UserInfoActivity extends AppCompatActivity {

    private ActivityUserInfoBinding binding;
    private SharedPreferences prefs;
    private AppDatabase db;
    private static final String PREF_NAME = "user_prefs";
    private static final String KEY_ID = "user_id";
    private static final String KEY_NAME = "user_name";
    private static final String KEY_EMAIL = "user_email";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Enable Edge-to-Edge to allow blue background to flow behind status bar
        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);
        binding = ActivityUserInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Handle system bars properly without creating a white gap
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            
            // Apply top padding to the AppBarLayout only, so its blue background fills the status bar
            binding.appBar.setPadding(0, systemBars.top, 0, 0);
            
            // Apply bottom, left, and right padding to the root to keep content visible
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom);

            return insets;
        });

        db = AppDatabase.getInstance(this);
        prefs = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        binding.toolbar.setNavigationOnClickListener(v -> finish());

        // Load persisted data
        String name = prefs.getString(KEY_NAME, "");
        String email = prefs.getString(KEY_EMAIL, "");
        updateDisplay(name, email);

        binding.signOutButton.setOnClickListener(v -> binding.signOutDialogOverlay.setVisibility(View.VISIBLE));

        binding.dialogCancelSignout.setOnClickListener(v -> 
            binding.signOutDialogOverlay.setVisibility(View.GONE));
            
        binding.dialogOkSignout.setOnClickListener(v -> {
            // Clear preferences
            prefs.edit().clear().apply();
            
            // Go to LoginActivity and clear task stack
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        binding.editInfoButton.setOnClickListener(v -> {
            binding.editUsernameInput.setText(prefs.getString(KEY_NAME, ""));
            binding.editEmailInput.setText(prefs.getString(KEY_EMAIL, ""));
            binding.editDialogOverlay.setVisibility(View.VISIBLE);
        });

        binding.editCancel.setOnClickListener(v -> binding.editDialogOverlay.setVisibility(View.GONE));
        binding.editOk.setOnClickListener(v -> {
            String newName = binding.editUsernameInput.getText().toString().trim();
            String newEmail = binding.editEmailInput.getText().toString().trim();
            
            if (newName.isEmpty() || newEmail.isEmpty()) {
                Toast.makeText(this, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }

            int userId = prefs.getInt(KEY_ID, -1);
            if (userId != -1) {
                // Update Database on background thread
                AppDatabase.databaseWriteExecutor.execute(() -> {
                    User user = db.userDao().getUserById(userId);
                    if (user != null) {
                        user.setUsername(newName);
                        user.setEmail(newEmail);
                        db.userDao().updateUser(user);

                        // Update UI and SharedPreferences on main thread
                        runOnUiThread(() -> {
                            prefs.edit()
                                .putString(KEY_NAME, newName)
                                .putString(KEY_EMAIL, newEmail)
                                .apply();

                            updateDisplay(newName, newEmail);
                            Toast.makeText(UserInfoActivity.this, "Profile updated", Toast.LENGTH_SHORT).show();
                            binding.editDialogOverlay.setVisibility(View.GONE);
                        });
                    }
                });
            }
        });
    }

    private void updateDisplay(String name, String email) {
        binding.usernameDisplay.setText(getString(R.string.username_template, name.isEmpty() ? "Not set" : name));
        binding.emailDisplay.setText(getString(R.string.email_template, email.isEmpty() ? "Not set" : email));
    }
}

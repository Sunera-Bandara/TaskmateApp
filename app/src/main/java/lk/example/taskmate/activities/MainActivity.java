package lk.example.taskmate.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;
import lk.example.taskmate.databinding.ActivitySplashBinding;

public class MainActivity extends AppCompatActivity {

    private ActivitySplashBinding binding;
    private static final String PREF_NAME = "user_prefs";
    private static final String KEY_NAME = "user_name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPreferences prefs = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        boolean isLoggedIn = prefs.contains(KEY_NAME);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (isLoggedIn) {
                startActivity(new Intent(MainActivity.this, TodoActivity.class));
            } else {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
            finish();
        }, 2000);
    }
}

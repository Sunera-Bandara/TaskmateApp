package lk.example.taskmate.activities;

import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import lk.example.taskmate.R;
import lk.example.taskmate.databinding.ActivityDevInfoBinding;

public class DevInfoActivity extends AppCompatActivity {

    private ActivityDevInfoBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Enable Edge-to-Edge to allow background to flow behind system bars
        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);
        binding = ActivityDevInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Handle system bars properly
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            
            // Push AppBar content down into the safe area
            binding.appBar.setPadding(0, systemBars.top, 0, 0);
            
            // Apply bottom padding to the root to keep buttons above the navigation bar
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom);
            
            return insets;
        });

        binding.toolbar.setNavigationOnClickListener(v -> finish());
        binding.exitButton.setOnClickListener(v -> finish());

        // Set version info using template string
        binding.versionChip.setText(getString(R.string.release_version_template, "1.0.0"));
    }
}

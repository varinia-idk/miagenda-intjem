package com.example.miagendaintjem;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public final class MainActivity extends AppCompatActivity {
    private TextView welcomeText;
    private Button openAgendaButton;
    private Button aboutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bindViews();
        applyResources();
        configureActions();
    }

    private void bindViews() {
        welcomeText = findViewById(R.id.welcomeText);
        openAgendaButton = findViewById(R.id.openAgendaButton);
        aboutButton = findViewById(R.id.aboutButton);
    }

    /** Demonstrates how Java resolves string, color and dimension resources. */
    private void applyResources() {
        String welcomeMessage = getString(R.string.welcome_message);
        int textColor = ContextCompat.getColor(this, R.color.md_on_surface);
        int padding = getResources().getDimensionPixelSize(R.dimen.spacing_medium);

        welcomeText.setText(welcomeMessage);
        welcomeText.setTextColor(textColor);
        welcomeText.setPadding(padding, padding, padding, padding);
    }

    private void configureActions() {
        openAgendaButton.setOnClickListener(view ->
                Toast.makeText(this, R.string.opening_agenda_message, Toast.LENGTH_SHORT).show());

        aboutButton.setOnClickListener(view -> {
            String message = getString(
                    R.string.about_message,
                    getString(R.string.app_name)
            );
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        });
    }
}

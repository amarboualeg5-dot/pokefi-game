package com.example.pokefi;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CaptureActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture);

        TextView title = findViewById(R.id.captureTitle);
        TextView details = findViewById(R.id.captureDetails);
        TextView status = findViewById(R.id.captureStatus);
        Button throwButton = findViewById(R.id.throwButton);

        final String name = getIntent().getStringExtra("name");
        final String rarity = getIntent().getStringExtra("rarity");
        final String element = getIntent().getStringExtra("element");
        final String security = getIntent().getStringExtra("security");
        final int signal = getIntent().getIntExtra("signal", 0);
        final int frequency = getIntent().getIntExtra("frequency", 0);
        final String ssid = getIntent().getStringExtra("ssid");

        title.setText(name == null ? "Unknown Monster" : name);
        details.setText(
                "Rarity: " + safe(rarity) +
                "
Element: " + safe(element) +
                "
Security: " + safe(security) +
                "
Signal: " + signal + " dBm" +
                "
Frequency: " + frequency + " MHz" +
                "
SSID: " + safe(ssid)
        );
        status.setText("Tap the Pokéball to capture.");

        throwButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                throwButton.setEnabled(false);
                throwButton.animate()
                        .rotationBy(360f)
                        .scaleX(1.15f)
                        .scaleY(1.15f)
                        .setDuration(350)
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                throwButton.animate()
                                        .scaleX(1f)
                                        .scaleY(1f)
                                        .setDuration(150)
                                        .start();
                                status.setText("Gotcha!");
                                throwButton.setText("Caught!");
                            }
                        })
                        .start();
            }
        });
    }

    private String safe(String value) {
        return value == null ? "-" : value;
    }
}

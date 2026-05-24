package com.example.pokefi;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class CaptureActivity extends Activity {

    private TextView monsterInfo;
    private Button catchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture);

        monsterInfo = findViewById(R.id.monsterInfo);
        catchButton = findViewById(R.id.catchButton);

        String name = getIntent().getStringExtra("name");
        String rarity = getIntent().getStringExtra("rarity");
        String element = getIntent().getStringExtra("element");
        String security = getIntent().getStringExtra("security");
        int signal = getIntent().getIntExtra("signal", 0);
        int frequency = getIntent().getIntExtra("frequency", 0);
        String ssid = getIntent().getStringExtra("ssid");

        String info =
                "Monster: " + safe(name) +
                "\nRarity: " + safe(rarity) +
                "\nElement: " + safe(element) +
                "\nSecurity: " + safe(security) +
                "\nSignal: " + signal + " dBm" +
                "\nFrequency: " + frequency + " MHz" +
                "\nSSID: " + safe(ssid);

        monsterInfo.setText(info);

        catchButton.setOnClickListener(v -> {

            ScaleAnimation anim = new ScaleAnimation(
                    1f, 1.3f,
                    1f, 1.3f,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f
            );

            anim.setDuration(200);
            anim.setRepeatMode(Animation.REVERSE);
            anim.setRepeatCount(1);

            catchButton.startAnimation(anim);

            new Handler().postDelayed(() -> {
                Toast.makeText(
                        CaptureActivity.this,
                        "Gotcha! " + safe(name) + " was captured!",
                        Toast.LENGTH_LONG
                ).show();
            }, 400);
        });
    }

    private String safe(String s) {
        return s == null ? "Unknown" : s;
    }
}

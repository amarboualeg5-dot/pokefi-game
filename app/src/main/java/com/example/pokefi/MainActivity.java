package com.example.pokefi;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends Activity {
    private static final int REQ_PERMISSIONS = 1001;

    private WifiManager wifiManager;
    private LocationManager locationManager;
    private TextView statusText;
    private MonsterAdapter adapter;
    private final ArrayList<Monster> monsters = new ArrayList<>();
    private boolean receiverRegistered = false;

    private final BroadcastReceiver scanReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            showScanResults();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        Button scanButton = findViewById(R.id.scanButton);
        statusText = findViewById(R.id.statusText);
        ListView listView = findViewById(R.id.monsterList);

        adapter = new MonsterAdapter(this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Monster monster = monsters.get(position);
                Intent capture = new Intent(MainActivity.this, CaptureActivity.class);
                capture.putExtra("name", monster.name);
                capture.putExtra("rarity", monster.rarity);
                capture.putExtra("element", monster.element);
                capture.putExtra("security", monster.security);
                capture.putExtra("signal", monster.signal);
                capture.putExtra("frequency", monster.frequency);
                capture.putExtra("ssid", monster.ssid);
                startActivity(capture);
            }
        });

        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!hasRequiredPermissions()) {
                    requestRequiredPermissions();
                    return;
                }
                scanForMonsters();
            }
        });

        if (savedInstanceState == null) {
            statusText.setText("Ready. Tap Scan for Monsters.");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!receiverRegistered) {
            registerScanReceiver();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterScanReceiver();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterScanReceiver();
    }

    private void registerScanReceiver() {
        if (receiverRegistered) {
            return;
        }
        IntentFilter filter = new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        if (Build.VERSION.SDK_INT >= 33) {
            registerReceiver(scanReceiver, filter, Context.RECEIVER_NOT_EXPORTED);
        } else {
            registerReceiver(scanReceiver, filter);
        }
        receiverRegistered = true;
    }

    private void unregisterScanReceiver() {
        if (!receiverRegistered) {
            return;
        }
        unregisterReceiver(scanReceiver);
        receiverRegistered = false;
    }

    private boolean hasRequiredPermissions() {
        boolean locationOk = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == android.content.pm.PackageManager.PERMISSION_GRANTED;
        if (Build.VERSION.SDK_INT >= 33) {
            boolean nearbyOk = checkSelfPermission(Manifest.permission.NEARBY_WIFI_DEVICES) == android.content.pm.PackageManager.PERMISSION_GRANTED;
            return locationOk && nearbyOk;
        }
        return locationOk;
    }

    private void requestRequiredPermissions() {
        if (Build.VERSION.SDK_INT >= 33) {
            requestPermissions(
                    new String[] {
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.NEARBY_WIFI_DEVICES
                    },
                    REQ_PERMISSIONS
            );
        } else {
            requestPermissions(
                    new String[] { Manifest.permission.ACCESS_FINE_LOCATION },
                    REQ_PERMISSIONS
            );
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode != REQ_PERMISSIONS) {
            return;
        }
        if (hasRequiredPermissions()) {
            scanForMonsters();
        } else {
            statusText.setText("Permission denied. Wi‑Fi scans need location access.");
        }
    }

    private void scanForMonsters() {
        if (locationManager != null && Build.VERSION.SDK_INT >= 28 && !locationManager.isLocationEnabled()) {
            statusText.setText("Turn on Location Services, then tap Scan again.");
            return;
        }

        registerScanReceiver();
        statusText.setText("Scanning nearby Wi‑Fi monsters...");

        boolean started = wifiManager != null && wifiManager.startScan();
        if (!started) {
            showScanResults();
        } else {
            // Show the most recent cached results immediately, then refresh again when the broadcast arrives.
            showScanResults();
        }
    }

    private void showScanResults() {
        if (wifiManager == null) {
            statusText.setText("Wi‑Fi system service not available.");
            return;
        }

        List<ScanResult> results = wifiManager.getScanResults();
        ArrayList<Monster> found = new ArrayList<>();
        Set<String> seen = new HashSet<>();

        for (ScanResult result : results) {
            String key = result.BSSID == null ? result.SSID : result.BSSID;
            if (seen.contains(key)) {
                continue;
            }
            seen.add(key);
            found.add(Monster.fromScanResult(result));
        }

        found.sort(new Comparator<Monster>() {
            @Override
            public int compare(Monster a, Monster b) {
                int rarity = rarityScore(b.rarity) - rarityScore(a.rarity);
                if (rarity != 0) {
                    return rarity;
                }
                return b.signal - a.signal;
            }
        });

        monsters.clear();
        monsters.addAll(found);
        adapter.setItems(found);

        if (found.isEmpty()) {
            statusText.setText("No monsters found. Try scanning again or move near more Wi‑Fi.");
        } else {
            statusText.setText("Found " + found.size() + " monsters.");
        }
    }

    private int rarityScore(String rarity) {
        if ("Legendary".equals(rarity)) return 5;
        if ("Epic".equals(rarity)) return 4;
        if ("Rare".equals(rarity)) return 3;
        if ("Uncommon".equals(rarity)) return 2;
        return 1;
    }
}

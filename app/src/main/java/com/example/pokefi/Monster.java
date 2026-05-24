package com.example.pokefi;

import android.net.wifi.ScanResult;

import java.util.Locale;

public class Monster {
    public final String ssid;
    public final String name;
    public final String rarity;
    public final String element;
    public final String security;
    public final int level;
    public final int signal;
    public final int frequency;

    public Monster(String ssid, String name, String rarity, String element, String security, int level, int signal, int frequency) {
        this.ssid = ssid;
        this.name = name;
        this.rarity = rarity;
        this.element = element;
        this.security = security;
        this.level = level;
        this.signal = signal;
        this.frequency = frequency;
    }

    public static Monster fromScanResult(ScanResult result) {
        String ssid = normalizeSsid(result.SSID);
        String security = readSecurity(result.capabilities);
        int signal = result.level;
        int frequency = result.frequency;
        String rarity = readRarity(security, signal);
        String element = readElement(frequency, security);
        String name = readMonsterName(ssid, security, signal, frequency, rarity);

        int level = Math.max(1, Math.min(50, 101 + signal));
        return new Monster(ssid, name, rarity, element, security, level, signal, frequency);
    }

    private static String normalizeSsid(String ssid) {
        if (ssid == null) {
            return "Hidden Network";
        }
        String cleaned = ssid.trim();
        if (cleaned.isEmpty() || "<unknown ssid>".equalsIgnoreCase(cleaned)) {
            return "Hidden Network";
        }
        return cleaned;
    }

    private static String readSecurity(String capabilities) {
        String caps = capabilities == null ? "" : capabilities.toUpperCase(Locale.US);
        if (caps.contains("WPA3")) return "WPA3";
        if (caps.contains("WPA2")) return "WPA2";
        if (caps.contains("WPA")) return "WPA";
        if (caps.contains("WEP")) return "WEP";
        return "OPEN";
    }

    private static String readRarity(String security, int signal) {
        if ("WPA3".equals(security) && signal >= -65) return "Legendary";
        if ("WPA3".equals(security)) return "Epic";
        if ("WPA2".equals(security) && signal >= -60) return "Rare";
        if ("WPA2".equals(security)) return "Uncommon";
        if ("WPA".equals(security)) return signal >= -55 ? "Rare" : "Uncommon";
        if ("WEP".equals(security)) return "Uncommon";
        return "Common";
    }

    private static String readElement(int frequency, String security) {
        if (frequency >= 5925) return "Psychic";
        if (frequency >= 5000) return "Lightning";
        if (frequency >= 2400) return "Earth";
        if ("OPEN".equals(security)) return "Air";
        return "Mystic";
    }

    private static String readMonsterName(String ssid, String security, int signal, int frequency, String rarity) {
        String lower = ssid.toLowerCase(Locale.US);

        if (lower.contains("starbucks") || lower.contains("coffee") || lower.contains("cafe")) {
            return "Caffeine Sprite";
        }
        if (lower.contains("guest")) {
            return "Guest Goblin";
        }
        if (lower.contains("hotel")) {
            return "Lobby Phantom";
        }
        if (lower.contains("airport")) {
            return "Terminal Titan";
        }
        if (lower.contains("linksys") || lower.contains("netgear") || lower.contains("tplink") || lower.contains("router")) {
            if ("WPA3".equals(security) && signal >= -65) {
                return "Mega Ancient Router Dragon";
            }
            return "Router Golem";
        }
        if (lower.contains("xfinity") || lower.contains("comcast") || lower.contains("verizon")) {
            return "Signal Kraken";
        }
        if (lower.contains("home") || lower.contains("house")) {
            return "Hearth Sprite";
        }
        if (lower.contains("school") || lower.contains("campus")) {
            return "Campus Chimera";
        }

        String prefix;
        if ("Legendary".equals(rarity)) prefix = "Ancient";
        else if ("Epic".equals(rarity)) prefix = "Mega";
        else if ("Rare".equals(rarity)) prefix = "Storm";
        else if ("Uncommon".equals(rarity)) prefix = "Swift";
        else prefix = "Tiny";

        String core;
        if (frequency >= 5925) {
            core = "Oracle";
        } else if (frequency >= 5000) {
            core = "Dragon";
        } else if (frequency > 0) {
            core = "Sprite";
        } else {
            core = "Wisp";
        }

        if ("OPEN".equals(security)) {
            return prefix + " Free " + core;
        }
        return prefix + " " + core;
    }
}

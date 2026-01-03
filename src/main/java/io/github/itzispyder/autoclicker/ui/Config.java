package io.github.itzispyder.autoclicker.ui;

import io.github.itzispyder.autoclicker.Global;

import java.util.HashMap;
import java.util.Map;

public class Config implements Global {

    private static final Map<String, Object> CONFIG = new HashMap<>();

    public static boolean left, right;
    public static boolean leftSpam, rightSpam;
    public static boolean leftOnlyHold, rightOnlyHold;
    public static int leftCps, rightCps;
    public static double leftChance, rightChance;

    public static boolean onlyWhenTarget;
    public static boolean noBabies;
    public static double maxAttackCooldown;
    public static boolean stopWhenMove;
    public static boolean stopWhenDamage;
    public static boolean stopWhenTarget;
    public static boolean debug;

    public static void update() {
        left = readBool("left", false);
        right = readBool("right", false);
        leftSpam = readBool("left-spam", true);
        rightSpam = readBool("right-spam", true);
        leftOnlyHold = readBool("left-hold", true);
        rightOnlyHold = readBool("right-hold", true);
        leftCps = readInt("left-cps", 10);
        rightCps = readInt("right-cps", 10);
        leftChance = readDouble("left-chance", 1.0);
        rightChance = readDouble("right-chance", 1.0);

        onlyWhenTarget = readBool("only-when-target", false);
        noBabies = readBool("no-babies", true);
        maxAttackCooldown = readDouble("max-attack-cooldown", 0.0);
        stopWhenMove = readBool("stop-when-move", false);
        stopWhenDamage = readBool("stop-when-damage", false);
        stopWhenTarget = readBool("stop-when-target", false);
        debug = readBool("debug", false);
    }

    /* ---------- simple config helpers ---------- */

    private static Object read(String key) {
        return CONFIG.get(key);
    }

    private static void write(String key, Object value) {
        CONFIG.put(key, value);
    }

    private static boolean readBool(String key, boolean def) {
        Object v = read(key);
        if (v == null) {
            write(key, def);
            return def;
        }
        return (boolean) v;
    }

    private static int readInt(String key, int def) {
        Object v = read(key);
        if (v == null) {
            write(key, def);
            return def;
        }
        return (int) v;
    }

    private static double readDouble(String key, double def) {
        Object v = read(key);
        if (v == null) {
            write(key, def);
            return def;
        }
        return (double) v;
    }
}
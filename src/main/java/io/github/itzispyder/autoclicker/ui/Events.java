package io.github.itzispyder.autoclicker.ui;

import io.github.itzispyder.autoclicker.Global;
import io.github.itzispyder.autoclicker.mixin.AccessorMinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.text.Text;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;

import java.util.Arrays;

public class Events implements Global {

    public static boolean leftToggle, rightToggle;
    public static Vec3d prevPos = Vec3d.ZERO;
    public static float prevHp;
    public static int tickLeft, tickRight;
    public static int[] noiseMapLeft = new int[20];
    public static int[] noiseMapRight = new int[20];

    public static void distributeNoise(int cps, double clickChance, int[] noiseMap) {
        float chance = cps / 400.0F;
        int distributions = 0;

        Arrays.fill(noiseMap, 0);

        while (distributions < cps) {
            int randomIndex = Math.max(0, Math.min(19, (int)(Math.random() * 20)));

            if (distributions < 20 && noiseMap[randomIndex] > 0)
                continue;
            if (Math.random() > chance * clickChance)
                continue;

            noiseMap[randomIndex]++;
            distributions++;
        }
    }

    public static void onTick() {
        if (!canClick())
            return;

        if (Config.left) {
            leftToggle = true;
            clickLeft();
        } else if (leftToggle) {
            leftToggle = false;
            mc.options.attackKey.setPressed(false);
        }

        if (Config.right) {
            rightToggle = true;
            clickRight();
        } else if (rightToggle) {
            rightToggle = false;
            mc.options.useKey.setPressed(false);
        }
    }

    private static void clickLeft() {
        if (!Config.leftSpam) {
            mc.options.attackKey.setPressed(true);
            return;
        }

        for (int i = 0; i < noiseMapLeft[tickLeft]; i++)
            if (Math.random() <= Config.leftChance)
                getInput().leftClick();

        if (++tickLeft >= 20) {
            tickLeft = 0;
            distributeNoise(Config.leftCps, Config.leftChance, noiseMapLeft);
        }
    }

    private static void clickRight() {
        if (!Config.rightSpam) {
            mc.options.useKey.setPressed(true);
            return;
        }

        for (int i = 0; i < noiseMapRight[tickRight]; i++)
            if (Math.random() <= Config.rightChance)
                getInput().rightClick();

        if (++tickRight >= 20) {
            tickRight = 0;
            distributeNoise(Config.rightCps, Config.rightChance, noiseMapRight);
        }
    }

    private static boolean canClick() {
        if (mc.player == null || mc.world == null)
            return false;

        var p = mc.player;
        boolean noTarget = true;
        boolean isBaby = false;

        if (mc.crosshairTarget instanceof EntityHitResult hit) {
            noTarget = false;
            isBaby = hit.getEntity() instanceof LivingEntity liv && liv.isBaby();
        }

        prevHp = p.getHealth();
        prevPos = p.getPos();

        if (Config.onlyWhenTarget && noTarget) return false;
        if (Config.noBabies && isBaby) return false;
        if (Config.maxAttackCooldown > 0 &&
                p.getAttackCooldownProgress(1.0F) < Config.maxAttackCooldown)
            return false;

        return !Config.stopWhenTarget || noTarget;
    }

    public static AccessorMinecraftClient getInput() {
        return (AccessorMinecraftClient) mc;
    }

    public static void send(String msg) {
        if (mc.player != null)
            mc.player.sendMessage(Text.literal("[Autoclicker] " + msg), false);
    }
}
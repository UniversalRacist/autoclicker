package io.github.itzispyder.autoclicker;

import io.github.itzispyder.autoclicker.ui.AutoClickerConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;

public class AutoClicker implements ClientModInitializer {

    private int tickCounter = 0;

    @Override
    public void onInitializeClient() {
        AutoClickerConfig.load();

        ClientTickEvents.END_CLIENT_TICK.register(this::onTick);
    }

    private void onTick(MinecraftClient client) {
        if (client.player == null || client.world == null) return;
        if (!AutoClickerConfig.INSTANCE.enabled) return;

        int cps = AutoClickerConfig.INSTANCE.cps;
        if (cps <= 0) return;

        int interval = Math.max(1, 20 / cps);
        tickCounter++;

        if (tickCounter >= interval) {
            tickCounter = 0;

            // ✅ SAFE ATTACK SIMULATION (1.21+)
            client.options.attackKey.setPressed(true);
            client.options.attackKey.setPressed(false);
        }
    }
}
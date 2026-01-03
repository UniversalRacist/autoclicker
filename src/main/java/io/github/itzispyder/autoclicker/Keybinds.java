package io.github.itzispyder.autoclicker;

import io.github.itzispyder.autoclicker.ui.AutoClickerConfig;
import io.github.itzispyder.autoclicker.ui.AutoClickerScreen;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import org.lwjgl.glfw.GLFW;

public class Keybinds {

    private static KeyBinding toggleKey;
    private static KeyBinding guiKey;

    public static void init() {

        // Toggle autoclicker (X)
        toggleKey = KeyBindingHelper.registerKeyBinding(
                new KeyBinding(
                        "Toggle AutoClicker",
                        GLFW.GLFW_KEY_X,
                        "Universal AutoClicker"
                )
        );

        // Open GUI (0)
        guiKey = KeyBindingHelper.registerKeyBinding(
                new KeyBinding(
                        "Open AutoClicker GUI",
                        GLFW.GLFW_KEY_0,
                        "Universal AutoClicker"
                )
        );

        ClientTickEvents.END_CLIENT_TICK.register(client -> {

            while (toggleKey.wasPressed()) {
                AutoClickerConfig.INSTANCE.enabled =
                        !AutoClickerConfig.INSTANCE.enabled;
                AutoClickerConfig.save();
            }

            while (guiKey.wasPressed()) {
                MinecraftClient.getInstance()
                        .setScreen(new AutoClickerScreen());
            }
        });
    }
}
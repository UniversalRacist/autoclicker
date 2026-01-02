package io.github.itzispyder.autoclicker;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Hand;
import org.lwjgl.glfw.GLFW;

public class AutoClicker implements ClientModInitializer {
    public static final String MOD_ID = "autoclicker";
    private static KeyBinding toggleKey;
    public static boolean enabled = false;
    public static int cps = 12;
    private static long lastClickTime = 0;
    private static boolean shouldClick = false;

    @Override
    public void onInitializeClient() {
        // 1. Register Keybind
        toggleKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.autoclicker.toggle",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_0,
            net.minecraft.client.option.KeyBinding.Category.MISC
        ));

        // 2. Register Tick Event
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) return;

            // Toggle when key is pressed
            while (toggleKey.wasPressed()) {
                enabled = !enabled;
            }

            // Handle clicking logic
            if (enabled) {
                long currentTime = System.currentTimeMillis();
                long delay = 1000 / cps; // Calculate delay in milliseconds

                if (currentTime - lastClickTime >= delay) {
                    shouldClick = true;
                    lastClickTime = currentTime;
                }
            }
            
            // Perform the click if scheduled
            if (shouldClick) {
                performLeftClick(client);
                shouldClick = false;
            }
        });

    /*    // 3. Register HUD Render Event - WITH SAFETY CHECKS
        HudRenderCallback.EVENT.register((drawContext, tickDelta) -> {
            if (enabled) {
                MinecraftClient client = MinecraftClient.getInstance();
                // Null check to prevent crashes
                if (client == null || client.textRenderer == null) return;
                
                String text = "AutoClicker [ON] (" + cps + " CPS)";
                net.minecraft.client.font.TextRenderer textRenderer = client.textRenderer;
                int textWidth = textRenderer.getWidth(text);
                int x = drawContext.getScaledWindowWidth() - textWidth - 5;
                int y = 5;
                drawContext.drawTextWithShadow(textRenderer, text, x, y, 0x00FF00);
            }
      });*/
    } // <-- This correctly closes onInitializeClient()

    private void performLeftClick(MinecraftClient client) {
        // Check if we're looking at something we can interact with
        if (client.crosshairTarget != null && client.interactionManager != null) {
            // Perform the attack/interaction
            if (client.crosshairTarget.getType() == net.minecraft.util.hit.HitResult.Type.ENTITY) {
                // Attack entity
                client.interactionManager.attackEntity(client.player, 
                    ((net.minecraft.util.hit.EntityHitResult) client.crosshairTarget).getEntity());
                client.player.swingHand(Hand.MAIN_HAND);
            } else if (client.crosshairTarget.getType() == net.minecraft.util.hit.HitResult.Type.BLOCK) {
                // Mine block (start breaking)
                net.minecraft.util.hit.BlockHitResult blockHit = (net.minecraft.util.hit.BlockHitResult) client.crosshairTarget;
                if (client.world.getBlockState(blockHit.getBlockPos()).isAir()) return;
                
                client.interactionManager.updateBlockBreakingProgress(blockHit.getBlockPos(), blockHit.getSide());
                client.player.swingHand(Hand.MAIN_HAND);
            }
        }
    }
} // <-- This correctly closes the class
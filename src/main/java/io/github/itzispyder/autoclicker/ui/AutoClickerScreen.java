package io.github.itzispyder.autoclicker.ui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;

public class AutoClickerScreen extends Screen {

    public AutoClickerScreen() {
        super(Text.literal("Universal AutoClicker"));
    }

    @Override
    protected void init() {
        int cx = width / 2;
        int y = height / 4 + 24;

        addDrawableChild(enabledButton(cx, y));
        y += 34;

        addDrawableChild(cpsSlider(cx, y));
        y += 40;

        addDrawableChild(modeButton(cx, y));
        y += 34;

        addDrawableChild(floatingButton(cx, y));
    }

    // ================= BUTTONS =================

    private ButtonWidget enabledButton(int x, int y) {
        return ButtonWidget.builder(
                Text.literal("🟡 Enabled: " + AutoClickerConfig.INSTANCE.enabled),
                b -> {
                    AutoClickerConfig.INSTANCE.enabled = !AutoClickerConfig.INSTANCE.enabled;
                    AutoClickerConfig.save();
                    b.setMessage(Text.literal("🟡 Enabled: " + AutoClickerConfig.INSTANCE.enabled));
                }
        ).dimensions(x - 110, y, 220, 24).build();
    }

    private ButtonWidget modeButton(int x, int y) {
        return ButtonWidget.builder(
                Text.literal("🎮 Mode: " + AutoClickerConfig.INSTANCE.mode),
                b -> {
                    AutoClickerConfig.INSTANCE.mode = switch (AutoClickerConfig.INSTANCE.mode) {
                        case "COMBAT" -> "MINING";
                        case "MINING" -> "BOTH";
                        default -> "COMBAT";
                    };
                    AutoClickerConfig.save();
                    b.setMessage(Text.literal("🎮 Mode: " + AutoClickerConfig.INSTANCE.mode));
                }
        ).dimensions(x - 110, y, 220, 24).build();
    }

    private ButtonWidget floatingButton(int x, int y) {
        return ButtonWidget.builder(
                Text.literal("📱 Floating Button: " + AutoClickerConfig.INSTANCE.floatingButton),
                b -> {
                    AutoClickerConfig.INSTANCE.floatingButton =
                            !AutoClickerConfig.INSTANCE.floatingButton;
                    AutoClickerConfig.save();
                    b.setMessage(Text.literal(
                            "📱 Floating Button: " + AutoClickerConfig.INSTANCE.floatingButton
                    ));
                }
        ).dimensions(x - 110, y, 220, 24).build();
    }

    // ================= CPS SLIDER =================

    private SliderWidget cpsSlider(int x, int y) {
        return new SliderWidget(
                x - 110,
                y,
                220,
                24,
                Text.empty(),
                (AutoClickerConfig.INSTANCE.cps - 1) / 49.0
        ) {
            @Override
            protected void updateMessage() {
                setMessage(Text.literal("⚡ CPS: " + AutoClickerConfig.INSTANCE.cps));
            }

            @Override
            protected void applyValue() {
                AutoClickerConfig.INSTANCE.cps =
                        1 + (int) Math.round(value * 49);
                AutoClickerConfig.save();
            }
        };
    }

    // ================= RENDER =================

    @Override
    public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {

        // background
        ctx.fill(0, 0, width, height, 0xFF120E06);

        // panel
        int panelW = 260;
        int panelH = 240;
        int px = (width - panelW) / 2;
        int py = height / 4 - 12;

        ctx.fill(px, py, px + panelW, py + panelH, 0xFF1F180A);

        // title
        ctx.drawCenteredTextWithShadow(
                textRenderer,
                "Universal AutoClicker",
                width / 2,
                py + 10,
                0xFFFFC107
        );

        super.render(ctx, mouseX, mouseY, delta);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
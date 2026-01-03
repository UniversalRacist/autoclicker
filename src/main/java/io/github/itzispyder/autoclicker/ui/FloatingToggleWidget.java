package io.github.itzispyder.autoclicker.ui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.screen.narration.NarrationPart;
import net.minecraft.text.Text;

public class FloatingToggleWidget extends ClickableWidget {

    public FloatingToggleWidget(int x, int y) {
        super(x, y, 20, 20, Text.literal("AC"));
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        MinecraftClient mc = MinecraftClient.getInstance();

        // background
        context.fill(
                getX(),
                getY(),
                getX() + width,
                getY() + height,
                0x90000000
        );

        // text
        context.drawCenteredTextWithShadow(
                mc.textRenderer,
                "AC",
                getX() + width / 2,
                getY() + 6,
                0xFFFFFF
        );
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        AutoClickerConfig.INSTANCE.enabled = !AutoClickerConfig.INSTANCE.enabled;
        AutoClickerConfig.save();
    }

    // REQUIRED in 1.21+
    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {
        builder.put(
                NarrationPart.TITLE,
                Text.literal("AutoClicker Toggle")
        );
    }
}
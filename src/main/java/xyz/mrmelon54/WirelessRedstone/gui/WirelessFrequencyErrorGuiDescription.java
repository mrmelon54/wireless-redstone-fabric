package xyz.mrmelon54.WirelessRedstone.gui;

import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.TranslatableText;

public class WirelessFrequencyErrorGuiDescription extends io.github.cottonmc.cotton.gui.client.LightweightGuiDescription {
    public WirelessFrequencyErrorGuiDescription() {
        WPlainPanel root = new WPlainPanel();
        setRootPanel(root);
        root.setSize(160, 55);
        root.setInsets(Insets.ROOT_PANEL);

        WLabel wLabel = new WLabel(new TranslatableText("screen.wireless-redstone.error_message"));
        root.add(wLabel, 0, 18);

        WButton wButton = new WButton();
        wButton.setLabel(new TranslatableText("screen.wireless-redstone.ok"));
        wButton.setOnClick(() -> {
            MinecraftClient.getInstance().setScreen(null);
        });
        root.add(wButton, 0, 36, 160, 20);

        root.validate(this);
    }
}

package xyz.mrmelon54.wirelessredstone.screen;

import io.github.cottonmc.cotton.gui.client.CottonClientScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.TranslatableText;
import xyz.mrmelon54.wirelessredstone.gui.WirelessFrequencyErrorGuiDescription;


public class WirelessFrequencyErrorScreen extends CottonClientScreen {
    public WirelessFrequencyErrorScreen(Screen parent) {
        super(new TranslatableText("screen.wireless_redstone.error"), new WirelessFrequencyErrorGuiDescription(parent));
    }
}

package xyz.mrmelon54.WirelessRedstone.screen;

import io.github.cottonmc.cotton.gui.client.CottonClientScreen;
import net.minecraft.text.TranslatableText;
import xyz.mrmelon54.WirelessRedstone.gui.WirelessFrequencyErrorGuiDescription;


public class WirelessFrequencyErrorScreen extends CottonClientScreen {
    public WirelessFrequencyErrorScreen() {
        super(new TranslatableText("screen.wireless-redstone.error"), new WirelessFrequencyErrorGuiDescription());
    }
}

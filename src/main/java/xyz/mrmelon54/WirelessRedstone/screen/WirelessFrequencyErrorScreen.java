package xyz.mrmelon54.WirelessRedstone.screen;

import io.github.cottonmc.cotton.gui.client.CottonClientScreen;
import net.minecraft.text.Text;
import xyz.mrmelon54.WirelessRedstone.gui.WirelessFrequencyErrorGuiDescription;


public class WirelessFrequencyErrorScreen extends CottonClientScreen {
    public WirelessFrequencyErrorScreen() {
        super(Text.translatable("screen.wireless-redstone.error"), new WirelessFrequencyErrorGuiDescription());
    }
}

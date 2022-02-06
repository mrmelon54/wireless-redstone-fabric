package xyz.mrmelon54.wirelessredstone.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.minecraft.screen.ScreenHandlerType;
import xyz.mrmelon54.wirelessredstone.WirelessRedstone;
import xyz.mrmelon54.wirelessredstone.gui.WirelessFrequencyGuiDescription;
import xyz.mrmelon54.wirelessredstone.screen.WirelessFrequencyScreen;

@Environment(EnvType.CLIENT)
public class WirelessRedstoneClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        register(WirelessRedstone.WIRELESS_FREQUENCY_SCREEN, (gui, inventory, title) -> new WirelessFrequencyScreen(gui, inventory.player, title));
    }

    void register(ScreenHandlerType<WirelessFrequencyGuiDescription> guiDescription, ScreenRegistry.Factory<WirelessFrequencyGuiDescription, WirelessFrequencyScreen> screenFactory) {
        ScreenRegistry.register(guiDescription, screenFactory);
    }
}

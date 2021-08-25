package net.onpointcoding.wirelessredstone.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.minecraft.screen.ScreenHandlerType;
import net.onpointcoding.wirelessredstone.WirelessRedstone;
import net.onpointcoding.wirelessredstone.gui.WirelessFrequencyGuiDescription;
import net.onpointcoding.wirelessredstone.screen.WirelessFrequencyScreen;

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

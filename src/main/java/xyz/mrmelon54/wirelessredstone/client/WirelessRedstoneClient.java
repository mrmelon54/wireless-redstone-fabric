package xyz.mrmelon54.wirelessredstone.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import xyz.mrmelon54.wirelessredstone.WirelessRedstone;
import xyz.mrmelon54.wirelessredstone.gui.WirelessFrequencyGuiDescription;
import xyz.mrmelon54.wirelessredstone.models.HandheldModelProvider;
import xyz.mrmelon54.wirelessredstone.screen.WirelessFrequencyErrorScreen;
import xyz.mrmelon54.wirelessredstone.screen.WirelessFrequencyScreen;

@Environment(EnvType.CLIENT)
public class WirelessRedstoneClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        register(WirelessRedstone.WIRELESS_FREQUENCY_SCREEN, (gui, inventory, title) -> new WirelessFrequencyScreen(gui, inventory.player, title));

        HandheldModelProvider handheldModelProvider = new HandheldModelProvider();
        ModelPredicateProviderRegistry.register(WirelessRedstone.WIRELESS_HANDHELD, new Identifier("wireless_redstone_handheld_enabled"), handheldModelProvider);
    }

    void register(ScreenHandlerType<WirelessFrequencyGuiDescription> guiDescription, ScreenRegistry.Factory<WirelessFrequencyGuiDescription, WirelessFrequencyScreen> screenFactory) {
        ScreenRegistry.register(guiDescription, screenFactory);
    }

    public static void displayErrorScreen() {
        MinecraftClient.getInstance().setScreen(new WirelessFrequencyErrorScreen());
    }
}

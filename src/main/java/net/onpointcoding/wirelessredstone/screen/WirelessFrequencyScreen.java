package net.onpointcoding.wirelessredstone.screen;

import io.github.cottonmc.cotton.gui.client.CottonInventoryScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.onpointcoding.wirelessredstone.gui.WirelessFrequencyGuiDescription;

public class WirelessFrequencyScreen extends CottonInventoryScreen<WirelessFrequencyGuiDescription> {
    public WirelessFrequencyScreen(WirelessFrequencyGuiDescription description, PlayerEntity player, Text title) {
        super(description, player, title);
    }
}

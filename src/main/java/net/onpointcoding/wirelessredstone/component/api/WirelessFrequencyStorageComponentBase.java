package net.onpointcoding.wirelessredstone.component.api;

import dev.onyxstudios.cca.api.v3.component.ComponentV3;
import net.minecraft.util.math.BlockPos;
import net.onpointcoding.wirelessredstone.util.TransmittingFrequencyEntry;

import java.util.Set;

public interface WirelessFrequencyStorageComponentBase extends ComponentV3 {
    Set<BlockPos> getReceivers();

    Set<TransmittingFrequencyEntry> getTransmitting();
}

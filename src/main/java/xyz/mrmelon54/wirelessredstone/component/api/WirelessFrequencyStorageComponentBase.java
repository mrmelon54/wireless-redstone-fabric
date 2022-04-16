package xyz.mrmelon54.wirelessredstone.component.api;

import dev.onyxstudios.cca.api.v3.component.ComponentV3;
import net.minecraft.util.math.BlockPos;
import xyz.mrmelon54.wirelessredstone.util.TransmittingFrequencyEntry;
import xyz.mrmelon54.wirelessredstone.util.TransmittingHandheldEntry;

import java.util.Set;

public interface WirelessFrequencyStorageComponentBase extends ComponentV3 {
    Set<BlockPos> getReceivers();

    Set<TransmittingFrequencyEntry> getTransmitting();

    Set<TransmittingHandheldEntry> getHandheld();
}

package xyz.mrmelon54.WirelessRedstone.component;

import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;
import xyz.mrmelon54.WirelessRedstone.component.api.WirelessFrequencyStorageComponentBase;
import xyz.mrmelon54.WirelessRedstone.util.TransmittingFrequencyEntry;
import xyz.mrmelon54.WirelessRedstone.util.TransmittingHandheldEntry;

import java.util.HashSet;
import java.util.Set;

public class WirelessFrequencyStorageComponent implements WirelessFrequencyStorageComponentBase {
    private final Set<BlockPos> wirelessReceivers = new HashSet<>();
    private final Set<TransmittingFrequencyEntry> wirelessTransmitting = new HashSet<>();
    private final Set<TransmittingHandheldEntry> wirelessHandheld = new HashSet<>();

    @Override
    public Set<BlockPos> getReceivers() {
        return wirelessReceivers;
    }

    @Override
    public Set<TransmittingFrequencyEntry> getTransmitting() {
        return wirelessTransmitting;
    }

    @Override
    public Set<TransmittingHandheldEntry> getHandheld() {
        return wirelessHandheld;
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        wirelessReceivers.clear();
        wirelessTransmitting.clear();
        wirelessHandheld.clear();

        NbtList receivers = tag.getList("wireless_receivers", NbtType.COMPOUND);
        for (NbtElement item : receivers)
            if (item instanceof NbtCompound compound) {
                int x = compound.getInt("x");
                int y = compound.getInt("y");
                int z = compound.getInt("z");
                wirelessReceivers.add(new BlockPos(x, y, z));
            }

        NbtList transmitting = tag.getList("wireless_transmitting", NbtType.COMPOUND);
        for (NbtElement item : transmitting)
            if (item instanceof NbtCompound compound) {
                int x = compound.getInt("x");
                int y = compound.getInt("y");
                int z = compound.getInt("z");
                long freq = compound.getLong("freq");
                wirelessTransmitting.add(new TransmittingFrequencyEntry(new BlockPos(x, y, z), freq));
            }
    }

    @Override
    public void writeToNbt(@NotNull NbtCompound tag) {
        NbtList receivers = new NbtList();
        for (BlockPos pos : wirelessReceivers) {
            NbtCompound compound = new NbtCompound();
            compound.putInt("x", pos.getX());
            compound.putInt("y", pos.getY());
            compound.putInt("z", pos.getZ());
            receivers.add(compound);
        }
        tag.put("wireless_receivers", receivers);

        NbtList transmitting = new NbtList();
        for (TransmittingFrequencyEntry entry : wirelessTransmitting) {
            NbtCompound compound = new NbtCompound();
            compound.putInt("x", entry.pos().getX());
            compound.putInt("y", entry.pos().getY());
            compound.putInt("z", entry.pos().getZ());
            compound.putLong("freq", entry.freq());
            transmitting.add(compound);
        }
        tag.put("wireless_transmitting", transmitting);
    }
}

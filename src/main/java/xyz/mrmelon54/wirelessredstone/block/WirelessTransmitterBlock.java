package xyz.mrmelon54.wirelessredstone.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import xyz.mrmelon54.wirelessredstone.MyComponents;
import xyz.mrmelon54.wirelessredstone.WirelessRedstone;
import xyz.mrmelon54.wirelessredstone.block.entity.WirelessTransmitterBlockEntity;
import xyz.mrmelon54.wirelessredstone.util.TransmittingFrequencyEntry;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public class WirelessTransmitterBlock extends WirelessFrequencyBlock {
    public WirelessTransmitterBlock(Settings settings) {
        super(settings);
    }

    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
        updateWirelessTransmittingState(state, world, pos);
    }

    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        updateWirelessTransmittingState(state, world, pos);
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.hasBlockEntity() && !state.isOf(newState.getBlock())) {
            world.removeBlockEntity(pos);
            updateWirelessFrequency(world, pos, false, 0);
            WirelessRedstone.sendTickScheduleToReceivers(world);
        }
    }

    void updateWirelessTransmittingState(BlockState state, World world, BlockPos pos) {
        boolean bl = state.get(Properties.LIT);
        boolean z = world.isReceivingRedstonePower(pos);
        if (bl != z) {
            world.setBlockState(pos, state.with(Properties.LIT, z), Block.NOTIFY_LISTENERS);
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof WirelessTransmitterBlockEntity wirelessTransmitterBlockEntity)
                updateWirelessFrequency(world, pos, z, wirelessTransmitterBlockEntity.getFrequency());
            else updateWirelessFrequency(world, pos, false, 0);
        }
    }

    void updateWirelessFrequency(World world, BlockPos pos, boolean isPowered, long freq) {
        Set<TransmittingFrequencyEntry> transmitting = MyComponents.FrequencyStorage.get(world).getTransmitting();
        if (isPowered)
            transmitting.add(new TransmittingFrequencyEntry(pos.toImmutable(), freq));
        else
            transmitting.removeIf(transmittingFrequencyEntry -> transmittingFrequencyEntry.pos().equals(pos));

        WirelessRedstone.sendTickScheduleToReceivers(world);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new WirelessTransmitterBlockEntity(pos, state);
    }
}

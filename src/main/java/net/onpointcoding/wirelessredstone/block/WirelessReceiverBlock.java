package net.onpointcoding.wirelessredstone.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.onpointcoding.wirelessredstone.MyComponents;
import net.onpointcoding.wirelessredstone.block.entity.WirelessReceiverBlockEntity;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class WirelessReceiverBlock extends WirelessFrequencyBlock {
    public WirelessReceiverBlock(Settings settings) {
        super(settings);
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (!world.isClient) {
            if (world.getBlockEntity(pos) instanceof WirelessReceiverBlockEntity wirelessReceiverBlockEntity) {
                Boolean isLit = state.get(Properties.LIT);
                long frequency = wirelessReceiverBlockEntity.getFrequency();
                boolean shouldBeLit = hasLitTransmitterOnFrequency(world, frequency);
                System.out.println("WirelessReceiverBlock got scheduledTick");
                System.out.println("Freq: " + frequency);
                System.out.println("Is lit: " + isLit);
                System.out.println("Should be lit: " + shouldBeLit);
                if (shouldBeLit != isLit)
                    world.setBlockState(pos, state.with(Properties.LIT, shouldBeLit), Block.NOTIFY_ALL);
            } else world.setBlockState(pos, state.with(Properties.LIT, false), Block.NOTIFY_ALL);
        }

    }

    boolean hasLitTransmitterOnFrequency(World world, long freq) {
        return MyComponents.FrequencyStorage.get(world).getTransmitting().stream().anyMatch(transmittingFrequencyEntry -> transmittingFrequencyEntry.freq() == freq);
    }

    @Deprecated
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        MyComponents.FrequencyStorage.get(world).getReceivers().add(pos);
    }

    @Deprecated
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.hasBlockEntity() && !state.isOf(newState.getBlock())) {
            world.removeBlockEntity(pos);
            MyComponents.FrequencyStorage.get(world).getReceivers().remove(pos);
        }
    }

    @Override
    public boolean emitsRedstonePower(BlockState state) {
        return true;
    }

    @Override
    public int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        return state.get(Properties.LIT) ? 15 : 0;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new WirelessReceiverBlockEntity(pos, state);
    }
}

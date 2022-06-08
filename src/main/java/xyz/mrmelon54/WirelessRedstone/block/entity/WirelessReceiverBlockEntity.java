package xyz.mrmelon54.WirelessRedstone.block.entity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import xyz.mrmelon54.WirelessRedstone.WirelessRedstone;

public class WirelessReceiverBlockEntity extends WirelessFrequencyBlockEntity<WirelessReceiverBlockEntity> {
    public WirelessReceiverBlockEntity(BlockPos pos, BlockState state) {
        super(WirelessRedstone.WIRELESS_RECEIVER_BLOCK_ENTITY, pos, state);
    }

    public static void tick(World world, BlockPos pos, BlockState state, WirelessReceiverBlockEntity be) {
        if (!world.isClient) {
            if (world.getBlockEntity(pos) instanceof WirelessReceiverBlockEntity wirelessReceiverBlockEntity) {
                Boolean isLit = state.get(Properties.LIT);
                long frequency = wirelessReceiverBlockEntity.getFrequency();
                boolean shouldBeLit = WirelessRedstone.hasLitTransmitterOnFrequency(world, frequency);
                if (shouldBeLit != isLit)
                    world.setBlockState(pos, state.with(Properties.LIT, shouldBeLit), Block.NOTIFY_ALL);
            } else world.setBlockState(pos, state.with(Properties.LIT, false), Block.NOTIFY_ALL);
        }
    }
}

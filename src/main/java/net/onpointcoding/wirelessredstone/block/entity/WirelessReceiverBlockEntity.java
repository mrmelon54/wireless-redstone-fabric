package net.onpointcoding.wirelessredstone.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.onpointcoding.wirelessredstone.WirelessRedstone;

public class WirelessReceiverBlockEntity extends WirelessFrequencyBlockEntity<WirelessReceiverBlockEntity> {
    public WirelessReceiverBlockEntity(BlockPos pos, BlockState state) {
        super(WirelessRedstone.WIRELESS_RECEIVER_BLOCK_ENTITY, pos, state);
    }
}

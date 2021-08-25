package net.onpointcoding.wirelessredstone.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.onpointcoding.wirelessredstone.WirelessRedstone;

public class WirelessTransmitterBlockEntity extends WirelessFrequencyBlockEntity<WirelessTransmitterBlockEntity> {
    public WirelessTransmitterBlockEntity(BlockPos pos, BlockState state) {
        super(WirelessRedstone.WIRELESS_TRANSMITTER_BLOCK_ENTITY, pos, state);
    }
}

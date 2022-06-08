package xyz.mrmelon54.WirelessRedstone.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Optional;
import java.util.function.BiFunction;

public class HandheldScreenHandlerContext implements ScreenHandlerContext {
    private final PlayerEntity player;
    private final World world;
    private final BlockPos blockPos;

    public HandheldScreenHandlerContext(PlayerEntity player, World world, BlockPos impossibleBlockPos) {
        this.player = player;
        this.world = world;
        this.blockPos = impossibleBlockPos;
    }

    @Override
    public <T> Optional<T> get(BiFunction<World, BlockPos, T> getter) {
        return Optional.of(getter.apply(world, blockPos));
    }

    public PlayerEntity getPlayer() {
        return player;
    }
}

package xyz.mrmelon54.WirelessRedstone.models;

import net.minecraft.client.item.ClampedModelPredicateProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.Nullable;
import xyz.mrmelon54.WirelessRedstone.WirelessRedstone;
import xyz.mrmelon54.WirelessRedstone.item.WirelessHandheldItem;

public class HandheldModelProvider implements ClampedModelPredicateProvider {
    @Override
    public float unclampedCall(ItemStack stack, @Nullable ClientWorld world, @Nullable LivingEntity entity, int seed) {
        if (stack != null && !stack.isEmpty()) {
            if (stack.isOf(WirelessRedstone.WIRELESS_HANDHELD)) {
                NbtCompound nbt = stack.getOrCreateNbt();
                return nbt.getBoolean(WirelessHandheldItem.WIRELESS_HANDHELD_ENABLED) ? 0.01f : 0;
            }
        }
        return 0;
    }
}

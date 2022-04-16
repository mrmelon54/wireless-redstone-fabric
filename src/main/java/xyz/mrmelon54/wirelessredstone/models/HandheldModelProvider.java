package xyz.mrmelon54.wirelessredstone.models;

import net.minecraft.client.item.UnclampedModelPredicateProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.Nullable;
import xyz.mrmelon54.wirelessredstone.WirelessRedstone;
import xyz.mrmelon54.wirelessredstone.item.WirelessHandheldItem;

public class HandheldModelProvider implements UnclampedModelPredicateProvider {
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

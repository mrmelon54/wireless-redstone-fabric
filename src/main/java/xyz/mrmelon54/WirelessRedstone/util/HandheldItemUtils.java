package xyz.mrmelon54.WirelessRedstone.util;

import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;
import xyz.mrmelon54.WirelessRedstone.MyComponents;
import xyz.mrmelon54.WirelessRedstone.WirelessRedstone;
import xyz.mrmelon54.WirelessRedstone.item.WirelessHandheldItem;

import java.util.UUID;

public class HandheldItemUtils {
    public static void addHandheldFromPlayer(ServerPlayerEntity player, World world) {
        PlayerInventory inv = player.getInventory();
        for (int i = 0; i < inv.size(); i++) {
            ItemStack stack = inv.getStack(i);
            if (stack != null && !stack.isEmpty()) addHandheldEntry(stack, world);
        }
        WirelessRedstone.sendTickScheduleToReceivers(world);
    }

    public static void removeHandheldFromPlayer(ServerPlayerEntity player, World world) {
        PlayerInventory inv = player.getInventory();
        for (int i = 0; i < inv.size(); i++) {
            ItemStack stack = inv.getStack(i);
            if (stack != null && !stack.isEmpty()) removeHandheldEntry(stack, world);
        }
        WirelessRedstone.sendTickScheduleToReceivers(world);
    }

    public static void addHandheldFromChunk(ServerWorld world, WorldChunk chunk) {
        chunk.getBlockEntities().forEach((blockPos, blockEntity) -> {
            if (blockEntity instanceof LootableContainerBlockEntity lootableContainerBlockEntity) {
                NbtCompound nbt = lootableContainerBlockEntity.createNbt();
                NbtList items = nbt.getList("Items", NbtType.COMPOUND);
                for (NbtElement item : items) {
                    if (item instanceof NbtCompound compound) {
                        ItemStack stack = ItemStack.fromNbt(compound);
                        if (stack.isEmpty()) continue;
                        addHandheldEntry(stack, world);
                    }
                }
            }
        });
    }

    public static void removeHandheldFromChunk(ServerWorld world, WorldChunk chunk) {
        chunk.getBlockEntities().forEach((blockPos, blockEntity) -> {
            if (blockEntity instanceof LootableContainerBlockEntity lootableContainerBlockEntity) {
                NbtCompound nbt = lootableContainerBlockEntity.createNbt();
                NbtList items = nbt.getList("Items", NbtType.COMPOUND);
                for (NbtElement item : items) {
                    if (item instanceof NbtCompound compound) {
                        ItemStack stack = ItemStack.fromNbt(compound);
                        if (stack.isEmpty()) continue;
                        removeHandheldEntry(stack, world);
                    }
                }
            }
        });
    }

    public static void addHandheldEntry(ItemStack stack, World world) {
        if (stack.isOf(WirelessRedstone.WIRELESS_HANDHELD)) {
            NbtCompound nbt = stack.getOrCreateNbt();
            boolean enabled = nbt.getBoolean(WirelessHandheldItem.WIRELESS_HANDHELD_ENABLED);
            if (enabled) {
                UUID uuid = nbt.getUuid(WirelessHandheldItem.WIRELESS_HANDHELD_UUID);
                int freq = nbt.getInt(WirelessHandheldItem.WIRELESS_HANDHELD_FREQ);
                MyComponents.FrequencyStorage.get(world).getHandheld().add(new TransmittingHandheldEntry(uuid, freq));
            }
        }
    }

    public static void removeHandheldEntry(ItemStack stack, World world) {
        if (stack.isOf(WirelessRedstone.WIRELESS_HANDHELD)) {
            NbtCompound compound = WirelessHandheldItem.getOrCreateNbt(stack);
            UUID uuid = compound.getUuid(WirelessHandheldItem.WIRELESS_HANDHELD_UUID);
            MyComponents.FrequencyStorage.get(world).getHandheld().removeIf(transmittingHandheldEntry -> transmittingHandheldEntry.handheldUuid().equals(uuid));
        }
    }
}

package xyz.mrmelon54.wirelessredstone;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityWorldChangeEvents;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.tick.OrderedTick;
import net.minecraft.world.tick.TickScheduler;
import xyz.mrmelon54.wirelessredstone.block.WirelessReceiverBlock;
import xyz.mrmelon54.wirelessredstone.block.WirelessTransmitterBlock;
import xyz.mrmelon54.wirelessredstone.block.entity.WirelessReceiverBlockEntity;
import xyz.mrmelon54.wirelessredstone.block.entity.WirelessTransmitterBlockEntity;
import xyz.mrmelon54.wirelessredstone.gui.WirelessFrequencyGuiDescription;
import xyz.mrmelon54.wirelessredstone.item.WirelessHandheldItem;
import xyz.mrmelon54.wirelessredstone.util.NetworkingConstants;
import xyz.mrmelon54.wirelessredstone.util.TransmittingHandheldEntry;

import java.util.UUID;

public class WirelessRedstone implements ModInitializer {
    public static final Block WIRELESS_TRANSMITTER = new WirelessTransmitterBlock(FabricBlockSettings.of(Material.METAL).collidable(true).strength(0).luminance(value -> value.get(Properties.LIT) ? 7 : 0));
    public static final Block WIRELESS_RECEIVER = new WirelessReceiverBlock(FabricBlockSettings.of(Material.METAL).collidable(true).strength(0).luminance(value -> value.get(Properties.LIT) ? 7 : 0));
    public static final Item WIRELESS_HANDHELD = new WirelessHandheldItem(new FabricItemSettings().group(ItemGroup.REDSTONE).maxCount(1));
    public static BlockEntityType<WirelessTransmitterBlockEntity> WIRELESS_TRANSMITTER_BLOCK_ENTITY;
    public static BlockEntityType<WirelessReceiverBlockEntity> WIRELESS_RECEIVER_BLOCK_ENTITY;
    public static ScreenHandlerType<WirelessFrequencyGuiDescription> WIRELESS_FREQUENCY_SCREEN;
    public static final BlockPos ImpossibleBlockPos = BlockPos.ORIGIN.offset(Direction.DOWN, 30000);

    public void onInitialize() {
        ServerPlayConnectionEvents.INIT.register((handler, server) -> addHandheldSignals(handler.player, handler.player.world));
        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> removeHandheldSignals(handler.player, handler.player.world));

        // remove and replace all handhelds when changing world
        ServerEntityWorldChangeEvents.AFTER_PLAYER_CHANGE_WORLD.register((player, origin, destination) -> {
            removeHandheldSignals(player, origin);
            addHandheldSignals(player, destination);
        });

        WIRELESS_FREQUENCY_SCREEN = ScreenHandlerRegistry.registerSimple(new Identifier("wireless_redstone", "frequency_screen"), ((syncId, inventory) -> new WirelessFrequencyGuiDescription(syncId, inventory, ScreenHandlerContext.EMPTY)));

        Registry.register(Registry.BLOCK, new Identifier("wireless_redstone", "transmitter"), WIRELESS_TRANSMITTER);
        Registry.register(Registry.ITEM, new Identifier("wireless_redstone", "transmitter"), new BlockItem(WIRELESS_TRANSMITTER, new FabricItemSettings().group(ItemGroup.REDSTONE).maxCount(64)));
        WIRELESS_TRANSMITTER_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, "wireless_redstone:transmitter", FabricBlockEntityTypeBuilder.create(WirelessTransmitterBlockEntity::new, WIRELESS_TRANSMITTER).build());

        Registry.register(Registry.BLOCK, new Identifier("wireless_redstone", "receiver"), WIRELESS_RECEIVER);
        Registry.register(Registry.ITEM, new Identifier("wireless_redstone", "receiver"), new BlockItem(WIRELESS_RECEIVER, new FabricItemSettings().group(ItemGroup.REDSTONE).maxCount(64)));
        WIRELESS_RECEIVER_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, "wireless_redstone:receiver", FabricBlockEntityTypeBuilder.create(WirelessReceiverBlockEntity::new, WIRELESS_RECEIVER).build());

        Registry.register(Registry.ITEM, new Identifier("wireless_redstone", "handheld"), WIRELESS_HANDHELD);

        ServerPlayNetworking.registerGlobalReceiver(NetworkingConstants.WIRELESS_FREQUENCY_CHANGE_PACKET_ID, ((server, player, handler, buf, responseSender) -> {
            if (player.currentScreenHandler instanceof WirelessFrequencyGuiDescription wirelessFrequencyScreenHandler) {
                if (wirelessFrequencyScreenHandler.getPropertyDelegate() != null)
                    wirelessFrequencyScreenHandler.getPropertyDelegate().set(0, buf.readInt());
            }
        }));
    }

    private void addHandheldSignals(ServerPlayerEntity player, World world) {
        System.out.println("addHandheldSignals()");
        PlayerInventory inv = player.getInventory();
        for (int i = 0; i < inv.size(); i++) {
            ItemStack stack = inv.getStack(i);
            if (stack != null && !stack.isEmpty()) {
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
        }
    }

    private void removeHandheldSignals(ServerPlayerEntity player, World world) {
        System.out.println("removeHandheldSignals()");
        PlayerInventory inv = player.getInventory();
        for (int i = 0; i < inv.size(); i++) {
            ItemStack stack = inv.getStack(i);
            if (stack != null && !stack.isEmpty()) {
                if (stack.isOf(WirelessRedstone.WIRELESS_HANDHELD)) {
                    NbtCompound compound = WirelessHandheldItem.getOrCreateNbt(stack);
                    UUID uuid = compound.getUuid(WirelessHandheldItem.WIRELESS_HANDHELD_UUID);
                    MyComponents.FrequencyStorage.get(world).getHandheld().removeIf(transmittingHandheldEntry -> transmittingHandheldEntry.handheldUuid().equals(uuid));
                }
            }
        }
    }

    public static void sendTickScheduleToReceivers(World world) {
        TickScheduler<Block> blockTickScheduler = world.getBlockTickScheduler();
        for (BlockPos p : MyComponents.FrequencyStorage.get(world).getReceivers())
            blockTickScheduler.scheduleTick(OrderedTick.create(WirelessRedstone.WIRELESS_RECEIVER, p));
    }
}

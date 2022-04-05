package xyz.mrmelon54.wirelessredstone;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.tick.OrderedTick;
import net.minecraft.world.tick.TickScheduler;
import xyz.mrmelon54.wirelessredstone.block.WirelessReceiverBlock;
import xyz.mrmelon54.wirelessredstone.block.WirelessTransmitterBlock;
import xyz.mrmelon54.wirelessredstone.block.entity.WirelessReceiverBlockEntity;
import xyz.mrmelon54.wirelessredstone.block.entity.WirelessTransmitterBlockEntity;
import xyz.mrmelon54.wirelessredstone.gui.WirelessFrequencyGuiDescription;
import xyz.mrmelon54.wirelessredstone.util.NetworkingConstants;

public class WirelessRedstone implements ModInitializer {
    public static final Block WIRELESS_TRANSMITTER = new WirelessTransmitterBlock(FabricBlockSettings.of(Material.METAL).collidable(true).strength(0).luminance(value -> value.get(Properties.LIT) ? 7 : 0));
    public static final Block WIRELESS_RECEIVER = new WirelessReceiverBlock(FabricBlockSettings.of(Material.METAL).collidable(true).luminance(value -> value.get(Properties.LIT) ? 7 : 0));
    public static BlockEntityType<WirelessTransmitterBlockEntity> WIRELESS_TRANSMITTER_BLOCK_ENTITY;
    public static BlockEntityType<WirelessReceiverBlockEntity> WIRELESS_RECEIVER_BLOCK_ENTITY;
    public static ScreenHandlerType<WirelessFrequencyGuiDescription> WIRELESS_FREQUENCY_SCREEN;

    public void onInitialize() {
        WIRELESS_FREQUENCY_SCREEN = ScreenHandlerRegistry.registerSimple(new Identifier("wireless_redstone", "frequency_screen"), ((syncId, inventory) -> new WirelessFrequencyGuiDescription(syncId, inventory, ScreenHandlerContext.EMPTY)));

        Registry.register(Registry.BLOCK, new Identifier("wireless_redstone", "transmitter"), WIRELESS_TRANSMITTER);
        Registry.register(Registry.ITEM, new Identifier("wireless_redstone", "transmitter"), new BlockItem(WIRELESS_TRANSMITTER, new FabricItemSettings().group(ItemGroup.REDSTONE).maxCount(64)));
        WIRELESS_TRANSMITTER_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, "wireless_redstone:transmitter", FabricBlockEntityTypeBuilder.create(WirelessTransmitterBlockEntity::new, WIRELESS_TRANSMITTER).build());

        Registry.register(Registry.BLOCK, new Identifier("wireless_redstone", "receiver"), WIRELESS_RECEIVER);
        Registry.register(Registry.ITEM, new Identifier("wireless_redstone", "receiver"), new BlockItem(WIRELESS_RECEIVER, new FabricItemSettings().group(ItemGroup.REDSTONE).maxCount(64)));
        WIRELESS_RECEIVER_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, "wireless_redstone:receiver", FabricBlockEntityTypeBuilder.create(WirelessReceiverBlockEntity::new, WIRELESS_RECEIVER).build());

        ServerPlayNetworking.registerGlobalReceiver(NetworkingConstants.WIRELESS_FREQUENCY_CHANGE_PACKET_ID, ((server, player, handler, buf, responseSender) -> {
            if (player.currentScreenHandler instanceof WirelessFrequencyGuiDescription wirelessFrequencyScreenHandler) {
                if (wirelessFrequencyScreenHandler.getPropertyDelegate() != null)
                    wirelessFrequencyScreenHandler.getPropertyDelegate().set(0, buf.readInt());
            }
        }));
    }

    public static void sendTickScheduleToReceivers(World world) {
        TickScheduler<Block> blockTickScheduler = world.getBlockTickScheduler();
        for (BlockPos p : MyComponents.FrequencyStorage.get(world).getReceivers())
            blockTickScheduler.scheduleTick(OrderedTick.create(WirelessRedstone.WIRELESS_RECEIVER, p));
    }
}

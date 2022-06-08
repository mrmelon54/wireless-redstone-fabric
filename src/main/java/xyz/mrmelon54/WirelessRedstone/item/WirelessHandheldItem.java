package xyz.mrmelon54.WirelessRedstone.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import xyz.mrmelon54.WirelessRedstone.MyComponents;
import xyz.mrmelon54.WirelessRedstone.WirelessRedstone;
import xyz.mrmelon54.WirelessRedstone.gui.WirelessFrequencyGuiDescription;
import xyz.mrmelon54.WirelessRedstone.util.HandheldScreenHandlerContext;
import xyz.mrmelon54.WirelessRedstone.util.TransmittingHandheldEntry;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public class WirelessHandheldItem extends Item implements NamedScreenHandlerFactory {
    public static final String WIRELESS_HANDHELD_UUID = "wireless_handheld_uuid";
    public static final String WIRELESS_HANDHELD_ENABLED = "wireless_handheld_enabled";
    public static final String WIRELESS_HANDHELD_FREQ = "wireless_handheld_freq";

    public WirelessHandheldItem(FabricItemSettings fabricItemSettings) {
        super(fabricItemSettings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if (user.isSneaking()) {
            user.openHandledScreen(this);
            return TypedActionResult.pass(itemStack);
        }

        NbtCompound compound = getOrCreateNbt(itemStack);
        if (compound == null) return TypedActionResult.fail(itemStack);

        UUID uuid = compound.getUuid(WIRELESS_HANDHELD_UUID);
        boolean enabled = !compound.getBoolean(WIRELESS_HANDHELD_ENABLED);
        compound.putBoolean(WIRELESS_HANDHELD_ENABLED, enabled);
        int freq = compound.getInt(WIRELESS_HANDHELD_FREQ);

        Set<TransmittingHandheldEntry> handheld = MyComponents.FrequencyStorage.get(world).getHandheld();
        if (enabled)
            handheld.add(new TransmittingHandheldEntry(uuid, freq));
        else
            handheld.removeIf(transmittingFrequencyEntry -> transmittingFrequencyEntry.handheldUuid().equals(uuid));

        WirelessRedstone.sendTickScheduleToReceivers(world);
        return TypedActionResult.pass(itemStack);
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable(getTranslationKey());
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);

        NbtCompound compound = getOrCreateNbt(stack);
        if (compound != null) {
            boolean enabled = compound.getBoolean(WIRELESS_HANDHELD_ENABLED);
            int freq = compound.getInt(WIRELESS_HANDHELD_FREQ);
            MutableText freqTooltip = Text.translatable("item.wireless-redstone.item.tooltip-frequency", freq);
            tooltip.add(enabled ? freqTooltip.formatted(Formatting.GREEN) : freqTooltip.formatted(Formatting.RED));
        }
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new WirelessFrequencyGuiDescription(syncId, inv, new HandheldScreenHandlerContext(player, player.world, WirelessRedstone.ImpossibleBlockPos));
    }

    public static NbtCompound getOrCreateNbt(ItemStack stack) {
        if (stack != null) {
            NbtCompound compound = stack.getOrCreateNbt();
            if (stack.isOf(WirelessRedstone.WIRELESS_HANDHELD)) {
                if (!compound.containsUuid(WirelessHandheldItem.WIRELESS_HANDHELD_UUID))
                    compound.putUuid(WirelessHandheldItem.WIRELESS_HANDHELD_UUID, UUID.randomUUID());
                if (!compound.contains(WirelessHandheldItem.WIRELESS_HANDHELD_ENABLED, NbtElement.BYTE_TYPE))
                    compound.putBoolean(WirelessHandheldItem.WIRELESS_HANDHELD_ENABLED, false);
                if (!compound.contains(WirelessHandheldItem.WIRELESS_HANDHELD_FREQ, NbtElement.INT_TYPE))
                    compound.putInt(WirelessHandheldItem.WIRELESS_HANDHELD_FREQ, 0);
            }
            return compound;
        }
        return null;
    }

    @Override
    public void onItemEntityDestroyed(ItemEntity entity) {
        super.onItemEntityDestroyed(entity);

        NbtCompound compound = getOrCreateNbt(entity.getStack());
        if (compound == null) return;

        UUID uuid = compound.getUuid(WIRELESS_HANDHELD_UUID);
        compound.putBoolean(WIRELESS_HANDHELD_ENABLED, false);

        Set<TransmittingHandheldEntry> handheld = MyComponents.FrequencyStorage.get(entity.world).getHandheld();
        handheld.removeIf(transmittingFrequencyEntry -> transmittingFrequencyEntry.handheldUuid().equals(uuid));

        WirelessRedstone.sendTickScheduleToReceivers(entity.world);
    }
}

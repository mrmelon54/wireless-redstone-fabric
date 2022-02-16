package xyz.mrmelon54.wirelessredstone.gui;

import io.github.cottonmc.cotton.gui.SyncedGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import io.github.cottonmc.cotton.gui.widget.WTextField;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.text.TranslatableText;
import xyz.mrmelon54.wirelessredstone.WirelessRedstone;
import xyz.mrmelon54.wirelessredstone.client.WirelessRedstoneClient;
import xyz.mrmelon54.wirelessredstone.packet.WirelessFrequencyChangeC2SPacket;
import xyz.mrmelon54.wirelessredstone.util.NetworkingConstants;

import java.util.regex.Pattern;

public class WirelessFrequencyGuiDescription extends SyncedGuiDescription {
    final Pattern numericCheckerPattern = Pattern.compile("^ ?-?[0-9]*$");
    static final int PROPERTY_COUNT = 1;
    private int wirelessFrequencyInput;
    private final WTextField wirelessFrequencyBox;

    public WirelessFrequencyGuiDescription(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(WirelessRedstone.WIRELESS_FREQUENCY_SCREEN, syncId, playerInventory, getBlockInventory(context, 0), getBlockPropertyDelegate(context, PROPERTY_COUNT));

        WPlainPanel root = new WPlainPanel();
        setRootPanel(root);
        root.setSize(160, 55);
        root.setInsets(Insets.ROOT_PANEL);

        WTextField wTextField = new WTextField();
        wirelessFrequencyBox = wTextField;
        wTextField.setMaxLength(20);
        wTextField.setText("");
        wTextField.setTextPredicate(s -> numericCheckerPattern.matcher(s).find());
        root.add(wTextField, 0, 15, 160, 20);

        WButton wButton = new WButton();
        wButton.setLabel(new TranslatableText("screen.wireless_redstone.set_frequency"));
        wButton.setOnClick(() -> {
            String s = wTextField.getText();
            if (playerInventory.player.world.isClient() && numericCheckerPattern.matcher(s).find()) {
                String t = s.trim();
                boolean parsed = false;
                try {
                    wirelessFrequencyInput = Integer.parseInt(t);
                    parsed = true;
                } catch (Exception ignored) {

                }
                if (parsed) {
                    System.out.println("Sending wirelessFrequencyInput to server: " + wirelessFrequencyInput);
                    WirelessFrequencyChangeC2SPacket wirelessFrequencyChangeC2SPacket = new WirelessFrequencyChangeC2SPacket(wirelessFrequencyInput);
                    PacketByteBuf byteBuf = new PacketByteBuf(Unpooled.buffer());
                    wirelessFrequencyChangeC2SPacket.write(byteBuf);
                    ClientPlayNetworking.send(NetworkingConstants.WIRELESS_FREQUENCY_CHANGE_PACKET_ID, byteBuf);
                } else {
                    WirelessRedstoneClient.displayErrorScreen();
                }
            }
        });
        root.add(wButton, 0, 36, 160, 20);

        root.validate(this);
    }

    @Override
    public void setProperty(int id, int value) {
        super.setProperty(id, value);
        if (id == 0)
            wirelessFrequencyBox.setText(String.valueOf(value));
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }

    @Override
    public void close(PlayerEntity playerEntity) {
        super.close(playerEntity);
    }
}

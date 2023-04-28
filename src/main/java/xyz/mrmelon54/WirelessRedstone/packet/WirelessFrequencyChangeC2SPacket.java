package xyz.mrmelon54.WirelessRedstone.packet;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.listener.ServerPlayPacketListener;
import net.minecraft.network.packet.Packet;

public record WirelessFrequencyChangeC2SPacket(int freq) implements Packet<ServerPlayPacketListener> {
    @Override
    public void write(PacketByteBuf buf) {
        buf.writeInt(freq);
    }

    @Override
    public void apply(ServerPlayPacketListener listener) {
    }
}

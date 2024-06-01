package me.whirlfrenzy.itemdespawntimer.networking;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record SetItemAgePacket(int entityId, int itemAge, int attempts) implements CustomPayload {
    public static final Id<SetItemAgePacket> PACKET_ID = new Id<>(new Identifier("item-despawn-timer","set-item-age"));
    public static final PacketCodec<RegistryByteBuf, SetItemAgePacket> PACKET_CODEC = PacketCodec.of((value, buf) -> {
        buf.writeVarInt(value.entityId);
        buf.writeVarInt(value.itemAge);
    }, buf -> new SetItemAgePacket(buf.readVarInt(), buf.readVarInt(), 0));

    @Override
    public Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }
}

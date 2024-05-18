package me.whirlfrenzy.itemdespawntimer.networking;

import io.netty.buffer.ByteBuf;
import me.whirlfrenzy.itemdespawntimer.ItemDespawnTimer;
import me.whirlfrenzy.itemdespawntimer.access.ItemEntityAccessInterface;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record SetItemAgePacket(int entityId, int itemAge, int attempts) implements CustomPayload {
    public static final Id<SetItemAgePacket> PACKET_ID = new Id<>(new Identifier("item-despawn-timer","set-item-age"));
    public static final PacketCodec<ByteBuf, SetItemAgePacket> PACKET_CODEC = PacketCodec.of(((value, buf) -> {
        buf.writeInt(value.entityId);
        buf.writeInt(value.itemAge);
    }), buf -> new SetItemAgePacket(buf.readInt(), buf.readInt(), 0));

    @Override
    public Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }

    public boolean attemptSet(){
        ClientWorld world = MinecraftClient.getInstance().world;
        if(world == null) return false;

        Entity itemEntity = world.getEntityById(this.entityId);

        if(itemEntity == null){
            ItemDespawnTimer.LOGGER.info("Failed to set item age for entity {}, entity does not exist on the client", this.entityId);
            return false;
        } else if(itemEntity instanceof ItemEntity){
            ((ItemEntityAccessInterface)itemEntity).item_despawn_timer$setModItemAge(this.itemAge);
            return true;
        }

        // Should only reach this when an entity with the specified id exists, but it isn't an item entity
        // And I have to return true otherwise it would attempt to set again in the next tick
        return true;
    }
}

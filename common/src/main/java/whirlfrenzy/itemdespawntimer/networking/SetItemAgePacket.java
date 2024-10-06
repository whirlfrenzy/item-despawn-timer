package whirlfrenzy.itemdespawntimer.networking;

import net.minecraft.network.RegistryByteBuf;
import whirlfrenzy.itemdespawntimer.ItemDespawnTimer;
import whirlfrenzy.itemdespawntimer.access.ItemEntityAccessInterface;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

public record SetItemAgePacket(int entityId, int itemAge, int attempts) implements ItemDataPacket {
    public static final Id<SetItemAgePacket> PACKET_ID = new Id<>(ItemDespawnTimer.identifier("set-item-age"));
    public static final PacketCodec<RegistryByteBuf, SetItemAgePacket> PACKET_CODEC = PacketCodec.of(((value, buf) -> {
        buf.writeVarInt(value.entityId);
        buf.writeVarInt(value.itemAge);
    }), buf -> new SetItemAgePacket(buf.readVarInt(), buf.readVarInt(), 0));

    @Override
    public Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }

    public boolean attemptSet(){
        ClientWorld world = MinecraftClient.getInstance().world;
        if(world == null) return false;

        Entity itemEntity = world.getEntityById(this.entityId);

        if(itemEntity == null){
            return false;
        } else if(itemEntity instanceof ItemEntity){
            ((ItemEntityAccessInterface)itemEntity).item_despawn_timer$setModItemAge(this.itemAge);
            return true;
        }

        // Should only reach this when an entity with the specified id exists, but it isn't an item entity
        // And I have to return true otherwise it would attempt to set again in the next tick
        return true;
    }

    @Override
    public ItemDataPacket createNextAttempt() {
        return new SetItemAgePacket(this.entityId, this.itemAge, this.attempts + 1);
    }

    @Override
    public int getAttempts() {
        return this.attempts;
    }

    @Override
    public int getEntityId() {
        return this.entityId;
    }
}

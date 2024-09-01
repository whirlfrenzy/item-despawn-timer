package whirlfrenzy.itemdespawntimer.networking;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import whirlfrenzy.itemdespawntimer.ItemDespawnTimer;
import whirlfrenzy.itemdespawntimer.access.ItemEntityAccessInterface;

/*
This is to support NeoForge's item lifespan extending functionality
(NeoForge calls it item lifespan and thus this is called SetItemLifespanPacket just for clarity)

If mods set the item age to a lower number (even going down to the negatives)
to extend an item's lifespan (like Custom Item Despawn Duration :insert self-promotion sticker gif thingy here:)
then this is unnecessary
 */
public record SetItemLifespanPacket(int entityId, int itemLifespan, int attempts) implements ItemDataPacket {
    public static final Id<SetItemLifespanPacket> PACKET_ID = new Id<>(ItemDespawnTimer.identifier("set-item-lifespan"));

    public static final PacketCodec<RegistryByteBuf, SetItemLifespanPacket> PACKET_CODEC = PacketCodec.of(((value, buf) -> {
        buf.writeVarInt(value.entityId);
        buf.writeVarInt(value.itemLifespan);
    }), buf -> new SetItemLifespanPacket(buf.readVarInt(), buf.readVarInt(), 0));

    @Override
    public Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }

    public boolean attemptSet(){
        ClientWorld world = MinecraftClient.getInstance().world;
        if(world == null) return false;

        Entity itemEntity = world.getEntityById(this.entityId);

        if(itemEntity == null){
            ItemDespawnTimer.LOGGER.warn("Failed to set item lifespan for entity {}, entity does not exist on the client", this.entityId);
            return false;
        } else if(itemEntity instanceof ItemEntity){
            ((ItemEntityAccessInterface)itemEntity).item_despawn_timer$setModItemLifespan(this.itemLifespan);
            return true;
        }

        return true;
    }

    @Override
    public ItemDataPacket createNextAttempt() {
        return new SetItemLifespanPacket(this.entityId, this.itemLifespan, this.attempts + 1);
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

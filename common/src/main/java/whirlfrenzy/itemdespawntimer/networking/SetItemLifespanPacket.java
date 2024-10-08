package whirlfrenzy.itemdespawntimer.networking;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import whirlfrenzy.itemdespawntimer.ItemDespawnTimer;
import whirlfrenzy.itemdespawntimer.access.ItemEntityAccessInterface;

/*
This is to support Forge's item lifespan extending functionality
(Forge calls it item lifespan and thus this is called SetItemLifespanPacket just for clarity)

If mods set the item age to a lower number (even going down to the negatives)
to extend an item's lifespan (like Custom Item Despawn Duration :insert self-promotion sticker gif thingy here:)
then this is unnecessary
 */
public class SetItemLifespanPacket extends ItemDataPacket {
    public static final Identifier PACKET_ID = ItemDespawnTimer.identifier("set-item-lifespan");

    private int entityId;
    private int itemLifespan;
    private int attempts;

    public SetItemLifespanPacket(int entityId, int itemLifespan, int attempts){
        this.entityId = entityId;
        this.itemLifespan = itemLifespan;
        this.attempts = attempts;
    }

    @Override
    public Identifier getId() {
        return PACKET_ID;
    }

    public static SetItemLifespanPacket fromPacketByteBuffer(PacketByteBuf packetByteBuf){
        return new SetItemLifespanPacket(packetByteBuf.readVarInt(), packetByteBuf.readVarInt(), 0);
    }

    public PacketByteBuf writeToBuffer(PacketByteBuf buffer){
        buffer.writeVarInt(this.entityId);
        buffer.writeVarInt(this.itemLifespan);

        return buffer;
    }

    public boolean attemptSet(){
        ClientWorld world = MinecraftClient.getInstance().world;
        if(world == null) return false;

        Entity itemEntity = world.getEntityById(this.entityId);

        if(itemEntity == null){
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

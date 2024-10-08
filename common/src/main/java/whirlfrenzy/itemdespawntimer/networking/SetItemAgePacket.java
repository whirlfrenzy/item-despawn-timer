package whirlfrenzy.itemdespawntimer.networking;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import whirlfrenzy.itemdespawntimer.ItemDespawnTimer;
import whirlfrenzy.itemdespawntimer.access.ItemEntityAccessInterface;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;

public class SetItemAgePacket extends ItemDataPacket {
    public static final Identifier PACKET_ID = ItemDespawnTimer.identifier("set-item-age");

    private int entityId;
    private int itemAge;
    private int attempts;

    @Override
    public Identifier getId() {
        return PACKET_ID;
    }

    public SetItemAgePacket(int entityId, int itemAge, int attempts){
        this.entityId = entityId;
        this.itemAge = itemAge;
        this.attempts = attempts;
    }

    public static SetItemAgePacket fromPacketByteBuffer(PacketByteBuf packetByteBuf){
        return new SetItemAgePacket(packetByteBuf.readVarInt(), packetByteBuf.readVarInt(), 0);
    }

    public PacketByteBuf writeToBuffer(PacketByteBuf buffer){
        buffer.writeVarInt(this.entityId);
        buffer.writeVarInt(this.itemAge);

        return buffer;
    }

    @Override
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

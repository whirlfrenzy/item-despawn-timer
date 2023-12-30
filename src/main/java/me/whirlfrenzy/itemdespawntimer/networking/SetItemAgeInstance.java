package me.whirlfrenzy.itemdespawntimer.networking;

import me.whirlfrenzy.itemdespawntimer.access.ItemEntityAccessInterface;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.network.PacketByteBuf;

// TODO: Find a better name for this
public class SetItemAgeInstance {
    private int entityId;
    private int itemAge;

    private int currentAttempt = 0;

    public SetItemAgeInstance(ItemEntity itemEntity){
        this.entityId = itemEntity.getId();
        this.itemAge = itemEntity.getItemAge();
    }

    public SetItemAgeInstance(PacketByteBuf buf){
        this.entityId = buf.readInt();
        this.itemAge = buf.readInt();
    }

    public void attemptSet(){
        if(currentAttempt > 5){
            return;
        }

        ClientWorld world = MinecraftClient.getInstance().world;
        if(world == null) return;

        Entity itemEntity = world.getEntityById(this.entityId);

        if(itemEntity == null){
            PacketReceiver.addSetRetry(this);
            currentAttempt++;
        } else if(itemEntity instanceof ItemEntity){
            ((ItemEntityAccessInterface)itemEntity).item_despawn_timer$setModItemAge(this.itemAge);
        }
    }

    public int getEntityId() {
        return entityId;
    }

    public int getItemAge() {
        return itemAge;
    }
}

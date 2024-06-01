package me.whirlfrenzy.itemdespawntimer.networking;

import me.whirlfrenzy.itemdespawntimer.ItemDespawnTimer;
import me.whirlfrenzy.itemdespawntimer.access.ItemEntityAccessInterface;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import java.util.ArrayList;

public class ClientNetworking {
    public static ArrayList<SetItemAgePacket> retryInstances = new ArrayList<>();

    public static void initialize(){
        ClientPlayNetworking.registerGlobalReceiver(SetItemAgePacket.PACKET_ID, ((payload, context) -> {
            context.client().execute(() -> {
                retryInstances.add(payload);
            });
        }));

        ClientTickEvents.END_WORLD_TICK.register((world) ->{
            ArrayList<SetItemAgePacket> copy = (ArrayList<SetItemAgePacket>) retryInstances.clone();
            retryInstances = new ArrayList<>();

            for(SetItemAgePacket setItemAgePacket : copy){
                if(!ClientNetworking.attemptSet(setItemAgePacket) && setItemAgePacket.attempts() < 5){
                    retryInstances.add(new SetItemAgePacket(setItemAgePacket.entityId(), setItemAgePacket.itemAge(), setItemAgePacket.attempts() + 1));
                }
            }
        });
    }

    public static boolean attemptSet(SetItemAgePacket setItemAgePacket){
        ClientWorld world = MinecraftClient.getInstance().world;
        if(world == null) return false;

        Entity itemEntity = world.getEntityById(setItemAgePacket.entityId());

        if(itemEntity == null){
            ItemDespawnTimer.LOGGER.info("Failed to set item age for entity {}, entity does not exist on the client", setItemAgePacket.entityId());
            return false;
        } else if(itemEntity instanceof ItemEntity){
            ((ItemEntityAccessInterface)itemEntity).item_despawn_timer$setModItemAge(setItemAgePacket.itemAge());
            return true;
        }

        // Should only reach this when an entity with the specified id exists, but it isn't an item entity
        // And I have to return true otherwise it would attempt to set again in the next tick
        return true;
    }
}

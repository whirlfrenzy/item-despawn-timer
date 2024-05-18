package me.whirlfrenzy.itemdespawntimer.networking;

import me.whirlfrenzy.itemdespawntimer.ItemDespawnTimer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientChunkEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class PacketReceiver {
    public static ArrayList<SetItemAgePacket> retryInstances = new ArrayList<>();

    public static void initialize(){
        PayloadTypeRegistry.playS2C().register(SetItemAgePacket.PACKET_ID, SetItemAgePacket.PACKET_CODEC);
        ClientPlayNetworking.registerGlobalReceiver(SetItemAgePacket.PACKET_ID, ((payload, context) -> {
            context.client().execute(() -> {
                retryInstances.add(payload);
            });
        }));

        ClientTickEvents.END_WORLD_TICK.register((world) ->{
            ArrayList<SetItemAgePacket> copy = (ArrayList<SetItemAgePacket>) retryInstances.clone();
            retryInstances = new ArrayList<>();

            for(SetItemAgePacket setItemAgePacket : copy){
                if(!setItemAgePacket.attemptSet() && setItemAgePacket.attempts() < 5){
                    retryInstances.add(new SetItemAgePacket(setItemAgePacket.entityId(), setItemAgePacket.itemAge(), setItemAgePacket.attempts() + 1));
                }
            }
        });
    }
}

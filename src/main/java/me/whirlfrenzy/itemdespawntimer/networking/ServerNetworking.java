package me.whirlfrenzy.itemdespawntimer.networking;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;

public class ServerNetworking {
    public static void initialize(){
        PayloadTypeRegistry.playS2C().register(SetItemAgePacket.PACKET_ID, SetItemAgePacket.PACKET_CODEC);
    }
}

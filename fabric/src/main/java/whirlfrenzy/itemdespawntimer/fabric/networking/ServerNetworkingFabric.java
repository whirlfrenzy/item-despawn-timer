package whirlfrenzy.itemdespawntimer.fabric.networking;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import whirlfrenzy.itemdespawntimer.networking.SetItemAgePacket;
import whirlfrenzy.itemdespawntimer.networking.SetItemLifespanPacket;

public class ServerNetworkingFabric {
    public static void initialize(){
        PayloadTypeRegistry.playS2C().register(SetItemAgePacket.PACKET_ID, SetItemAgePacket.PACKET_CODEC);
        PayloadTypeRegistry.playS2C().register(SetItemLifespanPacket.PACKET_ID, SetItemLifespanPacket.PACKET_CODEC);
    }
}

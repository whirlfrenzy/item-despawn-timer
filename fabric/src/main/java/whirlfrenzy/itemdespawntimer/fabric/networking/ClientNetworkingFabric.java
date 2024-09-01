package whirlfrenzy.itemdespawntimer.fabric.networking;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import whirlfrenzy.itemdespawntimer.networking.ClientNetworking;
import whirlfrenzy.itemdespawntimer.networking.SetItemAgePacket;
import whirlfrenzy.itemdespawntimer.networking.SetItemLifespanPacket;

public class ClientNetworkingFabric {
    public static void initialize(){
        ClientPlayNetworking.registerGlobalReceiver(SetItemAgePacket.PACKET_ID, ((packet, context) -> {
            context.client().execute(() -> {
                ClientNetworking.addItemDataPacket(packet);
            });
        }));

        ClientPlayNetworking.registerGlobalReceiver(SetItemLifespanPacket.PACKET_ID, ((packet, context) -> {
            context.client().execute(() -> {
                ClientNetworking.addItemDataPacket(packet);
            });
        }));
    }
}

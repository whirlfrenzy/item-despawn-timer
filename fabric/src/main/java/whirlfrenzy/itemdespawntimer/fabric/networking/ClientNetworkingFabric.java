package whirlfrenzy.itemdespawntimer.fabric.networking;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import whirlfrenzy.itemdespawntimer.networking.ClientNetworking;
import whirlfrenzy.itemdespawntimer.networking.SetItemAgePacket;
import whirlfrenzy.itemdespawntimer.networking.SetItemLifespanPacket;

public class ClientNetworkingFabric {
    public static void initialize(){
        ClientPlayNetworking.registerGlobalReceiver(SetItemAgePacket.PACKET_ID, ((MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) -> {
            SetItemAgePacket packet = SetItemAgePacket.fromPacketByteBuffer(buf);
            client.execute(() -> {
                // Need to read the buffer outside the client.execute() to prevent a netty IllegalReferenceCountException exception
                ClientNetworking.addItemDataPacket(packet);
            });
        }));

        ClientPlayNetworking.registerGlobalReceiver(SetItemLifespanPacket.PACKET_ID, ((MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) -> {
            SetItemLifespanPacket packet = SetItemLifespanPacket.fromPacketByteBuffer(buf);
            client.execute(() -> {
                ClientNetworking.addItemDataPacket(packet);
            });
        }));
    }
}

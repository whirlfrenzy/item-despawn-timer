package whirlfrenzy.itemdespawntimer.forge.networking;

import net.minecraft.util.Identifier;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import whirlfrenzy.itemdespawntimer.ItemDespawnTimer;
import whirlfrenzy.itemdespawntimer.networking.SetItemAgePacket;
import whirlfrenzy.itemdespawntimer.networking.SetItemLifespanPacket;

public class ForgeNetworking {
    public static Identifier NETWORK_CHANNEL = ItemDespawnTimer.identifier("network_channel");
    private static final String PROTOCOL_VERSION = "2";
    public static final SimpleChannel simpleChannelInstance = NetworkRegistry.newSimpleChannel(NETWORK_CHANNEL, () -> PROTOCOL_VERSION, (protocol) -> {
        if(protocol.equals(NetworkRegistry.ABSENT.version())) return true;

        return protocol.equals("2");
    }, (protocol) -> {
        if(protocol.equals(NetworkRegistry.ABSENT.version())) return true;

        return protocol.equals("2");
    });

    public static void initialize(){
        simpleChannelInstance.registerMessage(1, SetItemAgePacket.class, SetItemAgePacket::writeToBuffer, SetItemAgePacket::fromPacketByteBuffer, (packet, contextSupplier) -> {
            contextSupplier.get().enqueueWork(() -> {
                DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> packet::attemptSet);
            });
            contextSupplier.get().setPacketHandled(true);
        });

        simpleChannelInstance.registerMessage(2, SetItemLifespanPacket.class, SetItemLifespanPacket::writeToBuffer, SetItemLifespanPacket::fromPacketByteBuffer, (packet, contextSupplier) -> {
            contextSupplier.get().enqueueWork(() -> {
                DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> packet::attemptSet);
            });
            contextSupplier.get().setPacketHandled(true);
        });
    }
}

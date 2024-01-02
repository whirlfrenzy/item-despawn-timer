package me.whirlfrenzy.itemdespawntimer.networking;

import me.whirlfrenzy.itemdespawntimer.ItemDespawnTimer;
//import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
//import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.util.Identifier;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.ArrayList;

public class PacketReceiver {
    public static Identifier NETWORK_CHANNEL = new Identifier("item-despawn-timer", "network_channel");
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel simpleChannelInstance = NetworkRegistry.newSimpleChannel(NETWORK_CHANNEL, () -> PROTOCOL_VERSION, (protocol) -> true, (protocol) -> true);

    public static ArrayList<SetItemAgeInstance> retryInstances = new ArrayList<>();

    public static void addSetRetry(SetItemAgeInstance setItemAgeInstance){
        ItemDespawnTimer.LOGGER.info("Added packet of ItemEntity id "+ setItemAgeInstance.getEntityId() + " into the try-again-next-tick list");
        retryInstances.add(setItemAgeInstance);
    }

    public static void initialize(){
        simpleChannelInstance.registerMessage(1, SetItemAgeInstance.class, (setItemAgeInstance, packetByteBuf) -> {
            packetByteBuf.writeInt(setItemAgeInstance.getEntityId());
            packetByteBuf.writeInt(setItemAgeInstance.getItemAge());
            ItemDespawnTimer.LOGGER.info("Writing packet byte finished");
        }, SetItemAgeInstance::new, (setItemAgeInstance, contextSupplier) -> {
            contextSupplier.get().enqueueWork(() -> {
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> setItemAgeInstance::attemptSet);
                ItemDespawnTimer.LOGGER.info("Enqueed client to run attemptSet");
            });
        });
    }
}

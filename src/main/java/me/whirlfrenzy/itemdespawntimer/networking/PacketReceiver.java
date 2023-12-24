package me.whirlfrenzy.itemdespawntimer.networking;

import me.whirlfrenzy.itemdespawntimer.ItemDespawnTimer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.util.Identifier;

import java.util.ArrayList;

public class PacketReceiver {

    public static Identifier ITEM_AGE_PACKET_IDENTIIER = new Identifier("item-despawn-timer", "item-age");

    public static ArrayList<SetItemAgeInstance> retryInstances = new ArrayList<>();

    public static void addSetRetry(SetItemAgeInstance setItemAgeInstance){
        ItemDespawnTimer.LOGGER.info("Added packet of ItemEntity id "+ setItemAgeInstance.getEntityId() + " into the try-again-next-tick list");
        retryInstances.add(setItemAgeInstance);
    }

    public static void initialize(){
        ClientPlayNetworking.registerGlobalReceiver(ITEM_AGE_PACKET_IDENTIIER, (client, handler, buf, responseSender) -> {
            SetItemAgeInstance setItemAgeInstance = new SetItemAgeInstance(buf);

            client.execute(setItemAgeInstance::attemptSet);
        });

        ClientTickEvents.END_WORLD_TICK.register((world) ->{
            ArrayList<SetItemAgeInstance> copy = (ArrayList<SetItemAgeInstance>) retryInstances.clone();
            retryInstances = new ArrayList<>();

            for (SetItemAgeInstance setItemAgeInstance : copy) {
                ItemDespawnTimer.LOGGER.info("Attempting to set item age again for ItemEntity id "+ setItemAgeInstance.getEntityId());
                setItemAgeInstance.attemptSet();
            }
        });
    }
}

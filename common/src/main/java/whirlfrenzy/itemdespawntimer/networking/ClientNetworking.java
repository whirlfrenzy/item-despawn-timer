package whirlfrenzy.itemdespawntimer.networking;

import whirlfrenzy.itemdespawntimer.ItemDespawnTimer;

import java.util.ArrayList;

public class ClientNetworking {
    private static ArrayList<ItemDataPacket> queuedItemDataPackets = new ArrayList<>();

    public static void performQueuedSetInstances(){
        ArrayList<ItemDataPacket> itemDataPackets = (ArrayList<ItemDataPacket>) queuedItemDataPackets.clone();
        queuedItemDataPackets = new ArrayList<>();

        for(ItemDataPacket packet : itemDataPackets){
            if(!packet.attemptSet() && packet.getAttempts() < 5){
                ItemDespawnTimer.LOGGER.warn("Failed set item data packet {} for entity id {}, adding back into queue", packet.getId().id().toString(), packet.getEntityId());
                queuedItemDataPackets.add(packet.createNextAttempt());
            }
        }
    }

    public static void addItemDataPacket(ItemDataPacket packet){
        queuedItemDataPackets.add(packet);
    }

}

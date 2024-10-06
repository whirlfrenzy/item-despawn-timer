package whirlfrenzy.itemdespawntimer.networking;

import whirlfrenzy.itemdespawntimer.ItemDespawnTimer;

import java.util.ArrayList;

public class ClientNetworking {
    private static ArrayList<ItemDataPacket> queuedItemDataPackets = new ArrayList<>();

    public static void performQueuedSetInstances(){
        ArrayList<ItemDataPacket> itemDataPackets = (ArrayList<ItemDataPacket>) queuedItemDataPackets.clone();
        queuedItemDataPackets = new ArrayList<>();

        for(ItemDataPacket packet : itemDataPackets){
            if(!packet.attemptSet()){
                if(packet.getAttempts() < 5) {
                    queuedItemDataPackets.add(packet.createNextAttempt());
                } else {
                    ItemDespawnTimer.LOGGER.warn("Dropping item data packet {} for entity id {}, since it failed to apply after 5 attempts", packet.getId().id().toString(), packet.getEntityId());
                }
            }
        }
    }

    public static void addItemDataPacket(ItemDataPacket packet){
        queuedItemDataPackets.add(packet);
    }

}

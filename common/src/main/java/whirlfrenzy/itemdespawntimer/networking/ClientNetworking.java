package whirlfrenzy.itemdespawntimer.networking;

import whirlfrenzy.itemdespawntimer.ItemDespawnTimer;

import java.util.ArrayList;

public class ClientNetworking {
    private static ArrayList<SetItemAgePacket> queuedSetItemAgeInstances = new ArrayList<>();

    public static void performQueuedSetInstances(){
        ArrayList<SetItemAgePacket> itemAgeInstances = (ArrayList<SetItemAgePacket>) queuedSetItemAgeInstances.clone();
        queuedSetItemAgeInstances = new ArrayList<>();

        for(SetItemAgePacket setItemAgePacket : itemAgeInstances){
            if(!setItemAgePacket.attemptSet() && setItemAgePacket.attempts() < 5){
                ItemDespawnTimer.LOGGER.warn("Failed set item age for entity id {}, adding back into queue", setItemAgePacket.entityId());
                queuedSetItemAgeInstances.add(new SetItemAgePacket(setItemAgePacket.entityId(), setItemAgePacket.itemAge(), setItemAgePacket.attempts() + 1));
            }
        }
    }

    public static void addSetItemAgeInstance(SetItemAgePacket packet){
        queuedSetItemAgeInstances.add(packet);
    }

}

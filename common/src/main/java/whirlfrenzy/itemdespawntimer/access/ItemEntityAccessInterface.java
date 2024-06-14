package whirlfrenzy.itemdespawntimer.access;

import net.minecraft.server.network.ServerPlayerEntity;

public interface ItemEntityAccessInterface {
    boolean item_despawn_timer$getTimerLabelVisibility();

    void item_despawn_timer$setTimerLabelVisibility(boolean newTimerLabelVisibility);

    int item_despawn_timer$getModItemAge();

    void item_despawn_timer$setModItemAge(int modItemAge);

    void item_despawn_timer$sendItemAgePacket(ServerPlayerEntity player);

    void item_despawn_timer$sendItemAgePacketToNearbyPlayers();
}

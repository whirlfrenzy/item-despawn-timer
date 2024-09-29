package whirlfrenzy.itemdespawntimer.access;

import net.minecraft.server.network.ServerPlayerEntity;

public interface ItemEntityAccessInterface {
    boolean item_despawn_timer$getLabelVisibility();

    void item_despawn_timer$setLabelVisibility(boolean visible);

    int item_despawn_timer$getModItemAge();

    void item_despawn_timer$setModItemAge(int itemAge);

    void item_despawn_timer$sendItemAgePacket(ServerPlayerEntity player);

    void item_despawn_timer$sendItemAgePacketToNearbyPlayers();

    int item_despawn_timer$getOverriddenLifespanOrModItemLifespan();

    int item_despawn_timer$getModItemLifespan();

    void item_despawn_timer$setModItemLifespan(int itemLifespan);

    void item_despawn_timer$sendItemLifespanPacket(ServerPlayerEntity player);

    void item_despawn_timer$sendItemLifespanPacketToNearbyPlayers();
}

package me.whirlfrenzy.itemdespawntimer.access;

public interface ItemEntityAccessInterface {
    boolean item_despawn_timer$getTimerLabelVisibility();

    void item_despawn_timer$setTimerLabelVisibility(boolean newTimerLabelVisibility);

    int item_despawn_timer$getModItemAge();

    void item_despawn_timer$setModItemAge(int modItemAge);
}

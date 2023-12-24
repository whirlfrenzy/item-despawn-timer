package me.whirlfrenzy.itemdespawntimer;

import me.whirlfrenzy.itemdespawntimer.networking.PacketReceiver;
import net.fabricmc.api.ClientModInitializer;

public class ItemDespawnTimerClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        PacketReceiver.initialize();
    }
}

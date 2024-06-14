package whirlfrenzy.itemdespawntimer.fabric;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import whirlfrenzy.itemdespawntimer.fabric.networking.ServerNetworkingFabric;
import whirlfrenzy.itemdespawntimer.networking.ClientNetworking;

public final class ItemDespawnTimerFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        ServerNetworkingFabric.initialize();
    }
}

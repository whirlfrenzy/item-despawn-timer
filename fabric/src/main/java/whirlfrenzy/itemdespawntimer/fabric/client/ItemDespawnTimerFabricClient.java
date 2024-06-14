package whirlfrenzy.itemdespawntimer.fabric.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import whirlfrenzy.itemdespawntimer.fabric.networking.ClientNetworkingFabric;
import whirlfrenzy.itemdespawntimer.networking.ClientNetworking;

public final class ItemDespawnTimerFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientNetworkingFabric.initialize();
        ClientTickEvents.END_WORLD_TICK.register((world -> ClientNetworking.performQueuedSetInstances()));
    }
}

package whirlfrenzy.itemdespawntimer.fabric.client;

import net.fabricmc.api.ClientModInitializer;
import whirlfrenzy.itemdespawntimer.fabric.networking.ClientNetworkingFabric;

public final class ItemDespawnTimerFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientNetworkingFabric.initialize();
    }
}

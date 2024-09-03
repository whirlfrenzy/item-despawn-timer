package whirlfrenzy.itemdespawntimer.fabric.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import whirlfrenzy.itemdespawntimer.ItemDespawnTimer;
import whirlfrenzy.itemdespawntimer.config.ItemDespawnTimerClientConfig;
import whirlfrenzy.itemdespawntimer.fabric.networking.ClientNetworkingFabric;
import whirlfrenzy.itemdespawntimer.keybinding.ItemDespawnTimerKeybinds;
import whirlfrenzy.itemdespawntimer.networking.ClientNetworking;

public final class ItemDespawnTimerFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientNetworkingFabric.initialize();

        ClientTickEvents.END_WORLD_TICK.register(world -> {
            ClientNetworking.performQueuedSetInstances();
        });

        ItemDespawnTimer.initialize();

        KeyBindingHelper.registerKeyBinding(ItemDespawnTimerKeybinds.TOGGLE_TIMER_VISIBILITY);

        WorldRenderEvents.END.register(context -> {
            while(ItemDespawnTimerKeybinds.TOGGLE_TIMER_VISIBILITY.wasPressed()){
                ItemDespawnTimerClientConfig.timerVisible = !ItemDespawnTimerClientConfig.timerVisible;
                ItemDespawnTimerClientConfig.write(ItemDespawnTimer.MOD_ID);
            }
        });
    }
}

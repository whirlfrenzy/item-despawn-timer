package whirlfrenzy.itemdespawntimer.neoforge.client;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import whirlfrenzy.itemdespawntimer.ItemDespawnTimer;
import whirlfrenzy.itemdespawntimer.keybinding.ItemDespawnTimerKeybinds;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, modid = ItemDespawnTimer.NEOFORGE_MOD_ID)
public class ClientModBusEvents {
    @SubscribeEvent
    public static void registerKeybinds(RegisterKeyMappingsEvent event){
        ItemDespawnTimerKeybinds.initialize();
        event.register(ItemDespawnTimerKeybinds.TOGGLE_TIMER_VISIBILITY);
    }
}

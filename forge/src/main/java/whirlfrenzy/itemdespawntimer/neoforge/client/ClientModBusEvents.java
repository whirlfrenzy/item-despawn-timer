package whirlfrenzy.itemdespawntimer.neoforge.client;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import whirlfrenzy.itemdespawntimer.ItemDespawnTimer;
import whirlfrenzy.itemdespawntimer.config.ItemDespawnTimerClientConfig;
import whirlfrenzy.itemdespawntimer.keybinding.ItemDespawnTimerKeybinds;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, modid = ItemDespawnTimer.NEOFORGE_MOD_ID, value = Dist.CLIENT)
public class ClientModBusEvents {
    @SubscribeEvent
    public static void registerKeybinds(RegisterKeyMappingsEvent event){
        ItemDespawnTimerKeybinds.initialize();
        event.register(ItemDespawnTimerKeybinds.TOGGLE_TIMER_VISIBILITY);
        event.register(ItemDespawnTimerKeybinds.TOGGLE_NAME_VISIBILITY);
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event){
        ItemDespawnTimer.initialize();
        ModLoadingContext.get().registerExtensionPoint(IConfigScreenFactory.class, () -> (client, parent) -> ItemDespawnTimerClientConfig.getScreen(parent, ItemDespawnTimer.MOD_ID));
    }
}

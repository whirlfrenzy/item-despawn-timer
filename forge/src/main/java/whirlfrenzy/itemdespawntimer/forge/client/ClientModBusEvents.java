package whirlfrenzy.itemdespawntimer.forge.client;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import whirlfrenzy.itemdespawntimer.ItemDespawnTimer;
import whirlfrenzy.itemdespawntimer.config.ItemDespawnTimerClientConfig;
import whirlfrenzy.itemdespawntimer.keybinding.ItemDespawnTimerKeybinds;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = ItemDespawnTimer.FORGE_MOD_ID, value = Dist.CLIENT)
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
        ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class, () -> new ConfigScreenHandler.ConfigScreenFactory((client, parent) -> ItemDespawnTimerClientConfig.getScreen(parent, ItemDespawnTimer.MOD_ID)));

    }
}

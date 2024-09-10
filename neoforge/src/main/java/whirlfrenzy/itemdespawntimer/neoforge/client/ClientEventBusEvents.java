package whirlfrenzy.itemdespawntimer.neoforge.client;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import whirlfrenzy.itemdespawntimer.ItemDespawnTimer;
import whirlfrenzy.itemdespawntimer.config.ItemDespawnTimerClientConfig;
import whirlfrenzy.itemdespawntimer.keybinding.ItemDespawnTimerKeybinds;
import whirlfrenzy.itemdespawntimer.networking.ClientNetworking;

@EventBusSubscriber(value = Dist.CLIENT, modid = ItemDespawnTimer.NEOFORGE_MOD_ID)
public class ClientEventBusEvents {
    @SubscribeEvent
    public static void onWorldTick(LevelTickEvent.Post event){
        ClientNetworking.performQueuedSetInstances();
    }

    @SubscribeEvent
    public static void renderWorld(RenderLevelStageEvent event){
        if(event.getStage() == RenderLevelStageEvent.Stage.AFTER_LEVEL){
            while(ItemDespawnTimerKeybinds.TOGGLE_TIMER_VISIBILITY.wasPressed()){
                ItemDespawnTimerClientConfig.timerVisible = !ItemDespawnTimerClientConfig.timerVisible;
                ItemDespawnTimerClientConfig.write(ItemDespawnTimer.MOD_ID);
            }

            while(ItemDespawnTimerKeybinds.TOGGLE_NAME_VISIBILITY.wasPressed()){
                ItemDespawnTimerClientConfig.nameVisible = !ItemDespawnTimerClientConfig.nameVisible;
                ItemDespawnTimerClientConfig.write(ItemDespawnTimer.MOD_ID);
            }
        }
    }
}

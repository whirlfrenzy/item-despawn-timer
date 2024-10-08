package whirlfrenzy.itemdespawntimer.forge.client;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import whirlfrenzy.itemdespawntimer.ItemDespawnTimer;
import whirlfrenzy.itemdespawntimer.config.ItemDespawnTimerClientConfig;
import whirlfrenzy.itemdespawntimer.keybinding.ItemDespawnTimerKeybinds;
import whirlfrenzy.itemdespawntimer.networking.ClientNetworking;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = ItemDespawnTimer.FORGE_MOD_ID)
public class ClientEventBusEvents {
    @SubscribeEvent
    public static void onWorldTick(TickEvent.LevelTickEvent event){
        if(event.phase == TickEvent.Phase.END) {
            ClientNetworking.performQueuedSetInstances();
        }
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

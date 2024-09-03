package whirlfrenzy.itemdespawntimer.fabric.client;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import whirlfrenzy.itemdespawntimer.ItemDespawnTimer;
import whirlfrenzy.itemdespawntimer.config.ItemDespawnTimerClientConfig;

public class ModMenuApiImpl implements ModMenuApi {
    public ConfigScreenFactory<?> getModConfigScreenFactory(){
        return parent -> ItemDespawnTimerClientConfig.getScreen(parent, ItemDespawnTimer.MOD_ID);
    }
}

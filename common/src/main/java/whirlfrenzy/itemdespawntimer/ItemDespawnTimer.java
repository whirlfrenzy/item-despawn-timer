package whirlfrenzy.itemdespawntimer;

import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import whirlfrenzy.itemdespawntimer.config.ItemDespawnTimerClientConfig;
import whirlfrenzy.itemdespawntimer.keybinding.ItemDespawnTimerKeybinds;

public final class ItemDespawnTimer {
    public static final String MOD_ID = "item-despawn-timer";
    public static final String FORGE_MOD_ID = "item_despawn_timer"; // Forge forbids hyphens in mod ids and I prefer using hyphens than underscores when possible
    public static Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static Identifier identifier(String path){
        return Identifier.of(MOD_ID, path);
    }

    public static void initialize(){
        ItemDespawnTimerClientConfig.init(ItemDespawnTimer.MOD_ID, ItemDespawnTimerClientConfig.class);
        ItemDespawnTimerKeybinds.initialize();
    }
}

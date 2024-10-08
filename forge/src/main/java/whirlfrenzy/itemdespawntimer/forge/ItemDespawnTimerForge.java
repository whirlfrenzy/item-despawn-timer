package whirlfrenzy.itemdespawntimer.forge;

import net.minecraftforge.fml.common.Mod;
import whirlfrenzy.itemdespawntimer.ItemDespawnTimer;
import whirlfrenzy.itemdespawntimer.forge.networking.ForgeNetworking;

@Mod(ItemDespawnTimer.FORGE_MOD_ID)
public final class ItemDespawnTimerForge {
    public ItemDespawnTimerForge() {
        ForgeNetworking.initialize();
    }
}

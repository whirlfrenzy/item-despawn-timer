package me.whirlfrenzy.itemdespawntimer;

import me.whirlfrenzy.itemdespawntimer.networking.PacketReceiver;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(ItemDespawnTimer.MOD_ID)
public class ItemDespawnTimer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger("item-despawn-timer");
	public static final String MOD_ID = "item_despawn_timer";

	public ItemDespawnTimer(){
		PacketReceiver.initialize();
	}
}
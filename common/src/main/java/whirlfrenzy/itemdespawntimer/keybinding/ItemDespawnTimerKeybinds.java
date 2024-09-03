package whirlfrenzy.itemdespawntimer.keybinding;

import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

public class ItemDespawnTimerKeybinds {
    public static KeyBinding TOGGLE_TIMER_VISIBILITY = new KeyBinding("key.item-despawn-timer.toggle_timer_visibility", InputUtil.GLFW_KEY_F6, "key.item-despawn-timer.category");

    public static void initialize(){}
}

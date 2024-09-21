package whirlfrenzy.itemdespawntimer.config;

import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class ItemDespawnTimerClientConfig extends MidnightConfig {
    @Entry
    public static boolean timerVisible = true;

    @Entry(min = -0.5, max = 5)
    public static float timerLabelHeight = 0.75f;

    @Entry
    public static boolean nameVisible = false;

    @Entry(min = -0.5, max = 5)
    public static float nameLabelHeight = 1.25f;

    @Entry
    public static boolean useWhitelist = false;

    @Entry
    public static boolean whitelistIsBlacklist = false;

    @Entry
    public static List<Identifier> whitelistedItems = new ArrayList<>();
}

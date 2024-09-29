package whirlfrenzy.itemdespawntimer.config;

import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ItemDespawnTimerClientConfig extends MidnightConfig {

    @Comment(centered = true)
    public static Comment labelSectionTitle;

    @Entry
    public static boolean timerVisible = true;

    @Entry(min = -0.5, max = 5)
    public static float timerLabelHeight = 0.75f;

    @Entry
    public static boolean nameVisible = false;

    @Entry(min = -0.5, max = 5)
    public static float nameLabelHeight = 1.1f;

    @Comment(centered = true)
    public static Comment whitelistSectionTitle;
    @Comment
    public static Comment whitelistSectionNotice;

    @Entry
    public static boolean useWhitelist = false;

    @Entry
    public static boolean whitelistIsBlacklist = false;

    @Entry
    public static List<Identifier> whitelistedItems = new ArrayList<>();

    @Comment(centered = true)
    public static Comment timeOverridesSectionTitle;
    @Comment
    public static Comment timeOverridesSectionNotice;
    @Comment
    public static Comment timeOverridesSectionNotice2;
    @Comment
    public static Comment timeOverridesSectionNotice3;
    @Comment
    public static Comment timeOverridesSectionNotice4;

    @Entry
    public static boolean useTimeOverrides = false;

    @Entry
    public static Map<Identifier, Integer> timeOverrides = new LinkedHashMap<>();
}

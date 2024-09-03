package whirlfrenzy.itemdespawntimer.config;

public class ItemDespawnTimerClientConfig extends MidnightConfig {
    @Entry
    public static boolean timerVisible = true;

    @Entry(min = -0.5, max = 5)
    public static float timerLabelHeight = 0.75f;
}

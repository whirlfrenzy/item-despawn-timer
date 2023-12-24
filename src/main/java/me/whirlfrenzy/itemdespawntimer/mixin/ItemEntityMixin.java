package me.whirlfrenzy.itemdespawntimer.mixin;

import me.whirlfrenzy.itemdespawntimer.access.ItemEntityAccessInterface;
import me.whirlfrenzy.itemdespawntimer.networking.PacketReceiver;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin implements ItemEntityAccessInterface {
    @Shadow public abstract int getItemAge();

    @Unique
    boolean timerLabelVisibility = true;

    @Override
    public boolean item_despawn_timer$getTimerLabelVisibility() {
        return this.timerLabelVisibility;
    }

    @Override
    public void item_despawn_timer$setTimerLabelVisibility(boolean newTimerLabelVisibility) {
        this.timerLabelVisibility = newTimerLabelVisibility;
    }

    public void onStartedTrackingBy(ServerPlayerEntity player) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeInt(((Entity)(Object)(this)).getId());
        buf.writeInt(this.getItemAge());
        ServerPlayNetworking.send(player, PacketReceiver.ITEM_AGE_PACKET_IDENTIIER, buf);
    }
}

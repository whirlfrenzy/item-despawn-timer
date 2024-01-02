package me.whirlfrenzy.itemdespawntimer.mixin;

import me.whirlfrenzy.itemdespawntimer.ItemDespawnTimer;
import me.whirlfrenzy.itemdespawntimer.access.ItemEntityAccessInterface;
import me.whirlfrenzy.itemdespawntimer.networking.PacketReceiver;
//import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
//import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import me.whirlfrenzy.itemdespawntimer.networking.SetItemAgeInstance;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraftforge.network.PacketDistributor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin extends EntityMixin implements ItemEntityAccessInterface {
    @Shadow public abstract int getItemAge();

    @Shadow private int itemAge;
    @Unique
    boolean timerLabelVisibility = true;

    // -1 here is to mark that the previous tick's item age hasn't been set yet. Apparently checking if it's null is a bit weird or I'm just stupid
    // Only used on the server in the tick method
    @Unique
    int previousTickItemAge = -1;

    // I would like to use the itemAge field provided by the ItemEntity class, but I do want to avoid interfering with any other mod relying on that same field
    // + Minecraft uses that field to calculate the rotation and setting that field to a new value more than 1 away from the previous value causes a noticeable "lag back"
    // This is only used on the client by the way, the server will send packets to the client to update this field if necessary
    @Unique
    int modItemAge = 0;
    @Unique
    ArrayList<ServerPlayerEntity> playersInRange = new ArrayList<>();

    @Override
    public boolean item_despawn_timer$getTimerLabelVisibility() {
        return this.timerLabelVisibility;
    }

    @Override
    public void item_despawn_timer$setTimerLabelVisibility(boolean newTimerLabelVisibility) {
        this.timerLabelVisibility = newTimerLabelVisibility;
    }

    public int item_despawn_timer$getModItemAge(){
        return this.modItemAge;
    };

    public void item_despawn_timer$setModItemAge(int modItemAge){
        this.modItemAge = modItemAge;
    };

    @Unique
    public void item_despawn_timer$sendItemAgePacket(ServerPlayerEntity player){
        PacketReceiver.simpleChannelInstance.send(PacketDistributor.PLAYER.with(() -> player), new SetItemAgeInstance((ItemEntity) (Object)this));
    }

    @Override
    public void onStartedTrackingBy(ServerPlayerEntity player, CallbackInfo ci) {
        item_despawn_timer$sendItemAgePacket(player);
        playersInRange.add(player);
    }

    @Override
    public void onStoppedTrackingBy(ServerPlayerEntity player, CallbackInfo ci){
        playersInRange.remove(player);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    public void tick(CallbackInfo ci) {
        if(!this.getWorld().isClient()) {
            if (previousTickItemAge != -1 && previousTickItemAge != (this.getItemAge() - 1) && previousTickItemAge != -32768){
                for (ServerPlayerEntity player : playersInRange) {
                    item_despawn_timer$sendItemAgePacket(player);
                }
            }

            previousTickItemAge = this.getItemAge();
        } else {
            ++this.modItemAge;
        }
    }
}

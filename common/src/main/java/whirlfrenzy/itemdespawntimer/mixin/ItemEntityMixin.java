package whirlfrenzy.itemdespawntimer.mixin;

import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import whirlfrenzy.itemdespawntimer.ItemDespawnTimer;
import whirlfrenzy.itemdespawntimer.PlatformSpecificHelper;
import whirlfrenzy.itemdespawntimer.access.ItemEntityAccessInterface;
import net.minecraft.entity.ItemEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import whirlfrenzy.itemdespawntimer.networking.SetItemAgePacket;

import java.util.ArrayList;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin extends EntityMixin implements ItemEntityAccessInterface {
    @Shadow public abstract int getItemAge();

    @Shadow private int itemAge;

    @Shadow public abstract ItemStack getStack();

    @Unique
    boolean item_despawn_timer$timerLabelVisibility = true;

    // Only used on the server in the tick mixin below
    @Unique
    int item_despawn_timer$previousTickItemAge = 0;

    // I would like to use the itemAge field provided by the ItemEntity class, but I do want to avoid interfering with any other mod relying on that same field
    // + Minecraft uses that field to calculate the rotation and setting that field to a new value more than 1 away from the previous value causes a noticeable "lag back"
    // This is only used on the client by the way, the server will send packets to the client to update this field if necessary
    @Unique
    int item_despawn_timer$modItemAge = 0;
    @Unique
    ArrayList<ServerPlayerEntity> item_despawn_timer$playersInRange = new ArrayList<>();

    @Override
    public boolean item_despawn_timer$getTimerLabelVisibility() {
        return this.item_despawn_timer$timerLabelVisibility;
    }

    @Override
    public void item_despawn_timer$setTimerLabelVisibility(boolean newTimerLabelVisibility) {
        this.item_despawn_timer$timerLabelVisibility = newTimerLabelVisibility;
    }

    @Override
    public int item_despawn_timer$getModItemAge(){
        return this.item_despawn_timer$modItemAge;
    };

    @Override
    public void item_despawn_timer$setModItemAge(int modItemAge){
        this.item_despawn_timer$modItemAge = modItemAge;
    };

    @Override
    public void item_despawn_timer$sendItemAgePacket(ServerPlayerEntity player){
        PlatformSpecificHelper.sendPacketToPlayer(player, new SetItemAgePacket(((ItemEntity)(Object)this).getId(), itemAge, 0));
    }

    @Override
    public void item_despawn_timer$sendItemAgePacketToNearbyPlayers(){
        for(ServerPlayerEntity player : item_despawn_timer$playersInRange){
            item_despawn_timer$sendItemAgePacket(player);
        }
    }

    @Override
    public void onStartedTrackingBy(ServerPlayerEntity player, CallbackInfo ci) {
        item_despawn_timer$sendItemAgePacket(player);
        item_despawn_timer$playersInRange.add(player);
    }

    @Override
    public void onStoppedTrackingBy(ServerPlayerEntity player, CallbackInfo ci){
        item_despawn_timer$playersInRange.remove(player);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    public void tick(CallbackInfo ci) {
        if(!((ItemEntity)(Object)this).getWorld().isClient()) {
            if (item_despawn_timer$previousTickItemAge != (this.getItemAge() - 1) && item_despawn_timer$previousTickItemAge != -32768){
                item_despawn_timer$sendItemAgePacketToNearbyPlayers();
            }

            item_despawn_timer$previousTickItemAge = this.getItemAge();
        } else {
            ++this.item_despawn_timer$modItemAge;
        }
    }
}

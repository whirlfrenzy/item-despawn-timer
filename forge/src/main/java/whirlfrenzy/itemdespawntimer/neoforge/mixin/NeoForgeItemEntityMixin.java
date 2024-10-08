package whirlfrenzy.itemdespawntimer.neoforge.mixin;

import net.minecraft.entity.ItemEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import whirlfrenzy.itemdespawntimer.access.ItemEntityAccessInterface;
import whirlfrenzy.itemdespawntimer.mixin.ItemEntityMixin;

@Mixin(ItemEntity.class)
public class NeoForgeItemEntityMixin {
    @Shadow public int lifespan;

    @Inject(method = "tick", at = @At("TAIL"))
    public void lifespanChangeDetector(CallbackInfo ci){
        if(!((ItemEntity)(Object)this).getWorld().isClient()){
            if(((ItemEntityAccessInterface)(Object)this).item_despawn_timer$getModItemLifespan() != ((ItemEntity)(Object)this).lifespan){
                ((ItemEntityAccessInterface)(Object)this).item_despawn_timer$setModItemLifespan(((ItemEntity)(Object)this).lifespan);
                ((ItemEntityAccessInterface)(Object)this).item_despawn_timer$sendItemLifespanPacketToNearbyPlayers();
            }
        }
    }
}

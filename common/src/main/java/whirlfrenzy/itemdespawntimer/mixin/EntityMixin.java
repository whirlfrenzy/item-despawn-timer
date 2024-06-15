package whirlfrenzy.itemdespawntimer.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin {
    // Needed to get the onStartedTrackingBy mixin in ItemEntityMixin to work outside the dev environment
    // + ItemEntity doesn't override the onStartedTrackingBy method
    @Inject(method = "onStartedTrackingBy", at = @At("HEAD"))
    public void onStartedTrackingBy(ServerPlayerEntity player, CallbackInfo ci) {}

    @Inject(method = "onStoppedTrackingBy", at = @At("HEAD"))
    public void onStoppedTrackingBy(ServerPlayerEntity player, CallbackInfo ci) {}
}

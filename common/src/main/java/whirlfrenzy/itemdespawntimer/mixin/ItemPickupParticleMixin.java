package whirlfrenzy.itemdespawntimer.mixin;

import whirlfrenzy.itemdespawntimer.access.ItemEntityAccessInterface;
import net.minecraft.client.particle.ItemPickupParticle;
import net.minecraft.client.render.BufferBuilderStorage;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemPickupParticle.class)
public class ItemPickupParticleMixin {
    @Inject(at = @At("TAIL"), method = "<init>(Lnet/minecraft/client/render/entity/EntityRenderDispatcher;Lnet/minecraft/client/render/BufferBuilderStorage;Lnet/minecraft/client/world/ClientWorld;Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Vec3d;)V")
    public void disableLabelRendering(EntityRenderDispatcher dispatcher, BufferBuilderStorage bufferStorage, ClientWorld world, Entity itemEntity, Entity interactingEntity, Vec3d velocity, CallbackInfo ci){
        if(itemEntity instanceof ItemEntity) {
            ((ItemEntityAccessInterface) ((ItemPickupParticleAccessor) this).getItemEntity()).item_despawn_timer$setLabelVisibility(false);
        }
    }
}

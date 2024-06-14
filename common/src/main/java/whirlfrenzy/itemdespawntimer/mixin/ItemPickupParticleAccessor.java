package whirlfrenzy.itemdespawntimer.mixin;

import net.minecraft.client.particle.ItemPickupParticle;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ItemPickupParticle.class)
public interface ItemPickupParticleAccessor {
    @Accessor
    Entity getItemEntity();
}

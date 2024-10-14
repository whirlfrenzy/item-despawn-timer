package whirlfrenzy.itemdespawntimer.mixin;

import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.ItemEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.ItemEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import whirlfrenzy.itemdespawntimer.rendering.LabelRenderer;

// Priority is set to 999 (one below the default) to still render the labels in case any mods use ci.cancel() (such as Item Physic Full and Lite)
// Higher priorities get added first and thus my mixin should be added after Item Physic's mixin and end up above it
@Mixin(value = ItemEntityRenderer.class, priority = 300)
public abstract class ItemEntityRendererMixin {
    @Inject(at = @At(value = "HEAD"), method = "render(Lnet/minecraft/entity/ItemEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V")
    public void renderTextLabels(ItemEntity itemEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int lightLevel, CallbackInfo ci) {
        LabelRenderer.renderTextLabels(itemEntity, matrixStack, vertexConsumerProvider, lightLevel);
    }
}

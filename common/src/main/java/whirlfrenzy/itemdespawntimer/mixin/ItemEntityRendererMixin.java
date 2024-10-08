package whirlfrenzy.itemdespawntimer.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import whirlfrenzy.itemdespawntimer.ItemDespawnTimer;
import whirlfrenzy.itemdespawntimer.access.ItemEntityAccessInterface;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.ItemEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.ItemEntity;
import net.minecraft.text.Text;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import whirlfrenzy.itemdespawntimer.config.ItemDespawnTimerClientConfig;

@Mixin(ItemEntityRenderer.class)
public abstract class ItemEntityRendererMixin {
    @Inject(at = @At(value = "TAIL"), method = "render(Lnet/minecraft/entity/ItemEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V")
    public void renderTextLabels(ItemEntity itemEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {
        if(!((ItemEntityAccessInterface)itemEntity).item_despawn_timer$getLabelVisibility()) return;

        if(ItemDespawnTimerClientConfig.useWhitelist){
            Identifier itemId = Registries.ITEM.getId(itemEntity.getStack().getItem());
            if(ItemDespawnTimerClientConfig.whitelistIsBlacklist){
                if(ItemDespawnTimerClientConfig.whitelistedItems.contains(itemId)) return;
            } else {
                if(!ItemDespawnTimerClientConfig.whitelistedItems.contains(itemId)) return;
            }
        }

        // TODO: Investigate ItemEntity as well as other transparency effects not rendering behind the label

        TextRenderer textRenderer = ((EntityRendererAccessor)this).getTextRenderer();
        int textBackgroundOpacity = (int)(MinecraftClient.getInstance().options.getTextBackgroundOpacity(0.25f) * 255.0f) << 24;

        if(ItemDespawnTimerClientConfig.timerVisible){
            // The remaining seconds label
            matrixStack.push();
            matrixStack.translate(0, ItemDespawnTimerClientConfig.timerLabelHeight, 0);
            matrixStack.multiply(((EntityRendererAccessor)this).getDispatcher().getRotation());
            matrixStack.scale(-0.025f,-0.025f, 0.025f);

            Matrix4f matrix4f = matrixStack.peek().getPositionMatrix();
            matrix4f.translate(4.0F, 0F, 0F);

            Text text;

            int modItemAge = ((ItemEntityAccessInterface)itemEntity).item_despawn_timer$getModItemAge();

            if(modItemAge != -32768){
                text = Text.literal(Math.max(0, ((int)Math.ceil(((float) ((ItemEntityAccessInterface)itemEntity).item_despawn_timer$getOverriddenLifespanOrModItemLifespan() - (float) modItemAge) / 20))) + "s");
            } else {
                text = Text.literal("∞");
            }

            float negativeHalfOfTextWidth = (float) -textRenderer.getWidth(text) / 2;

            textRenderer.draw(text, negativeHalfOfTextWidth, 0, 0x0FFFFFFF, false, matrix4f, vertexConsumerProvider, TextRenderer.TextLayerType.NORMAL, textBackgroundOpacity, i);
            textRenderer.draw(text, negativeHalfOfTextWidth, 0, -1, false, matrix4f, vertexConsumerProvider, TextRenderer.TextLayerType.NORMAL, 0, i);

            // Timer icon
            float timerIconOffset = (float) -textRenderer.getWidth(text) / 2 - 10;
            matrix4f.translate(new Vector3f(timerIconOffset, 0.0F, 0.0F));

            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder buffer = tessellator.getBuffer();

            buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
            buffer.vertex(matrix4f, 0,0,0).color(1.0F,1.0F,1.0F,1.0F).texture(0.0F, 0.0F).next();
            buffer.vertex(matrix4f, 0,7,0).color(1.0F,1.0F,1.0F,1.0F).texture(0.0F, 1.0F).next();
            buffer.vertex(matrix4f, 7,7,0).color(1.0F,1.0F,1.0F,1.0F).texture(1.0F, 1.0F).next();
            buffer.vertex(matrix4f, 7,0,0).color(1.0F,1.0F,0F,1.0F).texture(1.0F, 0.0F).next();

            RenderSystem.setShader(GameRenderer::getPositionTexProgram);
            RenderSystem.setShaderTexture(0, ItemDespawnTimer.identifier("clock.png"));
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

            RenderSystem.enableDepthTest();

            tessellator.draw();
            matrixStack.pop();
        }

        if(ItemDespawnTimerClientConfig.nameVisible){
            // The name label, which is just the item stack's name
            matrixStack.push();
            matrixStack.translate(0, ItemDespawnTimerClientConfig.nameLabelHeight, 0);
            matrixStack.multiply(((EntityRendererAccessor)this).getDispatcher().getRotation());
            matrixStack.scale(-0.025f,-0.025f, 0.025f);

            Matrix4f matrix4f = matrixStack.peek().getPositionMatrix();
            matrix4f.translate(0.0F, 0F, 0F);

            float negativeHalfOfTextWidth = (float) -textRenderer.getWidth(itemEntity.getStack().getName()) / 2;

            textRenderer.draw(itemEntity.getStack().getName(), negativeHalfOfTextWidth, 0, 0x0FFFFFFF, false, matrix4f, vertexConsumerProvider, TextRenderer.TextLayerType.NORMAL, textBackgroundOpacity, i);
            matrix4f.translate(0F,0F, 0.03F); // Fix z fighting issue like with the timer label
            textRenderer.draw(itemEntity.getStack().getName(), negativeHalfOfTextWidth, 0.5F, -1, false, matrix4f, vertexConsumerProvider, TextRenderer.TextLayerType.NORMAL, 0, i);
            matrixStack.pop();
        }
    }
}

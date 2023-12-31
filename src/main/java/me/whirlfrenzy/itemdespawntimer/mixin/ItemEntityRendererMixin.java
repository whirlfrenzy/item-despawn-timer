package me.whirlfrenzy.itemdespawntimer.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import me.whirlfrenzy.itemdespawntimer.access.ItemEntityAccessInterface;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.ItemEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.ItemEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntityRenderer.class)
public abstract class ItemEntityRendererMixin {
    @Inject(at = @At(value = "TAIL"), method = "render(Lnet/minecraft/entity/ItemEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V")
    public void renderTimerText(ItemEntity itemEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {
        if(!((ItemEntityAccessInterface)itemEntity).item_despawn_timer$getTimerLabelVisibility()){
            return;
        }
        // The remaining seconds label
        // TODO: Investigate ItemEntity as well as other transparency effects not rendering behind the label
        matrixStack.push();
        matrixStack.translate(0.0F, itemEntity.getHeight() + 0.75f, 0.0);
        matrixStack.multiply(((EntityRendererAccessor)this).getDispatcher().getRotation());
        matrixStack.scale(-0.025f,-0.025f, 0.025f);

        Matrix4f matrix4f = matrixStack.peek().getPositionMatrix();
        matrix4f.translate(new Vector3f(4.0F, 0.0F, 0.0F));

        TextRenderer textRenderer = ((EntityRendererAccessor)this).getTextRenderer();
        Text text;

        int modItemAge = ((ItemEntityAccessInterface)itemEntity).item_despawn_timer$getModItemAge();

        if(modItemAge != -32768){
            text = Text.literal(Math.max(0, ((int)Math.ceil(((float) 6000 - (float) modItemAge) / 20))) + "s");
        } else {
            text = Text.literal("∞");
        }

        float negativeHalfOfTextWidth = (float) -textRenderer.getWidth(text) / 2;
        float textBackgroundOpacity = MinecraftClient.getInstance().options.getTextBackgroundOpacity(0.25f);
        int j = (int)(textBackgroundOpacity * 255.0f) << 24;

        textRenderer.draw(text, negativeHalfOfTextWidth, 0, 0x20FFFFFF, false, matrix4f, vertexConsumerProvider, TextRenderer.TextLayerType.NORMAL, j, i);
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
        RenderSystem.setShaderTexture(0, new Identifier("item-despawn-timer", "clock.png"));
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        // Necessary for the icon to draw behind blocks. I thought this was enabled already, but it wasn't. Oh well, it only took a little while to find out lol
        RenderSystem.enableDepthTest();

        tessellator.draw();
        matrixStack.pop();
    }
}

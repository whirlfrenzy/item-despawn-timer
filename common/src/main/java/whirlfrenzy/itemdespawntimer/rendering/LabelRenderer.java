package whirlfrenzy.itemdespawntimer.rendering;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.EntityAttachmentType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.registry.Registries;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import whirlfrenzy.itemdespawntimer.ItemDespawnTimer;
import whirlfrenzy.itemdespawntimer.access.ItemEntityAccessInterface;
import whirlfrenzy.itemdespawntimer.config.ItemDespawnTimerClientConfig;

public class LabelRenderer {
    public static void renderTextLabels(ItemEntity itemEntity, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int lightLevel) {
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

        Vec3d labelPosition = itemEntity.getAttachments().getPointNullable(EntityAttachmentType.NAME_TAG, 0, itemEntity.getYaw());
        if(labelPosition == null){
            labelPosition = new Vec3d(0,0,0);
        }

        int textBackgroundOpacity = (int)(MinecraftClient.getInstance().options.getTextBackgroundOpacity(0.25f) * 255.0f) << 24;

        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;

        if(ItemDespawnTimerClientConfig.timerVisible){
            int modItemAge = ((ItemEntityAccessInterface)itemEntity).item_despawn_timer$getModItemAge();
            int remainingSeconds = Math.max(0, ((int)Math.ceil(((float) ((ItemEntityAccessInterface)itemEntity).item_despawn_timer$getOverriddenLifespanOrModItemLifespan() - (float) modItemAge) / 20)));

            Text text;

            if(modItemAge != -32768){
                text = Text.literal(remainingSeconds + "s");
            } else {
                text = Text.literal("∞");
            }

            if(ItemDespawnTimerClientConfig.useTimerLabelVisibilityThreshold){
                if(remainingSeconds <= ItemDespawnTimerClientConfig.timerLabelVisibilityThreshold){
                    renderTimerLabel(text, matrixStack, labelPosition.add(0, ItemDespawnTimerClientConfig.timerLabelHeight, 0), textBackgroundOpacity, lightLevel, textRenderer, vertexConsumerProvider);
                }
            } else {
                renderTimerLabel(text, matrixStack, labelPosition.add(0, ItemDespawnTimerClientConfig.timerLabelHeight, 0), textBackgroundOpacity, lightLevel, textRenderer, vertexConsumerProvider);
            }
        }

        if(ItemDespawnTimerClientConfig.nameVisible){
            MutableText name = itemEntity.getStack().getName().copy();

            if(ItemDespawnTimerClientConfig.applyFormattingToNameLabel) {
                name.formatted(itemEntity.getStack().getRarity().getFormatting());
                if (itemEntity.getStack().contains(DataComponentTypes.CUSTOM_NAME)) {
                    name.formatted(Formatting.ITALIC);
                }
            }

            renderNameLabel(name, matrixStack, labelPosition.add(0, ItemDespawnTimerClientConfig.nameLabelHeight, 0), textBackgroundOpacity, lightLevel, textRenderer, vertexConsumerProvider);
        }
    }

    private static void renderTimerLabel(Text text, MatrixStack matrixStack, Vec3d labelPosition, int textBackgroundOpacity, int lightLevel, TextRenderer textRenderer, VertexConsumerProvider vertexConsumerProvider){
        // The remaining seconds label
        matrixStack.push();
        matrixStack.translate(labelPosition.getX(), labelPosition.getY(), labelPosition.getZ());
        // The rotation of an ItemEntity's dispatcher seems to always be the camera's rotation
        matrixStack.multiply(MinecraftClient.getInstance().gameRenderer.getCamera().getRotation());
        matrixStack.scale(0.025f,-0.025f, 0.025f);

        Matrix4f matrix4f = matrixStack.peek().getPositionMatrix();
        matrix4f.translate(4.0F, 0F, 0F);

        float negativeHalfOfTextWidth = (float) -textRenderer.getWidth(text) / 2;

        textRenderer.draw(text, negativeHalfOfTextWidth, 0, 0x0FFFFFFF, false, matrix4f, vertexConsumerProvider, TextRenderer.TextLayerType.NORMAL, textBackgroundOpacity, lightLevel);
        matrix4f.translate(0F,0F, 0.03F); // Fix z fighting between the background and text. While I could use the background color property in the method below and get rid of the one above, it also causes the z fighting which really sucks
        textRenderer.draw(text, negativeHalfOfTextWidth, 0.5F, -1, false, matrix4f, vertexConsumerProvider, TextRenderer.TextLayerType.NORMAL, 0, lightLevel);

        // Timer icon
        float timerIconOffset = (float) -textRenderer.getWidth(text) / 2 - 10;
        matrix4f.translate(new Vector3f(timerIconOffset, 0.0F, 0.0F));

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);

        buffer.vertex(matrix4f, 0,0,0).color(1.0F,1.0F,1.0F,1.0F).texture(0.0F, 0.0F);
        buffer.vertex(matrix4f, 0,7,0).color(1.0F,1.0F,1.0F,1.0F).texture(0.0F, 1.0F);
        buffer.vertex(matrix4f, 7,7,0).color(1.0F,1.0F,1.0F,1.0F).texture(1.0F, 1.0F);
        buffer.vertex(matrix4f, 7,0,0).color(1.0F,1.0F,0F,1.0F).texture(1.0F, 0.0F);

        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderTexture(0, ItemDespawnTimer.identifier("clock.png"));
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        RenderSystem.enableDepthTest();

        BufferRenderer.drawWithGlobalProgram(buffer.end());
        tessellator.clear();
        matrixStack.pop();
    }

    private static void renderNameLabel(Text text, MatrixStack matrixStack, Vec3d labelPosition, int textBackgroundOpacity, int lightLevel, TextRenderer textRenderer, VertexConsumerProvider vertexConsumerProvider){
        // The name label, which is just the item stack's name
        matrixStack.push();
        matrixStack.translate(labelPosition.getX(), labelPosition.getY(), labelPosition.getZ());
        matrixStack.multiply(MinecraftClient.getInstance().gameRenderer.getCamera().getRotation());
        matrixStack.scale(0.025f,-0.025f, 0.025f);

        Matrix4f matrix4f = matrixStack.peek().getPositionMatrix();
        matrix4f.translate(0.0F, 0F, 0F);

        float negativeHalfOfTextWidth = (float) -textRenderer.getWidth(text) / 2;

        textRenderer.draw(text, negativeHalfOfTextWidth, 0, 0x0FFFFFFF, false, matrix4f, vertexConsumerProvider, TextRenderer.TextLayerType.NORMAL, textBackgroundOpacity, lightLevel);
        matrix4f.translate(0F,0F, 0.03F); // Fix z fighting issue like with the timer label
        textRenderer.draw(text, negativeHalfOfTextWidth, 0.5F, -1, false, matrix4f, vertexConsumerProvider, TextRenderer.TextLayerType.NORMAL, 0, lightLevel);
        matrixStack.pop();
    }
}

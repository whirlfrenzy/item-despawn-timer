package whirlfrenzy.itemdespawntimer.mixin;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EntityRenderer.class)
public interface EntityRendererAccessor {
    @Accessor
    EntityRenderDispatcher getDispatcher();

    @Accessor
    TextRenderer getTextRenderer();
}

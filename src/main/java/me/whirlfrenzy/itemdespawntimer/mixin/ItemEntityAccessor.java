package me.whirlfrenzy.itemdespawntimer.mixin;

import net.minecraft.entity.ItemEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ItemEntity.class)
public interface ItemEntityAccessor {
    @Accessor(value = "itemAge")
    void setItemAge(int itemAge);
}

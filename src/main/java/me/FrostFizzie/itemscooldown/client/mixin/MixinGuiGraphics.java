package me.FrostFizzie.itemscooldown.client.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import me.FrostFizzie.itemscooldown.Itemscooldown;
import me.FrostFizzie.itemscooldown.client.Config.Config;
import me.FrostFizzie.itemscooldown.client.SingleTimeDuration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix3x2fStack;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

@Mixin(GuiGraphics.class)
public abstract class MixinGuiGraphics {
    @Shadow @Final
    Minecraft minecraft;

    @Shadow public abstract void drawString(Font font, @Nullable String string, int i, int j, int k, boolean bl);

    @Shadow @Final private Matrix3x2fStack pose;

    @Inject(method = "renderItemCooldown", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;fill(Lcom/mojang/blaze3d/pipeline/RenderPipeline;IIIII)V", shift = At.Shift.AFTER))
    public void renderItemCooldown(ItemStack itemStack, int i, int j, CallbackInfo ci, @Local float f) {
        pose.pushMatrix();
        float scale = 0.5f;
        pose.scale(scale, scale);
        float cooldown = Itemscooldown.getCooldownTime(itemStack, f);
        this.drawString(this.minecraft.font,
                SingleTimeDuration.formatDuration(cooldown / 20f),
                (int) ((i + 1) / scale),
                (int) ((j + 1) / scale),
                Color.decode(Config.displayColor).getRGB(),
                false);
        pose.popMatrix();

    }
}

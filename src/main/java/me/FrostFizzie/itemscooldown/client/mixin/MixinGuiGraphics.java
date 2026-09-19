package me.FrostFizzie.itemscooldown.client.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import me.FrostFizzie.itemscooldown.Itemscooldown;
import me.FrostFizzie.itemscooldown.Config.Config;
import me.FrostFizzie.itemscooldown.client.SingleTimeDuration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
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

@Mixin(GuiGraphicsExtractor.class)
public abstract class MixinGuiGraphics {
    @Shadow @Final
    private Minecraft minecraft;

    @Shadow @Final private Matrix3x2fStack pose;

    @Shadow
    public abstract void text(Font font, @Nullable String str, int x, int y, int color, boolean dropShadow);

    @Inject(method = "itemCooldown", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;fill(Lcom/mojang/blaze3d/pipeline/RenderPipeline;IIIII)V", shift = At.Shift.AFTER))
    public void renderItemCooldown(ItemStack itemStack, int i, int j, CallbackInfo ci, @Local(name = "cooldown") float cooldown) {
        pose.pushMatrix();
        float scale = 0.5f;
        pose.scale(scale, scale);
        float cooldownDur = Itemscooldown.getCooldownTime(itemStack, cooldown);
        this.text(this.minecraft.font,
                SingleTimeDuration.formatDuration(cooldownDur / 20f),
                (int) ((i + 1) / scale),
                (int) ((j + 1) / scale),
                Config.displayColor.getRGB(),
                true);
        pose.popMatrix();

    }
}

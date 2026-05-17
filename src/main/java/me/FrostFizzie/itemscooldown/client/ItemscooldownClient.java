package me.FrostFizzie.itemscooldown.client;

import me.FrostFizzie.itemscooldown.Itemscooldown;
import me.FrostFizzie.itemscooldown.client.Config.Config;
import net.minecraft.client.Minecraft;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.network.chat.Component;

import java.awt.*;

public class ItemscooldownClient implements ClientModInitializer {

    Minecraft client =  Minecraft.getInstance();
    @Override
    public void onInitializeClient() {
        Config.init();
        ItemTooltipCallback.EVENT.register((stack, tooltipContext, tooltipFlag, components) -> {
            assert client.player != null;
            float cooldown = Itemscooldown.getCooldownTime(stack, 0);
            if (cooldown > 0 && client.hasControlDown() && Config.displayTooltip) {
                components.add(Component.literal("Cooldown: " + (int) cooldown).withColor(Color.decode(Config.displayColor).getRGB()));
            }
        });
    }
}

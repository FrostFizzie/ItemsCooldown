package me.FrostFizzie.itemscooldown;

import me.FrostFizzie.itemscooldown.client.mixin.Accessors.CooldownInstanceAccessor;
import me.FrostFizzie.itemscooldown.client.mixin.Accessors.ItemCooldownsAccessor;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.item.ItemStack;
import org.apache.logging.log4j.LogManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Itemscooldown implements ModInitializer {
    public static final String MOD_ID = "itemscooldown";
    public static Minecraft client = Minecraft.getInstance();
    public static Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    @Override
    public void onInitialize() {
    }
    public static float getCooldownTime(ItemStack itemStack, float f) {
        assert client.player != null;
        ItemCooldowns itemCooldowns = client.player.getCooldowns();
        Identifier identifier = itemCooldowns.getCooldownGroup(itemStack);
        ItemCooldownsAccessor itemCooldownsAccessor = (ItemCooldownsAccessor) itemCooldowns;
        CooldownInstanceAccessor cooldownInstanceAccessor = (CooldownInstanceAccessor) itemCooldownsAccessor.getCooldowns().get(identifier);
        if (cooldownInstanceAccessor == null) return 0.0f;
        return cooldownInstanceAccessor.getEndTime() - (itemCooldownsAccessor.getTickCount() + f);
    }
}

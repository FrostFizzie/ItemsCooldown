package me.FrostFizzie.itemscooldown.Config;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.dvs.versioning.BasicVersioning;
import dev.dejvokep.boostedyaml.settings.dumper.DumperSettings;
import dev.dejvokep.boostedyaml.settings.general.GeneralSettings;
import dev.dejvokep.boostedyaml.settings.loader.LoaderSettings;
import dev.dejvokep.boostedyaml.settings.updater.UpdaterSettings;
import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.ColorControllerBuilder;
import dev.isxander.yacl3.api.controller.EnumControllerBuilder;
import me.FrostFizzie.itemscooldown.Itemscooldown;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

import static me.FrostFizzie.itemscooldown.Itemscooldown.LOGGER;
import static me.FrostFizzie.itemscooldown.Itemscooldown.MOD_ID;

public class Config {
    public static YamlDocument instance;
    public static Color displayColor;
    public static TooltipDisplayOptions displayTooltip;
    public static void init() {
        try {
            instance = YamlDocument.create(new File(FabricLoader.getInstance().getConfigDir().resolve("itemscooldown").toFile(), "config.yml"),
                    Objects.requireNonNull(Itemscooldown.class.getResourceAsStream("/assets/itemscooldown/config.yml")),
                    GeneralSettings.DEFAULT,
                    LoaderSettings.builder().setAutoUpdate(true).setAllowDuplicateKeys(false).setCreateFileIfAbsent(true).build(),
                    DumperSettings.DEFAULT,
                    UpdaterSettings.builder().setVersioning(new BasicVersioning("config-version")).setOptionSorting(UpdaterSettings.OptionSorting.SORT_BY_DEFAULTS)
                            .build());
            instance.save();
            updateValues();
        } catch (IOException ignored) {
        }
    }
    public static Screen createConfigMenu(Screen parent) {
        return YetAnotherConfigLib.createBuilder()
                .title(Component.translatable(MOD_ID + ".screen.config.title"))
                .category(ConfigCategory.createBuilder()
                        .name(Component.translatable(MOD_ID + ".screen.config.main.title"))
                        .option(Option.<TooltipDisplayOptions>createBuilder()
                                .name(Component.translatable(MOD_ID + ".screen.config.main.tooltip"))
                                .binding(TooltipDisplayOptions.CONTROL, () -> displayTooltip, (newVal) -> displayTooltip = newVal)
                                .controller(opt -> EnumControllerBuilder.create(opt)
                                        .enumClass(TooltipDisplayOptions.class))
                                .description((val) -> OptionDescription.of(Component.translatable(MOD_ID + ".screen.config.main.tooltip." + val.name().toLowerCase())))
                                .build()
                        )
                        .option(Option.<Color>createBuilder()
                                .name(Component.translatable(MOD_ID + ".screen.config.main.color"))
                                .binding(Color.decode("#FFAA00"), () -> displayColor, (newVal) -> displayColor = newVal)
                                .controller(ColorControllerBuilder::create)
                                .build())
                        .build())
                .save(() -> {
                    System.out.println(displayColor);
                    instance.set("display-color", "#" + Integer.toHexString(displayColor.getRGB() & 0x00ffffff).toUpperCase());
                    instance.set("display-tooltip-mode", displayTooltip.name());
                    try {
                        instance.save();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                })
                .build()
                .generateScreen(parent);
    }
    public static void updateValues() throws IOException {
        instance.update();
        try {
            displayColor = Color.decode(instance.getAsOptional("display-color", String.class).orElse("#FFAA00"));
        } catch (NumberFormatException _) {
            LOGGER.warn("Invalid display-color setting! Should be hexidecimal color!");
            displayColor = Color.decode("#FFAA00");
        }
        try {
            displayTooltip = TooltipDisplayOptions.valueOf(instance.getAsOptional("display-tooltip-mode", String.class).orElse("CONTROL"));
        } catch (IllegalArgumentException _) {
            LOGGER.warn("Invalid display-tooltip type! " + Arrays.toString(TooltipDisplayOptions.values()) + " are the only options!");
            displayTooltip = TooltipDisplayOptions.CONTROL;
        }

    }
    public enum TooltipDisplayOptions implements NameableEnum {
        NEVER,
        CONTROL,
        ALWAYS;

        @Override
        public Component getDisplayName() {
            return Component.translatable(MOD_ID + ".tooltipoptions." + name().toLowerCase());
        }
    }
}

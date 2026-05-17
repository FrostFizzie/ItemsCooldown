package me.FrostFizzie.itemscooldown.client.Config;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.dvs.versioning.BasicVersioning;
import dev.dejvokep.boostedyaml.settings.dumper.DumperSettings;
import dev.dejvokep.boostedyaml.settings.general.GeneralSettings;
import dev.dejvokep.boostedyaml.settings.loader.LoaderSettings;
import dev.dejvokep.boostedyaml.settings.updater.UpdaterSettings;
import me.FrostFizzie.itemscooldown.Itemscooldown;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class Config {
    public static YamlDocument instance;
    public static String displayColor;
    public static Boolean displayTooltip;
    public static void init() {
        try {
            instance = YamlDocument.create(new File(FabricLoader.getInstance().getConfigDir().resolve("ItemsCooldown").toFile(), "config.yml"),
                    Objects.requireNonNull(Itemscooldown.class.getResourceAsStream("/ItemsCooldown/config.yml")),
                    GeneralSettings.DEFAULT,
                    LoaderSettings.builder().setAutoUpdate(true).setAllowDuplicateKeys(false).setCreateFileIfAbsent(true).build(),
                    DumperSettings.DEFAULT,
                    UpdaterSettings.builder().setVersioning(new BasicVersioning("config-version")).setOptionSorting(UpdaterSettings.OptionSorting.SORT_BY_DEFAULTS)
                            .build());
            instance.update();
            instance.save();
            updateValues();
        } catch (IOException ignored) {
        }
    }
    public static void updateValues() {
        String color = instance.getString("display-color");
        if (!(color != null && color.matches("^#([A-Fa-f0-9]{3}|[A-Fa-f0-9]{6}|[A-Fa-f0-9]{8})$"))) Itemscooldown.LOGGER.warn("Invalid cooldown display color. Using default color.");
        displayColor = color != null &&
                color.matches("^#([A-Fa-f0-9]{3}|[A-Fa-f0-9]{6}|[A-Fa-f0-9]{8})$") ? color : "#FFAA00";
        displayTooltip = !Objects.equals(instance.getBoolean("display-tooltip"), null) ? instance.getBoolean("display-tooltip") : true;
    }
}

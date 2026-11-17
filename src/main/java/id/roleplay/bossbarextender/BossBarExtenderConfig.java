package id.roleplay.bossbarextender;

import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public final class BossBarExtenderConfig {
    private static final Path FILE = FabricLoader.getInstance()
            .getConfigDir().resolve("bossbar-extender.properties");

    private static volatile Settings settings = new Settings(32, 12, 19, 85);

    private BossBarExtenderConfig() {
    }

    public static void load() {
        Properties properties = new Properties();
        try {
            Files.createDirectories(FILE.getParent());
            if (Files.notExists(FILE)) {
                try (Writer writer = Files.newBufferedWriter(FILE)) {
                    properties.setProperty("max-boss-bars", "32");
                    properties.setProperty("top-offset", "12");
                    properties.setProperty("bar-spacing", "19");
                    properties.setProperty("max-screen-usage-percent", "85");
                    properties.store(writer, "BossBar Extender settings. Restart Minecraft after changing these values.");
                }
            }
            try (Reader reader = Files.newBufferedReader(FILE)) {
                properties.load(reader);
            }

            int maxBars = clamp(parse(properties, "max-boss-bars", 32), 1, 100);
            int topOffset = clamp(parse(properties, "top-offset", 12), 0, 200);
            int spacing = clamp(parse(properties, "bar-spacing", 19), 10, 40);
            int screenPercent = clamp(parse(properties, "max-screen-usage-percent", 85), 35, 100);
            settings = new Settings(maxBars, topOffset, spacing, screenPercent);

            System.out.println("[BossBar Extender] Loaded: max=" + maxBars
                    + ", spacing=" + spacing + ", screen usage=" + screenPercent + "%");
        } catch (IOException exception) {
            System.err.println("[BossBar Extender] Could not read config; using defaults: " + exception.getMessage());
            settings = new Settings(32, 12, 19, 85);
        }
    }

    public static Settings get() {
        return settings;
    }

    private static int parse(Properties properties, String key, int fallback) {
        try {
            return Integer.parseInt(properties.getProperty(key, Integer.toString(fallback)).trim());
        } catch (NumberFormatException ignored) {
            return fallback;
        }
    }

    private static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }

    public record Settings(int maxBossBars, int topOffset, int barSpacing, int maxScreenUsagePercent) {
    }
}

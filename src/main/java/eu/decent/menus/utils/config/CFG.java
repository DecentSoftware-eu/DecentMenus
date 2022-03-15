package eu.decent.menus.utils.config;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public final class CFG {

    @Nullable
    public static YamlConfiguration load(@NotNull Object object, @NotNull File file) {
        try {
            file.getParentFile().mkdirs();
            YamlConfiguration config = saveIntoConfigurationFromObject(object);
            if (!file.exists()) {
                config.save(file);
            }
            config.load(file);
            loadFromConfigurationToObject(object, config);
            write(object, file);
            return config;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void write(@NotNull Object object, @NotNull File file) throws Exception {
        saveIntoConfigurationFromObject(object).save(file);
    }

    public static void read(@NotNull Object object, @NotNull File file) throws IOException, InvalidConfigurationException {
        file.getParentFile().mkdirs();
        YamlConfiguration config = new YamlConfiguration();
        config.load(file);
        loadFromConfigurationToObject(object, config);
    }

    @NotNull
    public static YamlConfiguration saveIntoConfigurationFromObject(@NotNull Object object) throws Exception {
        YamlConfiguration config = new YamlConfiguration();
        boolean stat = object instanceof Class<?>;
        Class<?> clazz = stat ? (Class<?>) object : object.getClass();
        for (Field f : clazz.getDeclaredFields()) {
            f.setAccessible(true);
            if (f.isAnnotationPresent(ConfigValue.class)) {
                String key = f.getAnnotation(ConfigValue.class).value();
                if (Modifier.isStatic(f.getModifiers())) {
                    config.set(key, f.get(null));
                } else if (!stat) {
                    config.set(key, f.get(object));
                }
            }
        }
        return config;
    }

    public static void loadFromConfigurationToObject(@NotNull Object object, @NotNull YamlConfiguration config) {
        boolean stat = object instanceof Class<?>;
        Class<?> clazz = stat ? (Class<?>) object : object.getClass();
        for (Field f : clazz.getDeclaredFields()) {
            if (!f.isAnnotationPresent(ConfigValue.class)) {
                continue;
            }
            f.setAccessible(true);

            // -- Get the annotation
            ConfigValue configValue = f.getAnnotation(ConfigValue.class);
            String key = configValue.value();
            double min = configValue.min();
            double max = configValue.max();

            try {
                Object o = config.get(key);
                if (o != null) {
                    // -- If number, check min & max
                    if (o instanceof Number) {
                        Number number = (Number) o;
                        double numberValue = number.doubleValue();
                        if (numberValue < min || numberValue > max) {
                            continue;
                        }
                    }

                    // -- Set the field value
                    if (Modifier.isStatic(f.getModifiers())) {
                        f.set(null, o);
                    } else if (!stat) {
                        f.set(object, o);
                    }
                }
            } catch (Throwable ignored) {}
        }
    }

}

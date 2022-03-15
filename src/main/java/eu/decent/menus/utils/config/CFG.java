package eu.decent.menus.utils.config;

import lombok.experimental.UtilityClass;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * This utility class has some methods for loading/saving values between {@link YamlConfiguration} and Objects.
 */
@UtilityClass
public final class CFG {

    /**
     * Load field values of the given Object from the given {@link File}. Or, if the file
     * doesn't exist or is missing some values, save defaults from the object.
     *
     * @param object The object.
     * @param file The file.
     * @return The {@link YamlConfiguration} created from the file.
     */
    @Nullable
    public static YamlConfiguration load(@NotNull Object object, @NotNull File file) {
        try {
            // -- Prepare the dirs
            if (!file.getParentFile().mkdirs()) {
                return null;
            }
            // -- Get the YamlConfiguration
            YamlConfiguration config = CFG.saveIntoConfigurationFromObject(object);
            if (!file.exists()) {
                config.save(file);
            } else {
                config.load(file);
            }
            // -- Load & Save
            CFG.loadFromConfigurationToObject(object, config);
            CFG.saveIntoConfigurationFromObject(object).save(file);
            return config;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Save the given Object into a new {@link YamlConfiguration}. Values saved into the configuration are
     * taken from the fields annotated with {@link ConfigValue} which defines the path in the configuration.
     * <br>
     * If the object is a {@link Class}, static fields are used.
     *
     * @param object The object.
     * @return The configuration.
     */
    @NotNull
    public static YamlConfiguration saveIntoConfigurationFromObject(@NotNull Object object) {
        YamlConfiguration config = new YamlConfiguration();
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

            // -- Save the value
            try {
                if (Modifier.isStatic(f.getModifiers())) {
                    config.set(key, f.get(null));
                } else if (!stat) {
                    config.set(key, f.get(object));
                }
            } catch (Throwable ignored) {}
        }
        return config;
    }

    /**
     * Load values from the given {@link YamlConfiguration} into the given Objects fields. Value for a field
     * is only loaded if the field is annotated with {@link ConfigValue} which defines the path to use
     * to locate the value in the configuration.
     * <br>
     * If the object is a {@link Class}, static fields are used.
     *
     * @param object The object.
     * @param config The configuration.
     */
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

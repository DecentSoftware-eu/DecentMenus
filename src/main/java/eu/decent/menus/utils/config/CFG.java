package eu.decent.menus.utils.config;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public final class CFG {

    public static YamlConfiguration load(Object object, File file) {
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

    public static void write(Object object, File file) throws Exception {
        saveIntoConfigurationFromObject(object).save(file);
    }

    public static void read(Object object, File file) throws IOException, InvalidConfigurationException {
        file.getParentFile().mkdirs();
        YamlConfiguration config = new YamlConfiguration();
        config.load(file);
        loadFromConfigurationToObject(object, config);
    }

    public static YamlConfiguration saveIntoConfigurationFromObject(Object object) throws Exception {
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

    public static void loadFromConfigurationToObject(Object object, YamlConfiguration config) {
        boolean stat = object instanceof Class<?>;
        Class<?> clazz = stat ? (Class<?>) object : object.getClass();
        for (Field f : clazz.getDeclaredFields()) {
            f.setAccessible(true);
            if (f.isAnnotationPresent(ConfigValue.class)) {
                String key = f.getAnnotation(ConfigValue.class).value();
                if (Modifier.isStatic(f.getModifiers())) {
                    try {
                        Object o = config.get(key);
                        if (o != null) {
                            f.set(null, o);
                        }
                    } catch (Throwable ignored) {}
                } else if (!stat) {
                    try {
                        Object o = config.get(key);
                        if (o != null) {
                            f.set(object, o);
                        }
                    } catch (Throwable ignored) {}
                }
            }
        }
    }

}

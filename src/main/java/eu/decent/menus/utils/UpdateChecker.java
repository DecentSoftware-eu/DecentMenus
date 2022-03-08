package eu.decent.menus.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.logging.Level;

public class UpdateChecker {

    private final String url;

    public UpdateChecker(int resourceId) {
        this.url = "https://api.spigotmc.org/legacy/update.php?resource=" + resourceId;
    }

    public void check(final Consumer<String> consumer) {
        S.async(() -> {
            try (InputStream inputStream = new URL(url).openStream();
                 Scanner scanner = new Scanner(inputStream)) {
                if (scanner.hasNext()) {
                    consumer.accept(scanner.next());
                }
            } catch (IOException exception) {
                Common.log(Level.WARNING, "Cannot look for updates: " + exception.getMessage());
            }
        });
    }
}
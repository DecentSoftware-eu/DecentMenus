package eu.decent.menus.hooks;

import me.arcaniax.hdb.api.HeadDatabaseAPI;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

/**
 * HeadDatabase.
 */
public final class HDB {

    public static final HeadDatabaseAPI API;

    static {
        if (Bukkit.getPluginManager().isPluginEnabled("HeadDatabase")) {
            API = new HeadDatabaseAPI();
        } else {
            API = null;
        }
    }

    @Nullable
    public static ItemStack getHeadItemStackById(String id) {
        if (API == null) {
            return null;
        }
        return API.getItemHead(id);
    }

}

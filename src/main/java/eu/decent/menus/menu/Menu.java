package eu.decent.menus.menu;

import eu.decent.library.utils.Common;
import eu.decent.menus.menu.enums.EnumSlotType;
import eu.decent.menus.menu.item.MenuItem;
import eu.decent.menus.player.PlayerProfile;
import eu.decent.menus.utils.ticker.DecentMenusTicked;
import eu.decent.menus.utils.MenuUtils;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

/**
 * This class represents a menu.
 */
@Getter
public class Menu extends DecentMenusTicked implements InventoryHolder {

    private final MenuModel menuModel;
    private final PlayerProfile owner;
    private final Menu previousMenu;
    private Inventory inventory;
    @Getter(AccessLevel.NONE)
    private MenuItem[] items;

    /**
     * Create a new instance of {@link Menu}.
     *
     * @param menuModel The {@link MenuModel} for this menu.
     * @param owner The owning players profile.
     */
    public Menu(@NotNull MenuModel menuModel, @NotNull PlayerProfile owner) {
        this(menuModel, owner, null);
    }

    /**
     * Create a new instance of {@link Menu}.
     *
     * @param menuModel The {@link MenuModel} for this menu.
     * @param owner The owning players profile.
     * @param previousMenu The previous menu.
     */
    public Menu(@NotNull MenuModel menuModel, @NotNull PlayerProfile owner, Menu previousMenu) {
        super(menuModel.getUpdateInterval());
        this.menuModel = menuModel;
        this.owner = owner;
        this.previousMenu = previousMenu;
    }

    /*
     *  General Methods
     */

    /**
     * Open this menu for the owning player.
     */
    public void open() {
        Player player = owner.getPlayer();
        // If the player is offline or not allowed to open the menu, return.
        if (player == null || !menuModel.hasPermission(player)) {
            return;
        }

        Map<Character, MenuItem> itemMap = menuModel.getItems();
        List<String> slots = menuModel.getSlots();
        String title = Common.colorize(menuModel.getTitle());
        int size = slots.size() * 9;

        items = new MenuItem[size];

        // Create & open the inventory
        if (inventory == null || !equals(player.getOpenInventory().getTopInventory().getHolder()) || inventory.getSize() != size) {
            inventory = Bukkit.createInventory(this, size, title);
            player.openInventory(inventory);
            owner.setOpenMenu(this);
            startTicking();
        }

        // Update the inventory contents
        inventory.clear();
        for (int i = 0; i < slots.size(); i++) {
            String line = slots.get(i);
            char[] chars = line.toCharArray();
            for (int j = 0; j < chars.length; j++) {
                char ch = chars[j];
                if (ch == ' ' || !itemMap.containsKey(ch)) {
                    continue;
                }

                MenuItem menuItem = itemMap.get(ch);
                if (menuItem != null && menuItem.canDisplayTo(player) && menuItem.canDisplay()) {
                    // Prepare the slot
                    int slot = menuItem.getSlotType().equals(EnumSlotType.FILL)
                            ? MenuUtils.getFirstFreeSlot(inventory)
                            : i * 9 + j;
                    // Add the item if possible
                    if (slot >= 0) {
                        items[slot] = menuItem;
                        inventory.setItem(slot, menuItem.construct(this));
                    }
                }
            }
        }
        player.updateInventory();
    }

    /**
     * Close this menu for the owning player.
     */
    public void close() {
        Player player = owner.getPlayer();
        if (player == null) {
            return;
        }
        player.closeInventory();
    }

    @Override
    public void tick() {
        open();
    }

    /*
     *  Event Methods
     */

    /**
     * Handle {@link InventoryClickEvent}.
     *
     * @param event The event.
     */
    public void onClick(@NotNull InventoryClickEvent event) {
        event.setCancelled(true);
        int slot = event.getSlot();
        MenuItem menuItem = items[slot];
        if (menuItem != null && menuItem.canClick(owner.getPlayer())) {
            menuItem.onClick(this, event);
        }
    }

    /**
     * Handle {@link InventoryDragEvent}.
     *
     * @param event The event.
     */
    public void onDrag(@NotNull InventoryDragEvent event) {
        event.setCancelled(true);
    }

    /**
     * Handle {@link InventoryCloseEvent}.
     *
     * @param event The event.
     */
    public void onClose(@NotNull InventoryCloseEvent event) {
        getOwner().setOpenMenu(null);
        stopTicking();
    }

}

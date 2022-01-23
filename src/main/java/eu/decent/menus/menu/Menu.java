package eu.decent.menus.menu;

import eu.decent.library.utils.Common;
import eu.decent.menus.menu.enums.EnumSlotType;
import eu.decent.menus.menu.item.MenuItem;
import eu.decent.menus.player.PlayerProfile;
import eu.decent.menus.utils.DecentMenusTicked;
import eu.decent.menus.utils.MenuUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

/**
 * This class represents a menu.
 */
public class Menu extends DecentMenusTicked implements InventoryHolder {

    private final MenuModel menuModel;
    private final PlayerProfile owner;
    private final Menu previousMenu;
    private Inventory inventory;
    private MenuItem[] items;

    /**
     * Create a new instance of {@link Menu}.
     *
     * @param menuModel The {@link MenuModel} for this menu.
     * @param owner The owning players profile.
     */
    public Menu(MenuModel menuModel, PlayerProfile owner) {
        this(menuModel, owner, null);
    }

    /**
     * Create a new instance of {@link Menu}.
     *
     * @param menuModel The {@link MenuModel} for this menu.
     * @param owner The owning players profile.
     * @param previousMenu The previous menu.
     */
    public Menu(MenuModel menuModel, PlayerProfile owner, Menu previousMenu) {
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
        if (inventory == null || !player.getOpenInventory().getTopInventory().getHolder().equals(this) || inventory.getSize() != size) {
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
    public void onClick(InventoryClickEvent event) {
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
    public void onDrag(InventoryDragEvent event) {
        event.setCancelled(true);
    }

    /**
     * Handle {@link InventoryCloseEvent}.
     *
     * @param event The event.
     */
    public void onClose(InventoryCloseEvent event) {
        getOwner().setOpenMenu(null);
        stopTicking();
    }

    /*
     *  Getters & Setters
     */

    /**
     * Get the bukkit inventory of this menu.
     *
     * @return The inventory.
     */
    @Override
    public Inventory getInventory() {
        return inventory;
    }

    /**
     * Get the {@link MenuModel} of this menu.
     *
     * @return The menu model.
     */
    public MenuModel getMenuModel() {
        return menuModel;
    }

    /**
     * Get the owning player of this menu.
     *
     * @return The players profile.
     */
    public PlayerProfile getOwner() {
        return owner;
    }

    /**
     * Get the previous menu of this menu.
     *
     * @return The previous menu.
     */
    @Nullable
    public Menu getPreviousMenu() {
        return previousMenu;
    }

}

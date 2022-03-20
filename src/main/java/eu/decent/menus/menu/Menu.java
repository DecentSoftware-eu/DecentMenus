package eu.decent.menus.menu;

import eu.decent.menus.api.events.MenuClickEvent;
import eu.decent.menus.api.events.MenuCloseEvent;
import eu.decent.menus.api.events.MenuOpenEvent;
import eu.decent.menus.menu.item.MenuItem;
import eu.decent.menus.menu.item.MenuItemIntent;
import eu.decent.menus.menu.item.MenuSlotType;
import eu.decent.menus.player.PlayerProfile;
import eu.decent.menus.utils.Common;
import eu.decent.menus.utils.MenuUtils;
import eu.decent.menus.utils.S;
import eu.decent.menus.utils.item.ItemWrapper;
import eu.decent.menus.utils.ticker.Ticked;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
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
public class Menu extends Ticked implements InventoryHolder {

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
        // -- If the player is offline or not allowed to open the menu, return.
        if (player == null || !menuModel.isAllowed(owner, MenuIntent.OPEN)) {
            return;
        }
        String title = Common.colorize(menuModel.getTitle());
        int size = menuModel.getInventorySize();

        // -- Create & open the inventory if not open
        if (isClosed()) {
            // Run sync
            if (!Bukkit.isPrimaryThread()) {
                S.run(this::open);
                return;
            }
            // Open the inventory
            inventory = Bukkit.createInventory(this, size, title);
            player.openInventory(inventory);
            owner.setOpenMenu(this);
            // Perform open actions
            menuModel.performActions(owner, MenuIntent.OPEN);
            // Start ticking
            startTicking();
            // Call open event
            Bukkit.getPluginManager().callEvent(new MenuOpenEvent(this));
        }
        // -- Update contents
        this.update();
    }

    /**
     * Update the contents of this menu.
     */
    public void update() {
        // -- Don't update if not open
        if (isClosed()) {
            return;
        }

        Player player = owner.getPlayer();
        Map<String, MenuItem> itemMap = menuModel.getMenuItemMap();
        int size = menuModel.getInventorySize();
        this.items = new MenuItem[size];

        // -- Clear the inventory
        inventory.clear();

        if (!itemMap.isEmpty()) {
            // -- Add all items from layout
            List<String> slots = menuModel.getSlots();
            if (slots != null && !slots.isEmpty()) {
                for (int i = 0; i < slots.size(); i++) {
                    String line = slots.get(i);
                    char[] chars = line.toCharArray();
                    for (int j = 0; j < chars.length; j++) {
                        String ch = String.valueOf(chars[j]);
                        if (!itemMap.containsKey(ch)) {
                            continue;
                        }
                        MenuItem menuItem = itemMap.get(ch);
                        if (menuItem != null) {
                            int slot = prepareSlot(menuItem, i * 9 + j);
                            addMenuItemToInventoryIfPossible(player, menuItem, slot);
                        }
                    }
                }
            }

            // -- Add all the other items
            for (MenuItem menuItem : itemMap.values()) {
                int slot = prepareSlot(menuItem, menuItem.getSlot());
                addMenuItemToInventoryIfPossible(player, menuItem, slot);
            }
        }

        // -- Update inventory contents
        player.updateInventory();
    }

    /**
     * Close this menu for the owning player if allowed.
     */
    public void close() {
        owner.getPlayer().closeInventory();
    }

    /**
     * Check whether this menu is currently open.
     *
     * @return The requested boolean.
     */
    public boolean isClosed() {
        Player player = owner.getPlayer();
        InventoryHolder holder = player.getOpenInventory().getTopInventory().getHolder();
        return inventory == null || !equals(holder) || inventory.getSize() != menuModel.getInventorySize();
    }

    @Override
    public void tick() {
        this.update();
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

        ClickType clickType = event.getClick();
        int slot = event.getRawSlot();

        // -- Call the MenuClickEvent
        MenuClickEvent menuClickEvent = new MenuClickEvent(this, clickType, slot);
        Bukkit.getPluginManager().callEvent(menuClickEvent);
        if (menuClickEvent.isCancelled()) {
            return;
        }

        // -- Window border clicks
        if (clickType == ClickType.WINDOW_BORDER_RIGHT) {
            menuModel.performActions(owner, MenuIntent.WINDOW_BORDER_RIGHT_CLICK);
            return;
        } else if (clickType == ClickType.WINDOW_BORDER_LEFT) {
            menuModel.performActions(owner, MenuIntent.WINDOW_BORDER_LEFT_CLICK);
            return;
        }

        // -- Execute menu item click
        if (slot >= 0 && slot < items.length) {
            MenuItem menuItem = items[slot];
            if (menuItem != null) {
                menuItem.onClick(owner, event);
            }
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
    @SuppressWarnings("unused")
    public void onClose(@NotNull InventoryCloseEvent event) {
        stopTicking();
        getOwner().setOpenMenu(null);

        // -- Call the MenuCloseEvent
        MenuCloseEvent menuCloseEvent = new MenuCloseEvent(this);
        Bukkit.getPluginManager().callEvent(menuCloseEvent);

        if (menuModel.isAllowed(owner, MenuIntent.CLOSE) && !menuCloseEvent.isCancelled()) {
            menuModel.performActions(owner, MenuIntent.CLOSE);
        } else {
            // If the player is not allowed to close this menu, open it back.
            open();
        }
    }

    /*
     *  Utility Methods
     */

    private int prepareSlot(@NotNull MenuItem menuItem, int defaultValue) {
        return menuItem.getSlotType().equals(MenuSlotType.FILL)
                ? MenuUtils.getFirstFreeSlot(inventory)
                : defaultValue;
    }

    private void addMenuItemToInventoryIfPossible(@NotNull Player player, @NotNull MenuItem menuItem, int slot) {
        if (slot >= 0 && items[slot] == null) {
            ItemWrapper itemWrapper = menuItem.isAllowed(owner, MenuItemIntent.DISPLAY)
                    ? menuItem.getItemWrapper()
                    : menuItem.getErrorItemWrapper();
            if (itemWrapper != null) {
                inventory.setItem(slot, itemWrapper.toItemStack(player, (s) -> menuItem.processString(player, s)));
                items[slot] = menuItem;
            }
        }
    }

}

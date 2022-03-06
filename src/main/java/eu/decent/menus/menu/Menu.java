package eu.decent.menus.menu;

import eu.decent.library.utils.Common;
import eu.decent.menus.menu.item.MenuItem;
import eu.decent.menus.menu.item.MenuItemIntent;
import eu.decent.menus.menu.item.MenuSlotType;
import eu.decent.menus.player.PlayerProfile;
import eu.decent.menus.utils.MenuUtils;
import eu.decent.menus.utils.item.ItemWrapper;
import eu.decent.menus.utils.ticker.DecentMenusTicked;
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
import java.util.stream.Collectors;

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
        // -- If the player is offline or not allowed to open the menu, return.
        if (player == null || !menuModel.isAllowed(owner, MenuIntent.OPEN)) {
            return;
        }

        Map<String, MenuItem> itemMap = menuModel.getMenuItemMap();
        String title = Common.colorize(menuModel.getTitle());
        int size = menuModel.getInventorySize();

        items = new MenuItem[size];

        // -- Create & open the inventory if not open yet
        InventoryHolder playerTopInventoryHolder = player.getOpenInventory().getTopInventory().getHolder();
        if (inventory == null || !equals(playerTopInventoryHolder) || inventory.getSize() != size) {
            inventory = Bukkit.createInventory(this, size, title);
            player.openInventory(inventory);
            owner.setOpenMenu(this);
            menuModel.performActions(owner, MenuIntent.OPEN);
            startTicking();
        }

        // -- Clear the inventory
        inventory.clear();

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
        List<MenuItem> otherItems = itemMap.values().stream()
                .filter(menuItem -> menuItem.getSlot() != -1)
                .collect(Collectors.toList());
        for (MenuItem menuItem : otherItems) {
            int slot = prepareSlot(menuItem, menuItem.getSlot());
            addMenuItemToInventoryIfPossible(player, menuItem, slot);
        }

        // -- Update inventory contents
        player.updateInventory();
    }

    /**
     * Close this menu for the owning player if allowed.
     */
    public void close() {
        // If the player is not allowed to close this menu, don't close.
        if (menuModel.isAllowed(owner, MenuIntent.CLOSE)) {
            owner.getPlayer().closeInventory();
        }
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

        MenuItem menuItem = items[event.getSlot()];
        if (menuItem != null) {
            menuItem.onClick(owner, event);
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
        if (menuModel.isAllowed(owner, MenuIntent.CLOSE)) {
            getOwner().setOpenMenu(null);
            stopTicking();
            menuModel.performActions(owner, MenuIntent.CLOSE);
        } else {
            // If the player is not allowed to close this menu, open in back.
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
                inventory.setItem(slot, itemWrapper.toItemStack(player));
                items[slot] = menuItem;
            }
        }
    }

}

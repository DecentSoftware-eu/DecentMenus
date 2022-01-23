package eu.decent.menus.menu.item;

import eu.decent.menus.menu.Menu;
import eu.decent.menus.menu.enums.EnumMenuItemType;
import eu.decent.menus.utils.config.Configuration;
import eu.decent.menus.utils.item.ItemWrapper;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class NormalMenuItem extends MenuItem {

    private ItemWrapper itemWrapper;

    public NormalMenuItem(Configuration config, char identifier) {
        super(config, identifier, EnumMenuItemType.NORMAL);
    }

    @Override
    public ItemStack construct(Menu menu) {
        Player player = menu.getOwner().getPlayer();
        return itemWrapper.parse(this, player);
    }

    @Override
    public void load() {
        super.load();
        this.permission = config.getString("permission", null);
        this.itemWrapper = config.getItemWrapper("item");
    }

}

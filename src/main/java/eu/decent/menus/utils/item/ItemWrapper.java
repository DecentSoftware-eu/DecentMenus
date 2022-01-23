package eu.decent.menus.utils.item;

import eu.decent.library.utils.Common;
import eu.decent.menus.menu.item.MenuItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

@Getter
@AllArgsConstructor
public class ItemWrapper {

	protected Material material;
	protected String name;
	protected String skullOwner;
	protected String skullTexture;
	protected int amount;
	protected short durability;
	protected List<String> lore;
	protected Map<Enchantment, Integer> enchantments;
	protected ItemFlag[] flags;
	protected boolean unbreakable;

	public boolean canParse() {
		return material != null;
	}

	public ItemStack parse() {
		ItemBuilder itemBuilder = new ItemBuilder(material == null ? Material.STONE : material);
		if (name != null) {
			itemBuilder.withName(Common.colorize(name));
		}
		itemBuilder.withAmount(amount);
		itemBuilder.withDurability(durability);
		if (lore != null && !lore.isEmpty()) {
			itemBuilder.withLore(Common.colorize(lore));
		}
		if (enchantments != null && !enchantments.isEmpty()) {
			enchantments.forEach(itemBuilder::withUnsafeEnchantment);
		}
		if (skullOwner != null) {
			itemBuilder.withSkullOwner(skullOwner);
		} else if (skullTexture != null) {
			itemBuilder.withSkullTexture(skullTexture);
		}
		itemBuilder.withItemFlags(flags);
		itemBuilder.withUnbreakable(unbreakable);
		return itemBuilder.build();
	}

	public ItemStack parse(Player player) {
		return this.parseBuilder(player).build();
	}

	public ItemBuilder parseBuilder(Player player) {
		ItemBuilder itemBuilder = new ItemBuilder(material == null ? Material.STONE : material);
		if (name != null) {
			itemBuilder.withName(Common.colorize(name));
		}
		itemBuilder.withAmount(amount);
		itemBuilder.withDurability(durability);
		if (lore != null && !lore.isEmpty()) {
			itemBuilder.withLore(Common.colorize(lore));
		}
		if (enchantments != null && !enchantments.isEmpty()) {
			enchantments.forEach(itemBuilder::withUnsafeEnchantment);
		}
		if (skullOwner != null) {
			itemBuilder.withSkullOwner(skullOwner.equals("@") ? player.getName() : skullOwner);
		} else if (skullTexture != null) {
			itemBuilder.withSkullTexture(skullTexture);
		}
		itemBuilder.withItemFlags(flags);
		itemBuilder.withUnbreakable(unbreakable);
		return itemBuilder;
	}

	public ItemStack parse(MenuItem menuItem, Player player) {
		ItemBuilder itemBuilder = new ItemBuilder(material == null ? Material.STONE : material);
		if (name != null) {
			itemBuilder.withName(Common.colorize(menuItem.setPlaceholders(player, name)));
		}
		itemBuilder.withAmount(amount);
		itemBuilder.withDurability(durability);
		if (lore != null && !lore.isEmpty()) {
			itemBuilder.withLore(Common.colorize(menuItem.setPlaceholders(player, lore)));
		}
		if (enchantments != null && !enchantments.isEmpty()) {
			enchantments.forEach(itemBuilder::withUnsafeEnchantment);
		}
		if (skullOwner != null) {
			itemBuilder.withSkullOwner(skullOwner.equals("@") ? player.getName() : skullOwner);
		} else if (skullTexture != null) {
			itemBuilder.withSkullTexture(skullTexture);
		}
		itemBuilder.withItemFlags(flags);
		itemBuilder.withUnbreakable(unbreakable);
		return itemBuilder.build();
	}

	public ItemStack parseFrom(ItemStack itemStack, MenuItem menuItem, Player player) {
		ItemBuilder itemBuilder = new ItemBuilder(itemStack);
		if (name != null) {
			itemBuilder.withName(Common.colorize(menuItem.setPlaceholders(player, name)));
		}
		if (lore != null && !lore.isEmpty()) {
			itemBuilder.withLore(Common.colorize(menuItem.setPlaceholders(player, lore)));
		}
		if (enchantments != null && !enchantments.isEmpty()) {
			enchantments.forEach(itemBuilder::withUnsafeEnchantment);
		}
		if (skullOwner != null) {
			itemBuilder.withSkullOwner(skullOwner.equals("@") ? player.getName() : skullOwner);
		} else if (skullTexture != null) {
			itemBuilder.withSkullTexture(skullTexture);
		}
		itemBuilder.withItemFlags(flags);
		itemBuilder.withUnbreakable(unbreakable);
		return itemBuilder.build();
	}

}

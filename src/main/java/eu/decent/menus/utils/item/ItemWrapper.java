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
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class ItemWrapper {

	protected Material material;
	protected String name;
	protected String skullOwner;
	protected String skullTexture;
	protected int customModelData;
	protected int amount;
	protected short durability;
	protected List<String> lore;
	protected Map<Enchantment, Integer> enchantments;
	protected ItemFlag[] flags;
	protected boolean unbreakable;

	public ItemStack parse() {
		return parse(null);
	}

	public ItemStack parse(Player player) {
		return parse(player, Common::colorize);
	}

	public ItemStack parse(Player player, MenuItem menuItem) {
		return parse(player, (s) -> Common.colorize(menuItem.setPlaceholders(player, s)));
	}

	public ItemStack parse(Player player, Function<String, String> stringProcessor) {
		ItemBuilder itemBuilder = new ItemBuilder(material == null ? Material.STONE : material);
		if (name != null) {
			if (stringProcessor == null) {
				itemBuilder.withName(name);
			} else {
				itemBuilder.withName(stringProcessor.apply(name));
			}
		}
		if (lore != null && !lore.isEmpty()) {
			if (stringProcessor == null) {
				itemBuilder.withLore(lore);
			} else {
				itemBuilder.withLore(lore.stream()
						.map(stringProcessor)
						.collect(Collectors.toList())
				);
			}
		}
		if (enchantments != null && !enchantments.isEmpty()) {
			enchantments.forEach(itemBuilder::withUnsafeEnchantment);
		}
		if (skullOwner != null) {
			if (skullOwner.equals("@") && player != null) {
				itemBuilder.withSkullOwner(player.getName());
			} else {
				itemBuilder.withSkullOwner(skullOwner);
			}
		} else if (skullTexture != null) {
			itemBuilder.withSkullTexture(skullTexture);
		}
		itemBuilder.withCustomModelData(customModelData);
		itemBuilder.withItemFlags(flags);
		itemBuilder.withUnbreakable(unbreakable);
		return itemBuilder.build();
	}

}

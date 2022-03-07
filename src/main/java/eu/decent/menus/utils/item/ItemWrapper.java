package eu.decent.menus.utils.item;

import eu.decent.library.utils.Common;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@Setter
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

	public ItemStack toItemStack() {
		return toItemStack(null);
	}

	public ItemStack toItemStack(Player player) {
		return toItemStack(player, Common::colorize);
	}

	public ItemStack toItemStack(Player player, Function<String, String> stringProcessor) {
		ItemBuilder itemBuilder = new ItemBuilder(material == null ? Material.STONE : material);
		if (name != null) {
			if (stringProcessor == null) {
				itemBuilder.withName(name);
			} else {
				itemBuilder.withName(stringProcessor.apply(name));
			}
		}
		if (amount > 1) {
			itemBuilder.withAmount(amount);
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
		if (customModelData != -1) {
			itemBuilder.withCustomModelData(customModelData);
		}
		itemBuilder.withItemFlags(flags);
		itemBuilder.withUnbreakable(unbreakable);
		return itemBuilder.build();
	}

	@NotNull
	public static ItemWrapper fromItemStack(@NotNull ItemStack itemStack) {
		ItemBuilder itemBuilder = new ItemBuilder(itemStack);
		ItemMeta meta = itemStack.getItemMeta();
		return new ItemWrapper(
				itemStack.getType(),
				itemBuilder.getName(),
				itemBuilder.getSkullOwner(),
				"",
				meta == null || !meta.hasCustomModelData() ? 0 : meta.getCustomModelData(),
				itemStack.getAmount(),
				itemStack.getDurability(),
				itemBuilder.getLore(),
				itemStack.getEnchantments(),
				meta == null ? new ItemFlag[0] : meta.getItemFlags().toArray(new ItemFlag[0]),
				meta != null && meta.isUnbreakable()
		);
	}

}

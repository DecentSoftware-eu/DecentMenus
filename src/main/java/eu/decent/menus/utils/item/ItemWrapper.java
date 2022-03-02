package eu.decent.menus.utils.item;

import eu.decent.library.utils.Common;
import eu.decent.menus.menu.item.MenuItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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

	public Map<String, Object> toMap() {
		Map<String, Object> map = new LinkedHashMap<>();
		map.put("material", getMaterial().name());
		map.put("name", getName());
		map.put("amount", getAmount());
		map.put("durability", getDurability());
		map.put("lore", getLore());
		map.put("skull.owner", getSkullOwner());
		map.put("skull.texture", getSkullTexture());
		map.put("customModelData", getCustomModelData());
		map.put("unbreakable", isUnbreakable());
		map.put("enchantments", getEnchantments().entrySet().stream()
				.map((e) -> String.format("%s:%d", e.getKey().getName().toUpperCase(), e.getValue()))
				.collect(Collectors.toList())
		);
		map.put("flags", Arrays.stream(getFlags())
				.map(Enum::name)
				.collect(Collectors.toList())
		);
		return map;
	}

	@SuppressWarnings("unchecked")
	public static ItemWrapper fromMap(Map<String, Object> map) {
		Map<Enchantment, Integer> enchantments = new HashMap<>();
		Object objectEnchantmentsList = map.get("enchantments");
		if (objectEnchantmentsList instanceof List) {
			((List<String>) objectEnchantmentsList).stream()
					.map(s -> s.split(":"))
					.forEach(spl -> {
						Enchantment enchantment = Enchantment.getByName(spl[0]);
						int level = Integer.parseInt(spl[1]);
						enchantments.put(enchantment, level);
					});
		}

		ItemFlag[] itemFlags;
		Object objectItemFlagList = map.get("flags");
		if (objectItemFlagList instanceof List) {
			itemFlags = ((List<String>) objectItemFlagList).stream().map(ItemFlag::valueOf).toArray(ItemFlag[]::new);
		} else {
			itemFlags = new ItemFlag[0];
		}

		int durability = (int) map.getOrDefault("durability", 1);
		return new ItemWrapper(
				Material.getMaterial((String) map.getOrDefault("material", "STONE")),
				(String) map.getOrDefault("name", ""),
				(String) map.get("skull.owner"),
				(String) map.get("skull.texture"),
				(int) map.getOrDefault("customModelData", 0),
				(int) map.getOrDefault("amount", 1),
				durability > Short.MAX_VALUE ? Short.MAX_VALUE : durability < Short.MIN_VALUE ? Short.MIN_VALUE : (short) durability,
				(List<String>) map.getOrDefault("lore", List.of()),
				enchantments,
				itemFlags,
				(boolean) map.getOrDefault("unbreakable", false)
		);
	}

	public static ItemWrapper fromItemStack(ItemStack itemStack) {
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

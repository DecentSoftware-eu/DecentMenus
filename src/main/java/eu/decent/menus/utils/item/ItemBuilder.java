package eu.decent.menus.utils.item;

import com.google.common.collect.Lists;
import eu.decent.library.reflect.ReflectConstructor;
import eu.decent.library.reflect.ReflectMethod;
import eu.decent.library.reflect.ReflectionUtil;
import eu.decent.library.utils.Common;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.material.MaterialData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author D0bby_
 */
@SuppressWarnings("unused")
public class ItemBuilder implements Cloneable {

	private ItemStack itemStack;

	/*
	 *	Constructors
	 */

	public ItemBuilder(Material material) {
		this(material, 1);
	}

	public ItemBuilder(ItemStack itemStack) {
		this.itemStack = itemStack;
	}

	public ItemBuilder(Material material, int amount) {
		this(new ItemStack(material, amount));
	}

	public ItemBuilder(Material material, int amount, short durability) {
		this(new ItemStack(material, amount, durability));
	}

	/*
	 *	General Methods
	 */

	@Override
	public ItemBuilder clone() {
		return new ItemBuilder(itemStack);
	}

	public ItemStack toItemStack() {
		return itemStack;
	}

	public ItemStack build() {
		return itemStack;
	}

	public ItemBuilder withItemStack(ItemStack itemStack) {
		this.itemStack = itemStack;
		return this;
	}

	/*
	 *	Item Methods
	 */

	public ItemBuilder withMaterial(Material material) {
		itemStack.setType(material);
		return this;
	}

	public Material getMaterial() {
		return itemStack.getType();
	}

	public ItemBuilder withAmount(int amount) {
		itemStack.setAmount(amount);
		return this;
	}

	public ItemBuilder withDurability(short durability) {
		itemStack.setDurability(durability);
		return this;
	}

	public ItemBuilder withInfiniteDurability() {
		itemStack.setDurability(Short.MAX_VALUE);
		return this;
	}

	public ItemBuilder withUnbreakable(boolean unbreakable) {
		ItemMeta meta = itemStack.getItemMeta();
		if (meta != null) {
			meta.setUnbreakable(unbreakable);
			itemStack.setItemMeta(meta);
		}
		return this;
	}

	public ItemBuilder withName(String displayName) {
		ItemMeta meta = itemStack.getItemMeta();
		if (meta != null) {
			meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', displayName));
			itemStack.setItemMeta(meta);
		}
		return this;
	}

	public String getName() {
		ItemMeta meta = itemStack.getItemMeta();
		if (meta != null) {
			return meta.getDisplayName();
		}
		return null;
	}

	public ItemBuilder withEmptyName() {
		ItemMeta meta = itemStack.getItemMeta();
		if (meta != null) {
			meta.setDisplayName(" ");
			itemStack.setItemMeta(meta);
		}
		return this;
	}

	public ItemBuilder withLore(List<String> lore) {
		ItemMeta meta = itemStack.getItemMeta();
		if (meta != null) {
			meta.setLore(Common.colorize(lore));
			itemStack.setItemMeta(meta);
		}
		return this;
	}

	public ItemBuilder withLore(String... lore) {
		return withLore(Arrays.asList(lore));
	}

	public ItemBuilder withLoreLine(String line) {
		List<String> lore = getLore();
		if (lore != null) {
			lore.add(line);
			withLore(lore);
		} else {
			withLore(line);
		}
		return this;
	}

	public ItemBuilder withLoreLines(String... lines) {
		return withLoreLines(Arrays.asList(lines));
	}

	public ItemBuilder withLoreLines(List<String> lines) {
		List<String> lore = getLore();
		if (lore != null) {
			lore.addAll(lines);
			withLore(lore);
		} else {
			withLore(lines);
		}
		return this;
	}

	public List<String> getLore() {
		ItemMeta meta = itemStack.getItemMeta();
		if (meta != null) {
			return meta.getLore();
		}
		return Lists.newArrayList();
	}

	public ItemBuilder removeLore() {
		ItemMeta meta = itemStack.getItemMeta();
		if (meta != null) {
			meta.setLore(null);
			itemStack.setItemMeta(meta);
		}
		return this;
	}

	public ItemBuilder withItemFlags(ItemFlag... flags) {
		ItemMeta meta = itemStack.getItemMeta();
		if (meta != null) {
			meta.addItemFlags(flags);
			itemStack.setItemMeta(meta);
		}
		return this;
	}

	public ItemBuilder removeItemFlags(ItemFlag... flags) {
		ItemMeta meta = itemStack.getItemMeta();
		if (meta != null) {
			meta.removeItemFlags(flags);
			itemStack.setItemMeta(meta);
		}
		return this;
	}

	public ItemBuilder withEnchantment(Enchantment enchantment, int level) {
		itemStack.removeEnchantment(enchantment);
		itemStack.addEnchantment(enchantment, level);
		return this;
	}

	public ItemBuilder withUnsafeEnchantment(Enchantment enchantment, int level) {
		itemStack.addUnsafeEnchantment(enchantment, level);
		return this;
	}

	public ItemBuilder removeEnchantment(Enchantment enchantment) {
		itemStack.removeEnchantment(enchantment);
		return this;
	}

	public ItemBuilder withDyeColor(DyeColor color) {
		itemStack.setDurability(color.getDyeData());
		return this;
	}

	/*
	 *	Item Meta Methods
	 */

	public ItemBuilder withLeatherArmorColor(Color color) {
		ItemMeta meta = itemStack.getItemMeta();
		if (meta instanceof LeatherArmorMeta) {
			LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) meta;
			leatherArmorMeta.setColor(color);
			itemStack.setItemMeta(leatherArmorMeta);
		}
		return this;
	}

	public ItemBuilder withPotionType(PotionEffectType type) {
		ItemMeta meta = itemStack.getItemMeta();
		if (meta instanceof PotionMeta) {
			PotionMeta potionMeta = (PotionMeta) meta;
			potionMeta.setMainEffect(type);
			itemStack.setItemMeta(meta);
		}
		return this;
	}

	public ItemBuilder withCustomPotionEffect(PotionEffect effect, boolean overwrite) {
		ItemMeta meta = itemStack.getItemMeta();
		if (meta instanceof PotionMeta) {
			PotionMeta potionMeta = (PotionMeta) meta;
			potionMeta.addCustomEffect(effect, overwrite);
			itemStack.setItemMeta(meta);
		}
		return this;
	}

	public ItemBuilder removeCustomPotionEffect(PotionEffectType type) {
		ItemMeta meta = itemStack.getItemMeta();
		if (meta instanceof PotionMeta) {
			PotionMeta potionMeta = (PotionMeta) meta;
			potionMeta.removeCustomEffect(type);
			itemStack.setItemMeta(meta);
		}
		return this;
	}

	public ItemBuilder clearCustomPotionEffects() {
		ItemMeta meta = itemStack.getItemMeta();
		if (meta instanceof PotionMeta) {
			PotionMeta potionMeta = (PotionMeta) meta;
			potionMeta.clearCustomEffects();
			itemStack.setItemMeta(meta);
		}
		return this;
	}

	public ItemBuilder withSkullOwner(String playerName) {
		ItemMeta meta = itemStack.getItemMeta();
		if (meta instanceof SkullMeta) {
			SkullMeta skullMeta = (SkullMeta) meta;
			skullMeta.setOwner(playerName);
			itemStack.setItemMeta(meta);
			withDurability((short) SkullType.PLAYER.ordinal());
		}
		return this;
	}

	public String getSkullOwner() {
		ItemMeta meta = itemStack.getItemMeta();
		if (meta instanceof SkullMeta) {
			SkullMeta skullMeta = (SkullMeta) meta;
			return skullMeta.getOwner();
		}
		return null;
	}

	private static final ReflectConstructor GAME_PROFILE_CONSTRUCTOR;
	private static final ReflectConstructor PROPERTY_CONSTRUCTOR;
	private static final ReflectMethod GAME_PROFILE_GET_PROPERTIES;

	static {
		Class<?> gameProfileClass = ReflectionUtil.getClass("com.mojang.authlib.GameProfile");
		Class<?> propertyClass = ReflectionUtil.getClass("com.mojang.authlib.properties.Property");
		GAME_PROFILE_CONSTRUCTOR = new ReflectConstructor(gameProfileClass, UUID.class, Object.class);
		GAME_PROFILE_GET_PROPERTIES = new ReflectMethod(gameProfileClass, "getProperties");
		PROPERTY_CONSTRUCTOR = new ReflectConstructor(propertyClass, String.class, String.class);
	}

	public ItemBuilder withSkullTexture(String texture) {
		ItemMeta meta = itemStack.getItemMeta();
		if (meta instanceof SkullMeta) {
			SkullMeta skullMeta = (SkullMeta) meta;
			Object gameProfile = GAME_PROFILE_CONSTRUCTOR.newInstance(UUID.randomUUID(), null);
			Map<String, ?> properties = GAME_PROFILE_GET_PROPERTIES.invoke(gameProfile);
			properties.put("textures", PROPERTY_CONSTRUCTOR.newInstance("texture", texture));
			Field profileField;
			try {
				profileField = skullMeta.getClass().getDeclaredField("profile");
				profileField.setAccessible(true);
				profileField.set(skullMeta, gameProfile);
			} catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
			itemStack.setItemMeta(skullMeta);
			withDurability((short) SkullType.PLAYER.ordinal());
		}
		return this;
	}

	public ItemBuilder withData(MaterialData data) {
		itemStack.setData(data);
		return this;
	}

	public ItemBuilder withMeta(ItemMeta meta) {
		itemStack.setItemMeta(meta);
		return this;
	}

}

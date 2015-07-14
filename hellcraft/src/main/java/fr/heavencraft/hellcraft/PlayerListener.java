package fr.heavencraft.hellcraft;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.heavencraft.heavencore.bukkit.HeavenPlugin;
import fr.heavencraft.heavencore.bukkit.listeners.AbstractListener;
import fr.heavencraft.hellcraft.worlds.WorldsManager;

public class PlayerListener extends AbstractListener<HeavenPlugin>
{
	private static final String WELCOME_MESSAGE = ChatColor.GREEN + "Bienvenue sur le serveur HellCraft !";
	private static final String ENTER_CITY_MESSAGE = "Vous apparaissez dans un monde apocalyptique ! Vous entendez des bruits ...";

	public PlayerListener(HeavenPlugin plugin)
	{
		super(plugin);
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event)
	{
		final Player player = event.getPlayer();
		plugin.sendMessage(player, WELCOME_MESSAGE);
		if (event.getPlayer().hasPlayedBefore())
		{
			handlePlayerEquipment(player);
		}
	}

	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent event)
	{
		handlePlayerEquipment(event.getPlayer());
	}

	private void handlePlayerEquipment(Player player)
	{
		final ItemStack sword, helmet, chestplate, leggings, boots;

		if (player.hasPermission(HellCraftPermissions.DIAMOND_GRADE)
				|| player.hasPermission(HellCraftPermissions.VIP_GRADE))
		{
			sword = new ItemStack(Material.DIAMOND_SWORD);
			sword.addUnsafeEnchantment(Enchantment.KNOCKBACK, 2);
			sword.addUnsafeEnchantment(Enchantment.FIRE_ASPECT, 1);
			sword.addUnsafeEnchantment(Enchantment.DAMAGE_UNDEAD, 2);

			helmet = new ItemStack(Material.DIAMOND_HELMET);
			chestplate = new ItemStack(Material.DIAMOND_CHESTPLATE);
			leggings = new ItemStack(Material.DIAMOND_LEGGINGS);
			boots = new ItemStack(Material.DIAMOND_BOOTS);
		}
		else
		{
			sword = new ItemStack(Material.IRON_SWORD);

			helmet = new ItemStack(Material.IRON_HELMET);
			chestplate = new ItemStack(Material.IRON_CHESTPLATE);
			leggings = new ItemStack(Material.LEATHER_LEGGINGS);
			boots = new ItemStack(Material.IRON_BOOTS);
		}

		final ItemStack[] items = new ItemStack[]
		{ sword, new ItemStack(Material.BOW, 1), new ItemStack(Material.BREAD, 8),
				new ItemStack(Material.TORCH, 16), new ItemStack(Material.ARROW, 32) };

		final PlayerInventory inventory = player.getInventory();
		inventory.addItem(items);
		inventory.setHelmet(helmet);
		inventory.setChestplate(chestplate);
		inventory.setLeggings(leggings);
		inventory.setBoots(boots);
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event)
	{
		final Player player = event.getPlayer();

		if (!WorldsManager.getWorldSpawn().equals(player.getWorld()))
			return;

		final double x = player.getLocation().getX();
		final double y = player.getLocation().getY();
		final double z = player.getLocation().getZ();

		if (y <= 55 //
				&& -14 <= x && x <= 17 //
				&& 64 <= z && z <= 96)
		{
			player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 200, 255));
			plugin.teleportPlayer(player, WorldsManager.getCitySpawn());
			player.setGameMode(GameMode.SURVIVAL);
			plugin.sendMessage(player, ENTER_CITY_MESSAGE);
		}
	}
}
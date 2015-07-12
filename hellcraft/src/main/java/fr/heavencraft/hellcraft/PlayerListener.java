package fr.heavencraft.hellcraft;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

import fr.heavencraft.heavencore.bukkit.HeavenPlugin;
import fr.heavencraft.heavencore.bukkit.listeners.AbstractListener;
import fr.heavencraft.hellcraft.worlds.WorldsManager;

public class PlayerListener extends AbstractListener<HeavenPlugin>
{
	private static final String WELCOME_MESSAGE = ChatColor.GREEN + "Bienvenue sur le serveur HellCraft !";
	private static final String ENTER_CITY_MESSAGE = "Vous apparaissez dans un mon apocalyptique ! Vous entendez des bruits ...";

	public PlayerListener(HeavenPlugin plugin)
	{
		super(plugin);
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event)
	{
		plugin.sendMessage(event.getPlayer(), WELCOME_MESSAGE);
	}

	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent event)
	{
		final ItemStack[] items =
		{ new ItemStack(Material.IRON_SWORD, 1), new ItemStack(Material.BOW, 1),
				new ItemStack(Material.BREAD, 32), new ItemStack(Material.TORCH, 16),
				new ItemStack(Material.ARROW, 32), new ItemStack(Material.IRON_HELMET, 1),
				new ItemStack(Material.IRON_CHESTPLATE, 1), new ItemStack(Material.LEATHER_LEGGINGS, 1),
				new ItemStack(Material.IRON_BOOTS, 1) };

		event.getPlayer().getInventory().addItem(items);
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
			plugin.teleportPlayer(player, WorldsManager.getCitySpawn());
			plugin.sendMessage(player, ENTER_CITY_MESSAGE);
		}
	}
}
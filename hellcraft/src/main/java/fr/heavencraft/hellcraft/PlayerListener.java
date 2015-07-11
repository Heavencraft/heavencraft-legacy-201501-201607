package fr.heavencraft.hellcraft;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

import fr.heavencraft.heavencore.bukkit.HeavenPlugin;
import fr.heavencraft.heavencore.bukkit.listeners.AbstractListener;

public class PlayerListener extends AbstractListener<HeavenPlugin>
{
	private static final String WELCOME_FORMAT = ChatColor.GREEN + "Bienvenue sur le serveur HellCraft !";

	public PlayerListener(HeavenPlugin plugin)
	{
		super(plugin);
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event)
	{
		plugin.sendMessage(event.getPlayer(), WELCOME_FORMAT);
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

		event.getPlayer().getPlayer().getInventory().addItem(items);
	}
}
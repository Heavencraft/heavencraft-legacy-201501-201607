package fr.heavencraft.heavencore.bukkit.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import fr.heavencraft.heavencore.bukkit.HeavenPlugin;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.utils.chat.ChatUtil;

public abstract class AbstractSignListener
{
	protected final HeavenPlugin plugin;

	private final String tag;
	private final String permission;

	public AbstractSignListener(HeavenPlugin plugin, String tag, String permission)
	{
		this.plugin = plugin;
		this.tag = "[" + tag + "]";
		this.permission = permission;

		Bukkit.getPluginManager().registerEvents(new InternalListener(), plugin);
	}

	protected abstract boolean onSignPlace(Player player, SignChangeEvent event) throws HeavenException;

	protected abstract void onSignClick(Player player, Sign sign) throws HeavenException;

	private class InternalListener implements Listener
	{
		@EventHandler(ignoreCancelled = true)
		private void onSignChange(SignChangeEvent event)
		{
			final Player player = event.getPlayer();

			if (player == null || !player.hasPermission(permission) || !event.getLine(0).equalsIgnoreCase(tag))
				return;

			try
			{
				if (onSignPlace(player, event))
				{
					event.setLine(0, ChatColor.GREEN + tag);
					ChatUtil.sendMessage(player, "Le panneau {%1$s} a été placé correctement.", tag);
					return;
				}
			}
			catch (final HeavenException ex)
			{
				ChatUtil.sendMessage(player, ex.getMessage());
			}

			event.setCancelled(true);
			event.getBlock().breakNaturally();
		}

		@EventHandler(ignoreCancelled = true)
		public void onPlayerInteract(PlayerInteractEvent event)
		{
			if (event.getAction() != Action.RIGHT_CLICK_BLOCK)
				return;

			final Block block = event.getClickedBlock();

			if (block.getType() != Material.SIGN_POST && block.getType() != Material.WALL_SIGN)
				return;

			final Sign sign = (Sign) block.getState();

			if (!sign.getLine(0).equals(ChatColor.GREEN + tag))
				return;

			final Player player = event.getPlayer();
			event.setCancelled(true);

			try
			{
				onSignClick(player, sign);
			}
			catch (final HeavenException ex)
			{
				ChatUtil.sendMessage(player, ex.getMessage());
			}
		}
	}
}
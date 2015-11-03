package fr.heavencraft.heavenguard.bukkit.listeners;

import java.util.Collection;
import java.util.Iterator;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import fr.heavencraft.heavencore.bukkit.listeners.AbstractListener;
import fr.heavencraft.heavencore.utils.chat.ChatUtil;
import fr.heavencraft.heavenguard.api.Region;
import fr.heavencraft.heavenguard.bukkit.HeavenGuard;

public class PlayerListener extends AbstractListener<HeavenGuard>
{
	private final HeavenGuard plugin;

	public PlayerListener(HeavenGuard plugin)
	{
		super(plugin);
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	private void onPlayerInteract(PlayerInteractEvent event)
	{
		if (event.getAction() != Action.RIGHT_CLICK_BLOCK)
			return;

		if (event.getMaterial() != Material.ARROW)
			return;

		displayRegionAt(event.getPlayer(), event.getClickedBlock().getLocation());
	}

	public void displayRegionAt(Player player, Location location)
	{
		final String world = location.getWorld().getName();
		final int x = location.getBlockX();
		final int y = location.getBlockY();
		final int z = location.getBlockZ();

		final Collection<Region> regions = plugin.getRegionProvider().getRegionsAtLocation(world, x, y, z);

		if (regions.isEmpty())
		{
			ChatUtil.sendMessage(player, "Il n'y a aucune protection ici.");
		}

		else
		{
			final StringBuilder str = new StringBuilder("Liste des protections actives ici : ");

			for (final Iterator<Region> it = regions.iterator(); it.hasNext();)
			{
				str.append(it.next().getName());

				if (it.hasNext())
					str.append(", ");
			}

			ChatUtil.sendMessage(player, str.toString());
		}

		final StringBuilder canYouBuild = new StringBuilder("Pouvez-vous construire ? ");
		canYouBuild.append(
				plugin.getRegionManager().canBuildAt(player.getUniqueId(), world, x, y, z) ? "Oui." : "Non.");
		ChatUtil.sendMessage(player, canYouBuild.toString());

		final StringBuilder pvpEnabled = new StringBuilder("PVP activé ? ");
		pvpEnabled.append(plugin.getRegionManager().isPvp(world, x, y, z) ? "Oui." : "Non.");
		ChatUtil.sendMessage(player, pvpEnabled.toString());
	}
}
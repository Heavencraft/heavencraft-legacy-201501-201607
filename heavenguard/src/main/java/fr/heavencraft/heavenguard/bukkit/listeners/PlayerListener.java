package fr.heavencraft.heavenguard.bukkit.listeners;

import java.util.Collection;
import java.util.Iterator;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import fr.heavencraft.heavenguard.api.Region;
import fr.heavencraft.heavenguard.bukkit.HeavenGuard;

public class PlayerListener implements Listener
{
	public PlayerListener()
	{
		DevUtil.registerListener(this);
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

		final Collection<Region> regions = HeavenGuard.getRegionProvider().getRegionsAtLocation(world, x, y, z);

		if (regions.isEmpty())
		{
			HeavenGuard.sendMessage(player, "Il n'y a aucune protection ici.");
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

			HeavenGuard.sendMessage(player, str.toString());
		}

		final StringBuilder canYouBuild = new StringBuilder("Pouvez-vous construire ? ");
		canYouBuild.append(HeavenGuard.getRegionManager().canBuildAt(player.getUniqueId(), world, x, y, z) ? "Oui." : "Non.");
		HeavenGuard.sendMessage(player, canYouBuild.toString());

		final StringBuilder pvpEnabled = new StringBuilder("PVP activé ? ");
		pvpEnabled.append(HeavenGuard.getRegionManager().isPvp(world, x, y, z) ? "Oui." : "Non.");
		HeavenGuard.sendMessage(player, pvpEnabled.toString());
	}
}
package fr.heavencraft.heavencore.bukkit.listeners;

import java.util.Arrays;
import java.util.Collection;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;

import fr.heavencraft.heavencore.CorePermissions;
import fr.heavencraft.heavencore.bukkit.HeavenPlugin;

public final class ForbiddenBlocksListener extends AbstractListener<HeavenPlugin>
{
	private final Collection<Material> types;

	public ForbiddenBlocksListener(HeavenPlugin plugin, Material... types)
	{
		super(plugin);

		this.types = Arrays.asList(types);
	}

	@EventHandler(ignoreCancelled = true)
	private void onBlockPlace(BlockPlaceEvent event)
	{
		final Player player = event.getPlayer();

		if (player.hasPermission(CorePermissions.FORBIDDEN_BLOCKS))
			return;

		if (types.contains(event.getBlockPlaced().getType()))
			event.setCancelled(true);
	}
}
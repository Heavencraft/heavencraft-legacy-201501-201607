package fr.heavencraft.heavencrea.worlds;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.entity.EntityPortalEnterEvent;

import fr.heavencraft.heavencore.CorePermissions;
import fr.heavencraft.heavencore.bukkit.HeavenPlugin;
import fr.heavencraft.heavencore.bukkit.listeners.AbstractListener;

public class PortalListener extends AbstractListener<HeavenPlugin>
{
	private static final String TALENT_PERMISSION = CorePermissions.WORLD_ACCESS + WorldsManager.WORLD_TALENT;
	private static final String ARCHITECT_PERMISSION = CorePermissions.WORLD_ACCESS
			+ WorldsManager.WORLD_ARCHITECT;

	public PortalListener(HeavenPlugin plugin)
	{
		super(plugin);
	}

	@EventHandler
	public void onEntityPortalEnter(EntityPortalEnterEvent event)
	{
		if (event.getEntityType() != EntityType.PLAYER)
			return;

		final Block block = event.getLocation().getBlock();

		if (block.getWorld() != WorldsManager.getWorldCreative())
			return;

		final Player player = (Player) event.getEntity();

		switch (block.getType())
		{
			case ENDER_PORTAL:
				onEndPortalEnter(player);
				break;

			case PORTAL:
				onNetherPortalEnter(player, block);
				break;

			default:
				break;
		}
	}

	private void onEndPortalEnter(final Player player)
	{
		plugin.teleportPlayer(player, WorldsManager.getBiomeSpawnLocation());
		plugin.sendMessage(player, "Vous avez été téléporté dans le monde {CréaBiome}.");
	}

	private void onNetherPortalEnter(final Player player, final Block portal)
	{
		switch (portal.getRelative(BlockFace.DOWN).getType())
		{
			case SAND:
				if (!player.hasPermission(TALENT_PERMISSION))
				{
					plugin.sendMessage(player, "Vous n'avez pas la permission d'accéder au monde {Talent}.");
					return;
				}

				plugin.teleportPlayer(player, WorldsManager.getTalentSpawnLocation());
				plugin.sendMessage(player, "Vous avez été téléporté dans le monde {Talent}.");
				break;

			case SANDSTONE:
				if (!player.hasPermission(ARCHITECT_PERMISSION))
				{
					plugin.sendMessage(player, "Vous n'avez pas la permission d'accéder au monde {Architecte}.");
					return;
				}

				plugin.teleportPlayer(player, WorldsManager.getArchitectSpawnLocation());
				plugin.sendMessage(player, "Vous avez été téléporté dans le monde {Architecte}.");
				break;

			default:
				break;
		}
	}

	@EventHandler(ignoreCancelled = true)
	private void onBlockPhysics(BlockPhysicsEvent event)
	{
		if (event.getBlock().getType() == Material.PORTAL)
			event.setCancelled(true);
	}
}
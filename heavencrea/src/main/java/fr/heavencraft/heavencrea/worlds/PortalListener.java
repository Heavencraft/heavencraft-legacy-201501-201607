package fr.heavencraft.heavencrea.worlds;

import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityPortalEnterEvent;

import fr.heavencraft.heavencore.CorePermissions;
import fr.heavencraft.heavencore.bukkit.HeavenPlugin;
import fr.heavencraft.heavencore.bukkit.listeners.AbstractListener;

public class PortalListener extends AbstractListener
{
	private static final String TALENT_PERMISSION = CorePermissions.WORLD_ACCESS + WorldsManager.WORLD_TALENT;

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
				plugin.teleportPlayer(player, WorldsManager.getBiomeSpawnLocation());
				plugin.sendMessage(player, "Vous avez été téléporté dans le monde {CréaBiome}.");
				break;

			case PORTAL:
				if (player.hasPermission(TALENT_PERMISSION))
					plugin.sendMessage(player, "Vous n'avez pas la permission d'accéder au monde {Talent}.");

				else
				{
					plugin.teleportPlayer(player, WorldsManager.getTalentSpawnLocation());
					plugin.sendMessage(player, "Vous avez été téléporté dans le monde {Talent}.");
				}
				break;

			default:
				break;
		}
	}
}
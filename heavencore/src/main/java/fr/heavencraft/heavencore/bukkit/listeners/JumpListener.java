package fr.heavencraft.heavencore.bukkit.listeners;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerToggleSprintEvent;

import fr.heavencraft.heavencore.bukkit.HeavenPlugin;

public final class JumpListener extends AbstractListener
{
	public JumpListener(HeavenPlugin plugin)
	{
		super(plugin);
	}

	@EventHandler(ignoreCancelled = true)
	private void onPlayerToggleSprint(PlayerToggleSprintEvent event)
	{
		if (!event.isSprinting())
			return;

		final Player player = event.getPlayer();

		if (!player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType().equals(Material.SPONGE))
			return;

		player.setVelocity(player.getVelocity().setY(10));
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	private void onEntityDamage(EntityDamageEvent event)
	{
		if (!event.getEntityType().equals(EntityType.PLAYER) || !event.getCause().equals(DamageCause.FALL))
			return;

		final Block block = event.getEntity().getLocation().getBlock();

		// Dépends du lag du joueur :(
		if (!block.getRelative(0, -1, 0).getType().equals(Material.SPONGE)
				&& !block.getRelative(0, -2, 0).getType().equals(Material.SPONGE)
				&& !block.getRelative(0, -3, 0).getType().equals(Material.SPONGE)
				&& !block.getRelative(0, -4, 0).getType().equals(Material.SPONGE)
				&& !block.getRelative(0, -5, 0).getType().equals(Material.SPONGE))
			return;

		event.setCancelled(true);
	}
}
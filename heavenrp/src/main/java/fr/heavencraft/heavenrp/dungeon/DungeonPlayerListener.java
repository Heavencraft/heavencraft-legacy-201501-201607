package fr.heavencraft.heavenrp.dungeon;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import fr.heavencraft.heavencore.bukkit.HeavenPlugin;
import fr.heavencraft.heavencore.bukkit.listeners.AbstractListener;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavenrp.dungeon.DungeonManager.Dungeon;

public class DungeonPlayerListener extends AbstractListener<HeavenPlugin>
{
	public DungeonPlayerListener(HeavenPlugin plugin)
	{
		super(plugin);
	}

	@EventHandler
	public void onPlayerDisconnect(PlayerQuitEvent e) throws HeavenException
	{
		// Is the player inside a dungeon?
		Dungeon dg = DungeonManager.getDungeonByPlayer(e.getPlayer());
		if (dg == null)
			return;
		DungeonManager.PlayerLeave(e.getPlayer(), true);
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerDamageByEntity(final EntityDamageByEntityEvent e) throws HeavenException
	{
		// Is it a player?
		if (!(e.getEntityType() == EntityType.PLAYER))
			return;
		Player p = (Player) e.getEntity();

		// Is the player inside a dungeon?
		Dungeon dg = DungeonManager.getDungeonByPlayer(p);
		if (dg == null)
			return;
		// Would the player die?
		if (p.getHealth() <= e.getFinalDamage())
		{
			e.setDamage(0);
			DungeonManager.PlayerDies(p, dg.getName());
			e.setCancelled(true);
			return;
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onOtherDamageTypes(final EntityDamageEvent e) throws HeavenException
	{
		if (e.getEntityType() != EntityType.PLAYER)
			return;

		Player p = (Player) e.getEntity();
		// Is the player inside a dungeon?
		Dungeon dg = DungeonManager.getDungeonByPlayer(p);
		if (dg == null)
			return;
		// Damages types to ignore
//		if (e.getCause() == DamageCause.FALL)
//			return;
		// Would the player die?
		if (p.getHealth() <= e.getFinalDamage())
		{
			e.setDamage(0);
			DungeonManager.PlayerDies(p, dg.getName());
			e.setCancelled(true);
			return;
		}
	}
}

package fr.lorgan17.heavenrp.listeners;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileHitEvent;

import fr.heavencraft.heavencore.bukkit.HeavenPlugin;
import fr.heavencraft.heavencore.bukkit.listeners.AbstractListener;

public class SnowballListener extends AbstractListener<HeavenPlugin>
{
	public SnowballListener(HeavenPlugin plugin)
	{
		super(plugin);
	}

	@EventHandler
	public void onProjectileHit(ProjectileHitEvent event)
	{
		if (event.getEntityType() != EntityType.SNOWBALL)
			return;

		Snowball snowball = (Snowball) event.getEntity();

		for (Entity entity : snowball.getNearbyEntities(1, 1, 1))
			if (entity.getType() == EntityType.PLAYER)
				((Player) entity).damage(0);
	}
}
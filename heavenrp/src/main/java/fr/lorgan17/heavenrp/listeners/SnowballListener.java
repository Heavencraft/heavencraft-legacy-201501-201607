package fr.lorgan17.heavenrp.listeners;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

import fr.heavencraft.utils.DevUtil;

public class SnowballListener implements Listener
{
	public SnowballListener()
	{
		DevUtil.registerListener(this);
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
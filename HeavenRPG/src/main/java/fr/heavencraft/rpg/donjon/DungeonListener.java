package fr.heavencraft.rpg.donjon;

import org.bukkit.Bukkit;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityCombustByEntityEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import fr.heavencraft.Utils.DevUtils;
import fr.heavencraft.rpg.HeavenRPG;
import fr.heavencraft.rpg.donjon.Dungeon.DungeonRoom;

public class DungeonListener implements Listener {
	public DungeonListener()
	{
		Bukkit.getPluginManager().registerEvents(this, HeavenRPG.getInstance());
	}
	
	/**
	 * Track mobs spawning into a dungeons.
	 * @param event
	 */
	@EventHandler(priority = EventPriority.HIGH)
	public void onCreatureSpawn(CreatureSpawnEvent event)
	{
		if (!(event.getEntity() instanceof LivingEntity))
			return;

		// Mobs excluded from track-list.
		if(event.getEntity().getType() == EntityType.BAT ||
				event.getEntity().getType() == EntityType.CREEPER ||
				event.getEntity().getType() == EntityType.ENDERMAN)
			return;
		
		DungeonRoom dgr = DungeonManager.getRoomByLocation(event.getEntity().getLocation());
		if(dgr != null)
			dgr.add_mob(event.getEntity());
		return;
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onCreatureDespawn(ItemDespawnEvent event)
	{
		if (!(event.getEntity() instanceof LivingEntity)) 
			return;
		DungeonRoom dgr = DungeonManager.getRoomByEntity(event.getEntity());
		if(dgr != null)
			dgr.remove_mob(event.getEntity());
		return;
	}

	@EventHandler (ignoreCancelled = true, priority = EventPriority.HIGH)
	public void onEntityDeath(EntityDeathEvent event) {
		if (!(event.getEntity() instanceof LivingEntity)) 
			return;
		DungeonRoom dgr = DungeonManager.getRoomByEntity(event.getEntity());
		if(dgr != null)
			dgr.remove_mob(event.getEntity());
		return;
	}
	
	@EventHandler (ignoreCancelled = true, priority = EventPriority.HIGH)
	public void onEntityExplode(EntityExplodeEvent event) {
		if (!(event.getEntity() instanceof LivingEntity)) 
			return;
		DungeonRoom dgr = DungeonManager.getRoomByEntity(event.getEntity());
		if(dgr != null)
			dgr.remove_mob(event.getEntity());
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerBurn(EntityCombustByEntityEvent e) {
		if (!(e.getEntity() instanceof Player))
			return;	
		Player victim = (Player) e.getEntity();		
		Dungeon dg = DungeonManager.getDungeonByUser(victim);
		if(dg == null)
			return;
		Entity combuster = e.getCombuster();
		  if (combuster instanceof Projectile) {
			  LivingEntity attacker = (LivingEntity) ((Projectile)combuster).getShooter();
			  if(!(attacker instanceof Player))
				  return;
			  e.setDuration(0);
			  e.setCancelled(true);
			  return;
		  }
		if(DungeonManager.is_debug())
			DevUtils.log("~~onPlayerCombust: || Victim Health: {%1$s} || Duration left: {%2$s}", victim.getHealth() + " ",e.getDuration() + " ");		
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerDamageByEntity(EntityDamageByEntityEvent e) {
		if (!(e.getEntity() instanceof Player))
			return;	
		Player victim = (Player) e.getEntity();		
		Dungeon dg = DungeonManager.getDungeonByUser(victim);
		if(dg == null)
			return;
		
		// Protect form teamkill with arrows.
		if((e.getDamager() instanceof Arrow)) {
			Arrow a = (Arrow)e.getDamager();	
			if (!(a.getShooter() instanceof Player)) 
			      return;
			e.setDamage(0);
			e.setCancelled(true);
			return;     
		}
		
		//On annule les dégats caussées par les joueurs entre-eux
		if(e.getDamager().getType() == EntityType.PLAYER) {
			e.setDamage(0);
			e.setCancelled(true);
			return;
		}
		
		double caused = e.getDamage() + e.getDamage(DamageModifier.ABSORPTION) + e.getDamage(DamageModifier.ARMOR) 
				+ e.getDamage(DamageModifier.BLOCKING) + e.getDamage(DamageModifier.MAGIC) + e.getDamage(DamageModifier.RESISTANCE);
				
		if(DungeonManager.is_debug()) {
			DevUtils.log("~~onPlayerDamageByEntity: || Victim Health: {%1$s} || EventCausedDamage: {%2$s} || Is Deadly: {%3$s}",victim.getHealth() + " ",e.getDamage() + "(" + e.getDamage(DamageModifier.ARMOR) + ") ", ((victim.getHealth() - e.getDamage()) <= 0) + " " );
			DevUtils.log("Health {%1$s} | totaldamage: {%2$s} | subi: {%3$s} | lethal: {%4$s} ", victim.getHealth(), e.getDamage(), caused, victim.getHealth() - caused);
			
		}
			
		if (victim.getHealth() - caused <= 0)
		{
			e.setDamage(0);
			dg.handlePlayerDeath(victim);
			e.setCancelled(true);
			return;
		}
	}
	
	@EventHandler (priority = EventPriority.MONITOR)
	public void onPlayerDies(PlayerDeathEvent event) {
		Dungeon dg = DungeonManager.getDungeonByUser(event.getEntity());
		if(dg == null)
			return;
		dg.handlePlayerDeath(event.getEntity());
	}
	
	@EventHandler (ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onPlayerDisconnect(PlayerQuitEvent event) {
		Dungeon dg = DungeonManager.getDungeonByUser(event.getPlayer());
		if(dg == null)
			return;
		dg.handlePlayerDisconnect(event.getPlayer());
	}
}

package fr.heavencraft.rpg.player;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import fr.heavencraft.rpg.HeavenRPG;

public class RPGPlayerListener implements Listener {
	public RPGPlayerListener()
	{
		Bukkit.getPluginManager().registerEvents(this, HeavenRPG.getInstance());
	}
	
	// When a player joins the server, create a RPGPlayer for them
		@EventHandler(priority = EventPriority.NORMAL)
		public void onJoinCreateRPGPlayer(PlayerJoinEvent e) {
			Player p = e.getPlayer();
			RPGPlayer IP = new RPGPlayer(p);
			RPGPlayerManager.createRPGPlayer(IP);
		}

		// When a player leaves the server willingly, delete the RPGPlayer of them
		@EventHandler(priority = EventPriority.NORMAL)
		public void onLeaveDeleteRPGPlayer(final PlayerQuitEvent e) {
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(HeavenRPG.getInstance(), new Runnable()
			{
				@Override
				public void run() {

					RPGPlayer IP = RPGPlayerManager.getRPGPlayer(e.getPlayer());
					RPGPlayerManager.removeRPGPlayer(IP);
				}
			}, 2L);
		}

		// When a player leaves the server by kick, delete the RPGPlayer of them
		@EventHandler(priority = EventPriority.NORMAL)
		public void onKickedDeleteRPGPlayer(final PlayerKickEvent e) {
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(HeavenRPG.getInstance(), new Runnable()
			{

				@Override
				public void run() {

					RPGPlayer IP = RPGPlayerManager.getRPGPlayer(e.getPlayer());
					RPGPlayerManager.removeRPGPlayer(IP);
				}
			}, 2L);
		}
}

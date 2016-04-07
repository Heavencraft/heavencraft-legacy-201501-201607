package fr.heavencraft.heavenevent;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import fr.heavencraft.heavenevent.timer.TimerScoreboard;

public class PlayerJoinListener implements Listener
{

	public PlayerJoinListener(HeavenEvent plugin)
	{
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	private void onPlayerConnect(PlayerJoinEvent event)
	{
		// load Timer board
		event.getPlayer().setScoreboard(TimerScoreboard.getTimer());
	}

}

package fr.heavencraft.heavenrp.questframework;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import fr.heavencraft.heavencore.bukkit.HeavenPlugin;
import fr.heavencraft.heavencore.bukkit.listeners.AbstractListener;
import fr.heavencraft.heavenrp.HeavenRP;

/**
 * This listener handles loading and unloading quests for online players
 * 
 * @author Manuel
 *
 */
public class QuestGeneralListener extends AbstractListener<HeavenPlugin>
{
	public QuestGeneralListener(HeavenRP plugin)
	{
		super(plugin);
	}

	/**
	 * Loads a User's state
	 * 
	 * @param event
	 */
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event)
	{
		// TODO Load quest & context on player join
		System.out.println(String.format("%1$s has joined, loading quests...", event.getPlayer().getName()));
		// Load player context
		ContextManager.get().loadPlayerContext(event.getPlayer().getUniqueId());
	}

	/**
	 * Unregister user's state
	 * 
	 * @param event
	 */
	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent event)
	{
		// TODO Unload quest & context on player disconnect
		System.out.println(String.format("%1$s has left, clearing cached quests...", event.getPlayer().getName()));
	}

}

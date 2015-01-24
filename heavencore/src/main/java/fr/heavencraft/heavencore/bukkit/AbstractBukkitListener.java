package fr.heavencraft.heavencore.bukkit;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import fr.heavencraft.heavencore.logs.HeavenLog;

abstract class AbstractBukkitListener implements Listener
{
	protected final HeavenLog log = HeavenLog.getLogger(getClass());

	protected AbstractBukkitListener(String pluginName)
	{
		Bukkit.getPluginManager().registerEvents(this, Bukkit.getPluginManager().getPlugin(pluginName));
	}
}
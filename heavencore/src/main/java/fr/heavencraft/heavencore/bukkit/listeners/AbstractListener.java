package fr.heavencraft.heavencore.bukkit.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import fr.heavencraft.heavencore.bukkit.HeavenPlugin;
import fr.heavencraft.heavencore.logs.HeavenLog;

public abstract class AbstractListener<P extends HeavenPlugin> implements Listener
{
	protected final HeavenLog log = HeavenLog.getLogger(getClass());
	protected final P plugin;

	protected AbstractListener(P plugin)
	{
		this.plugin = plugin;

		Bukkit.getPluginManager().registerEvents(this, plugin);
	}
}
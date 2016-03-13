package fr.heavencraft.heavencore;

import org.bukkit.plugin.java.JavaPlugin;

import fr.heavencraft.async.actions.ActionsHandler;
import fr.heavencraft.async.queries.QueriesHandler;
import fr.heavencraft.heavencore.logs.BukkitHeavenLogFactory;
import fr.heavencraft.heavencore.logs.HeavenLog;

public class CorePlugin extends JavaPlugin
{
	private static CorePlugin instance;

	public CorePlugin()
	{
		HeavenLog.setFactory(new BukkitHeavenLogFactory());
	}

	@Override
	public void onEnable()
	{
		super.onEnable();
		instance = this;

		new ActionsHandler();
		new QueriesHandler();
	}

	public static CorePlugin getInstance()
	{
		return instance;
	}
}
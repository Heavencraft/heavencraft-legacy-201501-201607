package fr.heavencraft.heavencore;

import org.bukkit.plugin.java.JavaPlugin;

import fr.heavencraft.async.actions.ActionsHandler;
import fr.heavencraft.async.queries.BukkitQueriesHandlerTask;
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
		new BukkitQueriesHandlerTask();
		new BukkitTickCounterTask(this);
	}

	public static CorePlugin getInstance()
	{
		return instance;
	}
}
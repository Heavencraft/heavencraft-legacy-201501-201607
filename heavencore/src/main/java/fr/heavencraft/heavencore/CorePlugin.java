package fr.heavencraft.heavencore;

import org.bukkit.plugin.java.JavaPlugin;

import fr.heavencraft.async.actions.ActionsHandler;
import fr.heavencraft.async.queries.QueriesHandler;

public class CorePlugin extends JavaPlugin
{
	private static CorePlugin instance;

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
package fr.heavencraft.heavencore;

import org.bukkit.plugin.java.JavaPlugin;

public class CorePlugin extends JavaPlugin
{
	private static CorePlugin instance;

	@Override
	public void onEnable()
	{
		super.onEnable();
		instance = this;
	}

	public static CorePlugin getInstance()
	{
		return instance;
	}
}
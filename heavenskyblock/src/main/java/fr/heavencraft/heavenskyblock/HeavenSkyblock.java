package fr.heavencraft.heavenskyblock;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import fr.heavencraft.heavencore.bukkit.HeavenPlugin;
import fr.heavencraft.heavencore.bukkit.commands.SpawnCommand;
import fr.heavencraft.heavencore.bukkit.listeners.NoChatListener;

public class HeavenSkyblock extends HeavenPlugin
{
	@Override
	public void onEnable()
	{
		super.onEnable();

		new NoChatListener(this);
	}

	@Override
	protected void afterEnable()
	{
		super.afterEnable();

		final Location spawnLocation = Bukkit.getWorld("ASkyBlock").getSpawnLocation();
		new SpawnCommand(this, spawnLocation);
		new ServerListener(this, spawnLocation);
	}
}
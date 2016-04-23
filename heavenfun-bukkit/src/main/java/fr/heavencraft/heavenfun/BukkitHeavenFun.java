package fr.heavencraft.heavenfun;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import fr.heavencraft.heavencore.bukkit.HeavenPlugin;
import fr.heavencraft.heavencore.bukkit.listeners.ColoredSignsListener;
import fr.heavencraft.heavencore.bukkit.listeners.JumpListener;
import fr.heavencraft.heavencore.bukkit.listeners.LinkSignListener;
import fr.heavencraft.heavencore.bukkit.listeners.NoChatListener;
import fr.heavencraft.heavencore.bukkit.listeners.RedstoneLampListener;
import fr.heavencraft.heavencore.sql.ConnectionProvider;
import fr.heavencraft.heavencore.sql.Database;
import fr.heavencraft.heavencore.sql.HikariConnectionProvider;
import fr.heavencraft.heavenfun.commands.CommandsManager;
import fr.heavencraft.heavenfun.common.HeavenFun;
import fr.heavencraft.heavenfun.common.HeavenFunInstance;

public class BukkitHeavenFun extends HeavenPlugin implements HeavenFun
{
	private static Location spawn;

	private final ConnectionProvider connectionProvider;

	public BukkitHeavenFun()
	{
		HeavenFunInstance.set(this);
		connectionProvider = new HikariConnectionProvider(Database.UAT_FUN);
	}

	@Override
	public void onEnable()
	{
		super.onEnable();

		CommandsManager.init();

		new ServerListener(this);

		new JumpListener(this);
		new NoChatListener(this);
		new RedstoneLampListener(this);

		new ColoredSignsListener(this);
		new LinkSignListener(this);
	}

	public static Location getSpawn()
	{
		if (spawn == null)
			spawn = new Location(Bukkit.getWorld("world"), 0.5, 100.0, 0.5);

		return spawn;
	}

	@Override
	public ConnectionProvider getConnectionProvider()
	{
		return connectionProvider;
	}
}
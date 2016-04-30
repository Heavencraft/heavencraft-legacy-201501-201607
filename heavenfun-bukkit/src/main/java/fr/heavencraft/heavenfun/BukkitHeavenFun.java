package fr.heavencraft.heavenfun;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import fr.heavencraft.heavencore.bukkit.HeavenPlugin;
import fr.heavencraft.heavencore.bukkit.listeners.ColoredSignsListener;
import fr.heavencraft.heavencore.bukkit.listeners.JumpListener;
import fr.heavencraft.heavencore.bukkit.listeners.LinkSignListener;
import fr.heavencraft.heavencore.bukkit.listeners.NoChatListener;
import fr.heavencraft.heavencore.bukkit.listeners.RedstoneLampListener;
import fr.heavencraft.heavencore.sql.ConnectionHandlerFactory;
import fr.heavencraft.heavencore.sql.ConnectionProvider;
import fr.heavencraft.heavencore.sql.Database;
import fr.heavencraft.heavencore.users.UsersListener;
import fr.heavencraft.heavenfun.commands.CommandsManager;
import fr.heavencraft.heavenfun.common.HeavenFun;
import fr.heavencraft.heavenfun.common.HeavenFunInstance;
import fr.heavencraft.heavenfun.common.users.FunUserProvider;

public class BukkitHeavenFun extends HeavenPlugin implements HeavenFun
{
	private static Location spawn;

	private final ConnectionProvider connectionProvider;

	public BukkitHeavenFun()
	{
		HeavenFunInstance.set(this);
		connectionProvider = ConnectionHandlerFactory.getConnectionHandler(Database.UAT_FUN);
	}

	@Override
	public void onEnable()
	{
		super.onEnable();

		CommandsManager.init(this);

		new ServerListener(this);

		new JumpListener(this);
		new NoChatListener(this);
		new RedstoneLampListener(this);
		new UsersListener(this, FunUserProvider.get());

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
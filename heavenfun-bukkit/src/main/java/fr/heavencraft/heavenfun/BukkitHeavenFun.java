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
import fr.heavencraft.heavencore.users.UsersListener;
import fr.heavencraft.heavencore.users.balance.MoneyOnFirstLoginListener;
import fr.heavencraft.heavencore.users.balance.MoneyTask;
import fr.heavencraft.heavenfun.commands.CommandsManager;
import fr.heavencraft.heavenfun.common.HeavenFun;
import fr.heavencraft.heavenfun.common.HeavenFunInstance;
import fr.heavencraft.heavenfun.common.users.FunUserProvider;
import fr.heavencraft.heavenfun.economy.ShopSignListener;

public class BukkitHeavenFun extends HeavenPlugin implements HeavenFun
{
	private static Location spawn;

	private ConnectionProvider connectionProvider;

	public BukkitHeavenFun()
	{
		HeavenFunInstance.set(this);
	}

	@Override
	public void onEnable()
	{
		super.onEnable();
		connectionProvider = ConnectionHandlerFactory.getConnectionHandler(loadDatabase(getConfig(), "database"));

		CommandsManager.init(this);

		new ServerListener(this);

		// Economy
		new MoneyOnFirstLoginListener(this, FunUserProvider.get(), 5); // Connexion en Fun: 5 FP
		new MoneyTask(this, FunUserProvider.get(), 7200); // 1h connecté = 10 FP
		new ShopSignListener(this);

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
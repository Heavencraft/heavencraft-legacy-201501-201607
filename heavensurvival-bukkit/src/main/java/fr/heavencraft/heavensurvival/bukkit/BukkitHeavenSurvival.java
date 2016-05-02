package fr.heavencraft.heavensurvival.bukkit;

import org.bukkit.Bukkit;

import fr.heavencraft.heavencore.bukkit.HeavenPlugin;
import fr.heavencraft.heavencore.bukkit.listeners.NoChatListener;
import fr.heavencraft.heavencore.sql.ConnectionHandlerFactory;
import fr.heavencraft.heavencore.sql.ConnectionProvider;
import fr.heavencraft.heavencore.users.UsersListener;
import fr.heavencraft.heavensurvival.bukkit.protection.ProtectionCommand;
import fr.heavencraft.heavensurvival.bukkit.protection.SelectionManager;
import fr.heavencraft.heavensurvival.bukkit.pvp.PVPCommand;
import fr.heavencraft.heavensurvival.bukkit.pvp.PVPListener;
import fr.heavencraft.heavensurvival.bukkit.users.LitCommand;
import fr.heavencraft.heavensurvival.bukkit.worlds.DifficultyTask;
import fr.heavencraft.heavensurvival.bukkit.worlds.SetspawnCommand;
import fr.heavencraft.heavensurvival.bukkit.worlds.SpawnCommand;
import fr.heavencraft.heavensurvival.common.HeavenSurvival;
import fr.heavencraft.heavensurvival.common.HeavenSurvivalInstance;
import fr.heavencraft.heavensurvival.common.users.SurvivalUserProvider;

public class BukkitHeavenSurvival extends HeavenPlugin implements HeavenSurvival
{
	private ConnectionProvider connectionProvider;

	public BukkitHeavenSurvival()
	{
		HeavenSurvivalInstance.set(this);
	}

	@Override
	public ConnectionProvider getConnectionProvider()
	{
		return connectionProvider;
	}

	@Override
	public void onEnable()
	{
		try
		{
			connectionProvider = ConnectionHandlerFactory.getConnectionHandler(loadDatabase(getConfig(), "database"));

			new ProtectionCommand(this);
			new LitCommand(this);
			new SpawnCommand(this);
			new SetspawnCommand(this);

			new NoChatListener(this);
			new SelectionManager(this);
			new DifficultyTask(this);
			new UsersListener(this, SurvivalUserProvider.get());

			// PVP
			new PVPCommand(this);
			new PVPListener(this);
		}
		catch (final Throwable t)
		{
			t.printStackTrace();
			Bukkit.shutdown();
		}
	}
}
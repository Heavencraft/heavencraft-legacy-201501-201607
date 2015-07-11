package fr.heavencraft.hellcraft;

import org.bukkit.Bukkit;

import fr.heavencraft.heavencore.bukkit.HeavenPlugin;
import fr.heavencraft.heavencore.bukkit.commands.SpawnCommand;
import fr.heavencraft.heavencore.bukkit.listeners.NoChatListener;
import fr.heavencraft.heavencore.sql.ConnectionHandler;
import fr.heavencraft.heavencore.sql.ConnectionHandlerFactory;
import fr.heavencraft.heavencore.sql.Database;
import fr.heavencraft.hellcraft.back.BackCommand;
import fr.heavencraft.hellcraft.back.BackListener;
import fr.heavencraft.hellcraft.hps.HpsCommand;
import fr.heavencraft.hellcraft.hps.HpsManager;
import fr.heavencraft.hellcraft.worlds.WorldsManager;

public class HellCraft extends HeavenPlugin
{
	private HpsManager hpsManager;

	@Override
	public void onEnable()
	{
		try
		{
			super.onEnable();

			final ConnectionHandler webConnection = ConnectionHandlerFactory.getConnectionHandler(Database.WEB);
			hpsManager = new HpsManager(webConnection);

			new NoChatListener(this); // Chat is handled by the proxy

			new PlayerListener(this);
			new BackListener(this);
			new BackCommand(this);
			new HpsCommand(this);
		}
		catch (final Exception ex)
		{
			ex.printStackTrace();
			Bukkit.shutdown();
		}
	}

	@Override
	protected void afterEnable()
	{
		WorldsManager.init();
		new SpawnCommand(this, WorldsManager.getSpawnLocation());
	}

	public HpsManager getHpsManager()
	{
		return hpsManager;
	}
}

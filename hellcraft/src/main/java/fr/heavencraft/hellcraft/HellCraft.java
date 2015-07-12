package fr.heavencraft.hellcraft;

import org.bukkit.Bukkit;

import fr.heavencraft.heavencore.bukkit.HeavenPlugin;
import fr.heavencraft.heavencore.bukkit.commands.AccepterCommand;
import fr.heavencraft.heavencore.bukkit.commands.RejoindreCommand;
import fr.heavencraft.heavencore.bukkit.commands.SpawnCommand;
import fr.heavencraft.heavencore.bukkit.listeners.NoChatListener;
import fr.heavencraft.heavencore.bukkit.listeners.RedstoneLampListener;
import fr.heavencraft.heavencore.sql.ConnectionHandler;
import fr.heavencraft.heavencore.sql.ConnectionHandlerFactory;
import fr.heavencraft.heavencore.sql.Database;
import fr.heavencraft.hellcraft.back.BackListener;
import fr.heavencraft.hellcraft.hps.HpsCommand;
import fr.heavencraft.hellcraft.hps.HpsManager;
import fr.heavencraft.hellcraft.hps.PtimeCommand;
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

			final ConnectionHandler webConnection = ConnectionHandlerFactory
					.getConnectionHandler(Database.HELLCRAFT_WEB);
			hpsManager = new HpsManager(webConnection);

			new NoChatListener(this); // Chat is handled by the proxy

			new RedstoneLampListener(this);
			new PlayerListener(this);
			new BackListener(this);
			new HpsCommand(this);

			// VIP
			new RejoindreCommand(this);
			new PtimeCommand(this);

			// Non-vip
			new AccepterCommand(this);

			WorldsManager.init();
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
		new SpawnCommand(this, WorldsManager.getSpawnLocation());
		getCommand("spawn").setPermission(HellCraftPermissions.SPAWN_COMMAND);
	}

	public HpsManager getHpsManager()
	{
		return hpsManager;
	}
}

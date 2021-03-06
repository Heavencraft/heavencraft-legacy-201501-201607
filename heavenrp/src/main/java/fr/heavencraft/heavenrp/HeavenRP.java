package fr.heavencraft.heavenrp;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

import fr.heavencraft.heavencore.bukkit.HeavenPlugin;
import fr.heavencraft.heavencore.bukkit.commands.SpawnCommand;
import fr.heavencraft.heavencore.bukkit.commands.TutoCommand;
import fr.heavencraft.heavencore.sql.ConnectionHandlerFactory;
import fr.heavencraft.heavencore.sql.ConnectionProvider;
import fr.heavencraft.heavencore.sql.Database;
import fr.heavencraft.heavenrp.jobs.JobsProvider;
import fr.heavencraft.heavenrp.stores.StoresListener;
import fr.heavencraft.heavenrp.stores.StoresManager;
import fr.heavencraft.heavenrp.structureblock.StructureBlock;
import fr.heavencraft.heavenrp.worlds.WorldsManager;
import fr.lorgan17.heavenrp.managers.AuctionManager;

public class HeavenRP extends HeavenPlugin
{
	private static WorldGuardPlugin _WGP;
	private static HeavenRP _instance;

	private static ConnectionProvider srpConnection;
	private static ConnectionProvider mainConnection;

	private static StoresManager _storesManager;
	private static AuctionManager _auctionManager;

	public static Random Random = new Random();

	@Override
	public void onEnable()
	{
		try
		{
			super.onEnable();

			_instance = this;

			// Load jobs configuration (do it first as it can take time)
			JobsProvider.loadConfig();

			// Load structure for Structure Block
			StructureBlock.loadStructure();

			srpConnection = ConnectionHandlerFactory.getConnectionHandler(loadDatabase(getConfig(), "database"));
			mainConnection = ConnectionHandlerFactory.getConnectionHandler(loadDatabase(getConfig(), "web.database"));

			InitManager.init(this);

			_auctionManager = new AuctionManager();

			// Stores
			new StoresListener(this);
			_storesManager = new StoresManager(this);
			_storesManager.init();
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
		super.afterEnable();

		new SpawnCommand(this, WorldsManager.getSpawn());
		new TutoCommand(this, WorldsManager.getTutoLocation());
	}

	public static Connection getConnection() throws SQLException
	{
		return srpConnection.getConnection();
	}

	@Deprecated
	public static Connection getMainConnection() throws SQLException
	{
		return mainConnection.getConnection();
	}

	public static HeavenRP getInstance()
	{
		return _instance;
	}

	public StoresManager getStoresManager()
	{
		return _storesManager;
	}

	public static AuctionManager getAuctionManager()
	{
		return _auctionManager;
	}

	public static WorldGuardPlugin getWorldGuard()
	{

		if (_WGP == null)
		{
			final Plugin plugin = getInstance().getServer().getPluginManager().getPlugin("WorldGuard");
			if (plugin == null || !(plugin instanceof WorldGuardPlugin))
			{
				_WGP = null;
			}
			else
			{
				_WGP = (WorldGuardPlugin) plugin;
			}
		}
		return _WGP;
	}
}
package fr.heavencraft.heavencrea;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.Plugin;

import fr.heavencraft.heavencore.bukkit.HeavenPlugin;
import fr.heavencraft.heavencore.bukkit.commands.AccepterCommand;
import fr.heavencraft.heavencore.bukkit.commands.RejoindreCommand;
import fr.heavencraft.heavencore.bukkit.commands.SpawnCommand;
import fr.heavencraft.heavencore.bukkit.commands.TpCommand;
import fr.heavencraft.heavencore.bukkit.commands.TphereCommand;
import fr.heavencraft.heavencore.bukkit.commands.TpposCommand;
import fr.heavencraft.heavencore.bukkit.listeners.ClearWeatherListener;
import fr.heavencraft.heavencore.bukkit.listeners.ForbiddenBlocksListener;
import fr.heavencraft.heavencore.bukkit.listeners.JumpListener;
import fr.heavencraft.heavencore.bukkit.listeners.NoChatListener;
import fr.heavencraft.heavencore.bukkit.listeners.RedstoneLampListener;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.sql.ConnectionHandler;
import fr.heavencraft.heavencore.sql.ConnectionHandlerFactory;
import fr.heavencraft.heavencore.sql.Database;
import fr.heavencraft.heavencrea.generator.CreativeChunkGenerator;
import fr.heavencraft.heavencrea.hps.HpsCommand;
import fr.heavencraft.heavencrea.hps.HpsManager;
import fr.heavencraft.heavencrea.plots.ParcelleCommand;
import fr.heavencraft.heavencrea.plots.PlotCommand;
import fr.heavencraft.heavencrea.plots.PlotSignListener;
import fr.heavencraft.heavencrea.users.JetonsCommand;
import fr.heavencraft.heavencrea.users.JetonsTask;
import fr.heavencraft.heavencrea.users.UserListener;
import fr.heavencraft.heavencrea.users.UserProvider;
import fr.heavencraft.heavencrea.users.homes.BuyhomeCommand;
import fr.heavencraft.heavencrea.users.homes.HomeCommand;
import fr.heavencraft.heavencrea.users.homes.SethomeCommand;
import fr.heavencraft.heavencrea.users.homes.TphomeCommand;
import fr.heavencraft.heavencrea.worlds.PortalListener;
import fr.heavencraft.heavencrea.worlds.WorldsManager;
import fr.heavencraft.heavenguard.bukkit.HeavenGuard;

public class HeavenCrea extends HeavenPlugin
{

	private UserProvider userProvider;
	private HpsManager hpsManager;

	@Override
	public void onEnable()
	{
		try
		{
			super.onEnable();

			final ConnectionHandler creaConnection = ConnectionHandlerFactory.getConnectionHandler(Database.CREATIVE);
			final ConnectionHandler webConnection = ConnectionHandlerFactory.getConnectionHandler(Database.WEB);

			userProvider = new UserProvider(creaConnection);
			hpsManager = new HpsManager(webConnection);

			/*
			 * Commands and listeners from HeavenCrea (Bukkit code)
			 */

			// HPs
			new HpsCommand(this);

			// Plots
			final HeavenGuard hGuard = loadHeavenGuard();
			new ParcelleCommand(this, hGuard);
			new PlotSignListener(this, hGuard);
			new PlotCommand(this, hGuard);

			// Users
			new JetonsCommand(this);
			new JetonsTask(this);
			new UserListener(this);

			// Homes
			new BuyhomeCommand(this);
			new HomeCommand(this);
			new SethomeCommand(this);
			new TphomeCommand(this);

			// Worlds
			new PortalListener(this);

			Bukkit.getScheduler().runTaskLater(this, new Runnable()
			{
				@Override
				public void run()
				{
					WorldsManager.init();
				}
			}, 100);

			/*
			 * Commands and listener from HeavenCore (Bukkit code)
			 */

			new AccepterCommand(this);
			new RejoindreCommand(this);
			new SpawnCommand(this, "world_creative", 8, 44, 8, 0, 0);

			new TpCommand(this);
			new TphereCommand(this);
			new TpposCommand(this);

			new ClearWeatherListener(this);
			new ForbiddenBlocksListener(this, Material.BARRIER);
			new JumpListener(this);
			new NoChatListener(this);
			new RedstoneLampListener(this);
		}
		catch (final Exception ex)
		{
			ex.printStackTrace();
			Bukkit.shutdown();
		}
	}

	private static HeavenGuard loadHeavenGuard() throws HeavenException
	{
		final Plugin plugin = Bukkit.getPluginManager().getPlugin("HeavenGuard");

		if (plugin != null && plugin instanceof HeavenGuard)
		{
			return (HeavenGuard) plugin;
		}

		throw new HeavenException("Impossible to load HeavenGuard");
	}

	public UserProvider getUserProvider()
	{
		return userProvider;
	}

	public HpsManager getHpsManager()
	{
		return hpsManager;
	}

	@Override
	public ChunkGenerator getDefaultWorldGenerator(String worldName, String id)
	{
		if (WorldsManager.WORLD_CREATIVE.equals(worldName))
			return new CreativeChunkGenerator();

		return super.getDefaultWorldGenerator(worldName, id);
	}
}
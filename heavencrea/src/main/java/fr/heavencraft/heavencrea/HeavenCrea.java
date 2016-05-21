package fr.heavencraft.heavencrea;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.Plugin;

import fr.heavencraft.deprecated.DeprecatedHasUserProvider;
import fr.heavencraft.deprecated.DeprecatedUserProvider;
import fr.heavencraft.heavencore.bukkit.HeavenPlugin;
import fr.heavencraft.heavencore.bukkit.commands.AccepterCommand;
import fr.heavencraft.heavencore.bukkit.commands.RejoindreCommand;
import fr.heavencraft.heavencore.bukkit.commands.SpawnCommand;
import fr.heavencraft.heavencore.bukkit.commands.TpCommand;
import fr.heavencraft.heavencore.bukkit.commands.TphereCommand;
import fr.heavencraft.heavencore.bukkit.commands.TpposCommand;
import fr.heavencraft.heavencore.bukkit.listeners.ClearWeatherListener;
import fr.heavencraft.heavencore.bukkit.listeners.ColoredSignsListener;
import fr.heavencraft.heavencore.bukkit.listeners.ForbiddenBlocksListener;
import fr.heavencraft.heavencore.bukkit.listeners.JumpListener;
import fr.heavencraft.heavencore.bukkit.listeners.LinkSignListener;
import fr.heavencraft.heavencore.bukkit.listeners.NoChatListener;
import fr.heavencraft.heavencore.bukkit.listeners.RedstoneLampListener;
import fr.heavencraft.heavencore.bukkit.listeners.WorldAccessListener;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.sql.ConnectionHandlerFactory;
import fr.heavencraft.heavencore.sql.ConnectionProvider;
import fr.heavencraft.heavencore.sql.Database;
import fr.heavencraft.heavencrea.generator.CreativeChunkGenerator;
import fr.heavencraft.heavencrea.hps.HpsCommand;
import fr.heavencraft.heavencrea.hps.HpsManager;
import fr.heavencraft.heavencrea.plots.ParcelleCommand;
import fr.heavencraft.heavencrea.plots.PlotCommand;
import fr.heavencraft.heavencrea.plots.PlotSignListener;
import fr.heavencraft.heavencrea.plots.TalentSignListener;
import fr.heavencraft.heavencrea.users.CreativeUser;
import fr.heavencraft.heavencrea.users.CreativeUserListener;
import fr.heavencraft.heavencrea.users.CreativeUserProvider;
import fr.heavencraft.heavencrea.users.JetonsCommand;
import fr.heavencraft.heavencrea.users.JetonsTask;
import fr.heavencraft.heavencrea.users.color.TabColorListener;
import fr.heavencraft.heavencrea.users.homes.BuyhomeCommand;
import fr.heavencraft.heavencrea.users.homes.HomeCommand;
import fr.heavencraft.heavencrea.users.homes.SethomeCommand;
import fr.heavencraft.heavencrea.users.homes.TphomeCommand;
import fr.heavencraft.heavencrea.worlds.PortalListener;
import fr.heavencraft.heavencrea.worlds.WorldsManager;
import fr.heavencraft.heavenguard.bukkit.BukkitHeavenGuard;

public class HeavenCrea extends HeavenPlugin implements DeprecatedHasUserProvider<CreativeUser>
{

	private DeprecatedUserProvider<CreativeUser> userProvider;
	private HpsManager hpsManager;

	@Override
	public void onEnable()
	{
		try
		{
			super.onEnable();
			saveDefaultConfig();

			final ConnectionProvider creaConnection = ConnectionHandlerFactory
					.getConnectionHandler(loadDatabase(getConfig(), "database"));
			final ConnectionProvider webConnection = ConnectionHandlerFactory.getConnectionHandler(Database.WEB);

			userProvider = new CreativeUserProvider(creaConnection);
			hpsManager = new HpsManager(webConnection);

			/*
			 * Commands and listeners from HeavenCrea (Bukkit code)
			 */

			// No Lags here
			new AntiLagListener(this);

			// HPs
			new HpsCommand(this);

			// Plots
			final BukkitHeavenGuard hGuard = loadHeavenGuard();
			new ParcelleCommand(this, hGuard);
			new PlotCommand(this, hGuard);
			new PlotSignListener(this, hGuard);
			new TalentSignListener(this, hGuard);

			// Users
			new JetonsCommand(this);
			new JetonsTask(this);
			new CreativeUserListener(this);

			// Homes
			new BuyhomeCommand(this);
			new HomeCommand(this);
			new SethomeCommand(this);
			new TphomeCommand(this);

			// Worlds
			new PortalListener(this);

			/*
			 * Commands and listener from HeavenCore (Bukkit code)
			 */

			new AccepterCommand(this);
			new RejoindreCommand(this);

			new TpCommand(this);
			new TphereCommand(this);
			new TpposCommand(this);

			new ClearWeatherListener(this);
			new ColoredSignsListener(this);
			new ForbiddenBlocksListener(this, Material.BARRIER);
			new JumpListener(this);
			new LinkSignListener(this);
			new NoChatListener(this);
			new RedstoneLampListener(this);

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

		new SpawnCommand(this, WorldsManager.getCreativeSpawnLocation());
		new WorldAccessListener(this, WorldsManager.getCreativeSpawnLocation(), WorldsManager.WORLD_TALENT,
				WorldsManager.WORLD_ARCHITECT);

		// TabColor
		new TabColorListener(this);
	}

	private static BukkitHeavenGuard loadHeavenGuard() throws HeavenException
	{
		final Plugin plugin = Bukkit.getPluginManager().getPlugin("HeavenGuard");

		if (plugin != null && plugin instanceof BukkitHeavenGuard)
		{
			return (BukkitHeavenGuard) plugin;
		}

		throw new HeavenException("Impossible to load HeavenGuard");
	}

	@Override
	public DeprecatedUserProvider<CreativeUser> getUserProvider()
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
		switch (worldName)
		{
			case WorldsManager.WORLD_CREATIVE:
				return new CreativeChunkGenerator();

			default:
				return super.getDefaultWorldGenerator(worldName, id);
		}
	}
}
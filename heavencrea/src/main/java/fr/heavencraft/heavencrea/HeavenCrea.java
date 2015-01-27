package fr.heavencraft.heavencrea;

import org.bukkit.Bukkit;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.Plugin;

import fr.heavencraft.heavencore.bukkit.HeavenPlugin;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.sql.ConnectionHandler;
import fr.heavencraft.heavencore.sql.ConnectionHandlerFactory;
import fr.heavencraft.heavencore.sql.Database;
import fr.heavencraft.heavencrea.bukkit.commands.JetonsCommand;
import fr.heavencraft.heavencrea.plots.PlotCommand;
import fr.heavencraft.heavencrea.plots.PlotSignListener;
import fr.heavencraft.heavencrea.users.UserListener;
import fr.heavencraft.heavencrea.users.UserProvider;
import fr.heavencraft.heavenguard.bukkit.HeavenGuard;

public class HeavenCrea extends HeavenPlugin
{
	private HeavenGuard regionPlugin;

	private ConnectionHandler connectionHandler;
	private UserProvider userProvider;

	@Override
	public void onEnable()
	{
		try
		{
			super.onEnable();

			regionPlugin = loadHeavenGuard();

			connectionHandler = ConnectionHandlerFactory.getConnectionHandler(Database.TEST);
			userProvider = new UserProvider(connectionHandler);

			/*
			 * Commands and listeners (Bukkit)
			 */

			new JetonsCommand(this);
			new UserListener(this);
			new PlotSignListener(this, regionPlugin);
			new PlotCommand(this, regionPlugin);
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

	@Override
	public ChunkGenerator getDefaultWorldGenerator(String worldName, String id)
	{
		log.info("getDefaultWorldGenerator %1$s", worldName);

		if ("world_creative".equals(worldName))
			return new CreativeChunkGenerator();

		else
			return super.getDefaultWorldGenerator(worldName, id);
	}
}
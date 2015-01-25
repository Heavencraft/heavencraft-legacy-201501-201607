package fr.heavencraft.heavencrea;

import org.bukkit.generator.ChunkGenerator;

import fr.heavencraft.heavencore.bukkit.HeavenPlugin;
import fr.heavencraft.heavencore.sql.ConnectionHandler;
import fr.heavencraft.heavencore.sql.ConnectionHandlerFactory;
import fr.heavencraft.heavencore.sql.Database;
import fr.heavencraft.heavencrea.bukkit.commands.JetonsCommand;
import fr.heavencraft.heavencrea.plots.PlotSignListener;
import fr.heavencraft.heavencrea.users.UserListener;
import fr.heavencraft.heavencrea.users.UserProvider;

public class HeavenCrea extends HeavenPlugin
{
	private static HeavenCrea instance;

	private ConnectionHandler connectionHandler;
	private UserProvider userProvider;

	@Override
	public void onEnable()
	{
		super.onEnable();

		instance = this;
		connectionHandler = ConnectionHandlerFactory.getConnectionHandler(Database.TEST);
		userProvider = new UserProvider(connectionHandler);

		/*
		 * Commands and listeners (Bukkit)
		 */

		new JetonsCommand(this);
		new UserListener(this);
		new PlotSignListener(this);
	}

	public static HeavenCrea getInstance()
	{
		return instance;
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
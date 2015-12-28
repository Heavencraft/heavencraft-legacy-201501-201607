package fr.heavencraft.heavenvip;

import org.bukkit.Bukkit;

import fr.heavencraft.heavencore.bukkit.HeavenPlugin;
import fr.heavencraft.heavencore.sql.ConnectionHandler;
import fr.heavencraft.heavencore.sql.ConnectionHandlerFactory;
import fr.heavencraft.heavencore.sql.Database;
import fr.heavencraft.heavencore.utils.menu.MenuListener;

public class HeavenVIP extends HeavenPlugin
{
	private static HeavenVIP _instance;
	private static ConnectionHandler mainConnection;
	
	
	@Override
	public void onEnable()
	{
		try
		{
			super.onEnable();
			_instance = this;
			mainConnection = ConnectionHandlerFactory.getConnectionHandler(Database.WEB);
			
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
		new HeavenVipCommand(getInstance());
		new MenuListener();
	}

	public static HeavenVIP getInstance()
	{
		return _instance;
	}

	public static ConnectionHandler getMainConnection()
	{
		return mainConnection;
	}

}
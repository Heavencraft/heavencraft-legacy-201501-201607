package fr.heavencraft.rpg;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

import fr.heavencraft.rpg.mobs.MobManager;

public class HeavenRPG extends JavaPlugin {
	private final static String RP_DB_URL = "jdbc:mysql://localhost:3306/minecraft-semirp?user=mc-sql&password=9e781e41f865901850d5c3060063c8ca&zeroDateTimeBehavior=convertToNull&?autoReconnect=true";
	private final static String MAIN_DB_URL = "jdbc:mysql://localhost:3306/mc-db?user=mc-sql&password=9e781e41f865901850d5c3060063c8ca&zeroDateTimeBehavior=convertToNull&?autoReconnect=true";
	private static WorldEditPlugin _WEP;
	private static WorldGuardPlugin _WGP;
	private static HeavenRPG _instance;

	private static Connection _connection;
	private static Connection _mainConnection;
	
	
	@Override
	public void onEnable()
	{
		super.onEnable();
		_instance = this;
		
		RPGFiles.getDungeons().options().copyDefaults(true);
		RPGFiles.getZones().options().copyDefaults(true);
		RPGFiles.saveAll();
		// Charger tout les listners 
		InitManager.init();
	}
	
	@Override
    public void onDisable() {
        
        MobManager.killAllMobs();
        
    }

	public static Connection getConnection()
	{
		try
		{
			if (_connection == null || _connection.isClosed())
			{
				_connection = DriverManager.getConnection(RP_DB_URL);
			}
		}
		catch (SQLException ex)
		{
			ex.printStackTrace();
			Bukkit.shutdown();
		}

		return _connection;
	}

	public static Connection getMainConnection()
	{
		try
		{
			if (_mainConnection == null || _mainConnection.isClosed())
			{
				_mainConnection = DriverManager.getConnection(MAIN_DB_URL);
			}
		}
		catch (SQLException ex)
		{
			ex.printStackTrace();
			Bukkit.shutdown();
		}

		return _mainConnection;
	}

	public static HeavenRPG getInstance()
	{
		return _instance;
	}

	public static WorldEditPlugin getWorldEdit()
	{

		if (_WEP == null)
		{
			Plugin plugin = getInstance().getServer().getPluginManager().getPlugin("WorldEdit");
			if (plugin == null || !(plugin instanceof WorldEditPlugin))
			{
				_WEP = null;
			}
			else
			{
				_WEP = (WorldEditPlugin) plugin;
			}
		}
		return _WEP;
	}
	
	public static WorldGuardPlugin getWorldGuard()
	{

		if (_WGP == null)
		{
			Plugin plugin = getInstance().getServer().getPluginManager().getPlugin("WorldGuard");
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

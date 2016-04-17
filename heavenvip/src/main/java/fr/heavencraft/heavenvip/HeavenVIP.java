package fr.heavencraft.heavenvip;

import org.bukkit.Bukkit;

import fr.heavencraft.heavencore.bukkit.HeavenPlugin;
import fr.heavencraft.heavencore.sql.ConnectionHandlerFactory;
import fr.heavencraft.heavencore.sql.ConnectionProvider;
import fr.heavencraft.heavencore.sql.Database;
import fr.heavencraft.heavencore.utils.menu.MenuListener;
import fr.heavencraft.heavenvip.movments.MovmentEffectListener;

public class HeavenVIP extends HeavenPlugin
{
	private static HeavenVIP _instance;
	private static ConnectionProvider proxyConnection;
	private static ConnectionProvider webConnection;

	@Override
	public void onEnable()
	{
		try
		{
			super.onEnable();
			_instance = this;
			proxyConnection = ConnectionHandlerFactory.getConnectionHandler(Database.PROXY);
			webConnection = ConnectionHandlerFactory.getConnectionHandler(Database.WEB);

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
		new MovmentEffectListener(this);
		
		/*
		 * Timer for effects
		 */
//		new BukkitRunnable(){
//			@Override
//			public void run()
//			{
//				for(Player p : Bukkit.getServer().getOnlinePlayers())
//				{
//					try
//					{
//						for(VipEffect eff : EffectProvider.getVipEffectByUser(p.getUniqueId().toString()))
//						{
//							eff.activate(EffectApplicationEventType.TICK_1S, p);
//						}
//					}
//					catch (HeavenException e)
//					{}
//				}
//			}
//			
//		}.runTaskTimer(getInstance(), 0, 20);
	}

	public static HeavenVIP getInstance()
	{
		return _instance;
	}

	public static ConnectionProvider getProxyConnection()
	{
		return proxyConnection;
	}
	
	public static ConnectionProvider getWebConnection()
	{
		return webConnection;
	}

}
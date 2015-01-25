package fr.heavencraft.heavencore.bukkit;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import fr.heavencraft.heavencore.logs.HeavenLog;
import fr.heavencraft.heavencore.providers.BukkitUniqueIdProvider;
import fr.heavencraft.heavencore.providers.UniqueIdProvider;
import fr.heavencraft.heavencore.sql.ConnectionHandlerFactory;
import fr.heavencraft.heavencore.sql.Database;

public abstract class HeavenPlugin extends JavaPlugin
{
	protected final HeavenLog log = HeavenLog.getLogger(getClass());
	protected final UniqueIdProvider uniqueIdProvider = new BukkitUniqueIdProvider(
			ConnectionHandlerFactory.getConnectionHandler(Database.PROXY));

	public UniqueIdProvider getUniqueIdProvider()
	{
		return uniqueIdProvider;
	}

	private static final String BEGIN = "{";
	private static final String END = "}";

	protected String textColor = ChatColor.GOLD.toString();
	protected String highlightColor = ChatColor.RED.toString();

	public void sendMessage(final CommandSender sender, final String format, final Object... args)
	{
		Bukkit.getScheduler().runTask(this, new Runnable()
		{
			@Override
			public void run()
			{
				sender.sendMessage(new StringBuilder(textColor).append(
						String.format(format.replace(BEGIN, highlightColor).replace(END, textColor), args)).toString());
			}
		});
	}
}
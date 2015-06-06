package fr.heavencraft.heavencore.bukkit.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;

import fr.heavencraft.heavencore.bukkit.HeavenPlugin;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.logs.HeavenLog;

public abstract class AbstractCommandExecutor implements CommandExecutor
{
	protected final HeavenLog log = HeavenLog.getLogger(getClass());
	protected final HeavenPlugin plugin;

	public AbstractCommandExecutor(HeavenPlugin plugin, String name)
	{
		this(plugin, name, "", new ArrayList<String>());
	}

	public AbstractCommandExecutor(HeavenPlugin plugin, String name, String permission)
	{
		this(plugin, name, permission, new ArrayList<String>());
	}

	public AbstractCommandExecutor(HeavenPlugin plugin, String name, List<String> aliases)
	{
		this(plugin, name, "", aliases);
	}

	public AbstractCommandExecutor(HeavenPlugin plugin, String name, String permission, List<String> aliases)
	{
		this.plugin = plugin;

		final PluginCommand command = plugin.getCommand(name);
		command.setExecutor(this);
		command.setPermission(permission);
		command.setPermissionMessage("");
		command.setAliases(aliases);

		log.info("Command %1$s registered", name);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		try
		{
			if (sender instanceof Player)
				onPlayerCommand((Player) sender, args);
			else
				onConsoleCommand(sender, args);
		}

		catch (final HeavenException ex)
		{
			plugin.sendMessage(sender, ex.getMessage());
		}

		return true;
	}

	protected abstract void onPlayerCommand(Player player, String[] args) throws HeavenException;

	protected abstract void onConsoleCommand(CommandSender sender, String[] args) throws HeavenException;

	protected abstract void sendUsage(CommandSender sender);
}
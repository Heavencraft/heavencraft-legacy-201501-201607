package fr.heavencraft.heavencore.bukkit;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import fr.heavencraft.heavencore.exceptions.HeavenException;

public abstract class AbstractBukkitCommandExecutor implements CommandExecutor
{
	public AbstractBukkitCommandExecutor(JavaPlugin plugin, String name)
	{
		this(plugin, name, "");
	}

	public AbstractBukkitCommandExecutor(JavaPlugin plugin, String name, String permission)
	{
		final PluginCommand command = plugin.getCommand(name);

		command.setExecutor(this);
		command.setPermission(permission);
		command.setPermissionMessage("");
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
			sender.sendMessage(ex.getMessage());
			// ChatUtil.sendMessage(sender, ex.getMessage());
		}

		return true;
	}

	protected abstract void onPlayerCommand(Player player, String[] args) throws HeavenException;

	protected abstract void onConsoleCommand(CommandSender sender, String[] args) throws HeavenException;

	protected abstract void sendUsage(CommandSender sender);
}
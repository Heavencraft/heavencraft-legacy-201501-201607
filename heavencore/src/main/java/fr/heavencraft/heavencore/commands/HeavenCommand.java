package fr.heavencraft.heavencore.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;

import fr.heavencraft.heavencore.exceptions.HeavenException;

public abstract class HeavenCommand implements CommandExecutor
{
	public HeavenCommand(String name)
	{
		this(name, "");
	}

	public HeavenCommand(String name, String permission)
	{
		final PluginCommand command = DevUtil.getPlugin().getCommand(name);

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
			ChatUtil.sendMessage(sender, ex.getMessage());
		}

		return true;
	}

	protected abstract void onPlayerCommand(Player player, String[] args) throws HeavenException;

	protected abstract void onConsoleCommand(CommandSender sender, String[] args) throws HeavenException;

	protected abstract void sendUsage(CommandSender sender);
}
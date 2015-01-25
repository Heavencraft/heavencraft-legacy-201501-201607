package fr.heavencraft.heavenguard.bukkit.commands;

import org.bukkit.command.CommandSender;

import fr.heavencraft.heavenguard.bukkit.HeavenGuard;

public abstract class AbstractSubCommand implements SubCommand
{
	protected final HeavenGuard plugin;
	private final String permission;

	protected AbstractSubCommand(HeavenGuard plugin, String permission)
	{
		this.plugin = plugin;
		this.permission = permission;
	}

	@Override
	public boolean canExecute(CommandSender sender, String regionName)
	{
		return sender.hasPermission(permission);
	}
}
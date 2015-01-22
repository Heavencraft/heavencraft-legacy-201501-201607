package fr.heavencraft.heavenguard.bukkit.commands;

import org.bukkit.command.CommandSender;

import fr.heavencraft.heavenguard.api.RegionProvider;

public abstract class AbstractSubCommand implements SubCommand
{
	protected final RegionProvider regionProvider;
	private final String permission;

	protected AbstractSubCommand(RegionProvider regionProvider, String permission)
	{
		this.regionProvider = regionProvider;
		this.permission = permission;
	}

	@Override
	public boolean canExecute(CommandSender sender, String regionName)
	{
		return sender.hasPermission(permission);
	}
}
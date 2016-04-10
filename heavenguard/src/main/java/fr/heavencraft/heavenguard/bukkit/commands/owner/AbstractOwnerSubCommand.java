package fr.heavencraft.heavenguard.bukkit.commands.owner;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavenguard.bukkit.BukkitHeavenGuard;
import fr.heavencraft.heavenguard.bukkit.commands.AbstractSubCommand;

abstract class AbstractOwnerSubCommand extends AbstractSubCommand
{
	protected AbstractOwnerSubCommand(BukkitHeavenGuard plugin, String permission)
	{
		super(plugin, permission);
	}

	@Override
	public boolean canExecute(CommandSender sender, String regionName)
	{
		if (super.canExecute(sender, regionName))
			return true;

		if (sender instanceof Player)
		{
			try
			{
				return plugin.getRegionProvider().getRegionByName(regionName).isMember(((Player) sender).getUniqueId(), true);
			}
			catch (final HeavenException ex)
			{
				return false;
			}
		}

		return false;
	}
}
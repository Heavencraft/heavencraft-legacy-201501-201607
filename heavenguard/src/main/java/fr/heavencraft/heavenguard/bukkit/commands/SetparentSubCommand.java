package fr.heavencraft.heavenguard.bukkit.commands;

import org.bukkit.command.CommandSender;

import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.utils.chat.ChatUtil;
import fr.heavencraft.heavenguard.api.HeavenGuardPermissions;
import fr.heavencraft.heavenguard.bukkit.BukkitHeavenGuard;

public class SetparentSubCommand extends AbstractSubCommand
{
	public SetparentSubCommand(BukkitHeavenGuard plugin)
	{
		super(plugin, HeavenGuardPermissions.SETPARENT_COMMAND);
	}

	@Override
	public void execute(CommandSender sender, String regionName, String[] args) throws HeavenException
	{
		switch (args.length)
		{
			case 0: // Remove parent
				setparent(sender, regionName, null);
				break;

			case 1: // Set parent
				setparent(sender, regionName, args[0]);
				break;

			default:
				sendUsage(sender);
				return;
		}
	}

	@Override
	public void sendUsage(CommandSender sender)
	{
		ChatUtil.sendMessage(sender, "/rg {setparent} <protection> <protection parente>");
	}

	private void setparent(CommandSender sender, String regionName, String parentName) throws HeavenException
	{
		plugin.getRegionProvider().getRegionByName(regionName).setParent(parentName);
		ChatUtil.sendMessage(sender, "La protection {%1$s} a désormais pour parent {%2$s}.", regionName,
				parentName);
	}
}
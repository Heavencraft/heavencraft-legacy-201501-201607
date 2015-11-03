package fr.heavencraft.heavenguard.bukkit.commands;

import org.bukkit.command.CommandSender;

import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.utils.chat.ChatUtil;
import fr.heavencraft.heavenguard.api.HeavenGuardPermissions;
import fr.heavencraft.heavenguard.bukkit.HeavenGuard;

public class RemoveSubCommand extends AbstractSubCommand
{
	public RemoveSubCommand(HeavenGuard plugin)
	{
		super(plugin, HeavenGuardPermissions.REMOVE_COMMAND);
	}

	@Override
	public void execute(CommandSender sender, String regionName, String[] args) throws HeavenException
	{
		if (args.length != 0)
		{
			sendUsage(sender);
			return;
		}

		plugin.getRegionProvider().deleteRegion(regionName);
		ChatUtil.sendMessage(sender, "La protection {%1$s} a bien été supprimée.", regionName);
	}

	@Override
	public void sendUsage(CommandSender sender)
	{
		ChatUtil.sendMessage(sender, "/rg {remove} <protection>");
	}
}
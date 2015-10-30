package fr.heavencraft.heavenrp.commands.warps;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.heavencraft.commands.HeavenCommand;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavenrp.RPPermissions;
import fr.heavencraft.heavenrp.warps.WarpsManager;
import fr.heavencraft.utils.ChatUtil;

public class WarpCommand extends HeavenCommand
{
	public WarpCommand()
	{
		super("warp", RPPermissions.WARP);
	}

	@Override
	protected void onPlayerCommand(Player player, String[] args) throws HeavenException
	{
		switch (args.length)
		{
			case 1:
				if (args[0].equalsIgnoreCase("list"))
				{
					List<String> warps = WarpsManager.listWarps();
					String list = "";

					for (String warp : warps)
						list = (list == "" ? "" : ", ") + "{" + warp + "}";

					ChatUtil.sendMessage(player, "Liste des warps :");
					ChatUtil.sendMessage(player, list);
				}
				break;
			case 2:
				if (args[0].equalsIgnoreCase("define"))
				{
					WarpsManager.createWarp(args[1], player.getLocation());
					ChatUtil.sendMessage(player, "Le warp {%1$s} a bien été défini.", args[1]);
				}
				else if (args[0].equalsIgnoreCase("remove"))
				{
					WarpsManager.getWarp(args[1]).remove();
					ChatUtil.sendMessage(player, "Le warp {%1$s} a bien été supprimé.", args[1]);
				}
				else if (args[0].equalsIgnoreCase("tp"))
				{
					player.teleport(WarpsManager.getWarp(args[1]).getLocation());
					ChatUtil.sendMessage(player, "Vous avez été téléporté à {%1$s}.", args[1]);
				}
				break;
			default:
				sendUsage(player);
				return;
		}
	}

	@Override
	protected void onConsoleCommand(CommandSender sender, String[] args) throws HeavenException
	{
		ChatUtil.sendMessage(sender, "Cette commande n'est pas utilisable depuis la console.");
	}

	@Override
	protected void sendUsage(CommandSender sender)
	{
		ChatUtil.sendMessage(sender, "/{warp} define <nom>");
		ChatUtil.sendMessage(sender, "/{warp} list");
		ChatUtil.sendMessage(sender, "/{warp} tp <nom>");
		ChatUtil.sendMessage(sender, "/{warp} remove <nom>");
	}
}
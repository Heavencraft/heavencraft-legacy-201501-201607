package fr.heavencraft.rpg.zones;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.bukkit.selections.CuboidSelection;
import com.sk89q.worldedit.bukkit.selections.Selection;

import fr.heavencraft.Utils.ChatUtil;
import fr.heavencraft.exceptions.HeavenException;
import fr.heavencraft.rpg.HeavenCommand;
import fr.heavencraft.rpg.HeavenRPG;
import fr.heavencraft.rpg.RPGFiles;
import fr.heavencraft.rpg.RPGpermissions;

public class ZoneCommand extends HeavenCommand
{
	private final static String NO_SELECTION = "Vous devez d'abord faire une selection avec World Edit.";
	private final static String SELECTION_SET = "La zone a été ajoutée.";
	private final static String NO_PERMISSION = "Vous n'avez pas la permission";
	private final static String FOUND_X_ZONES = "Vous ètes dans: {%1$s} zones:";
	private final static String ZONE_INFO = "{%1$s} \n Niveau: {%2$s}";
	private final static String IN_NO_ZONE = "Aucune zone ici.";
	private final static String ZONE_DELTED = "La zone {%1$s} a été supprimée.";
	private final static String ZONE_INVALID = "La zone {%1$s} n'existe pas!";

	public ZoneCommand()
	{
		super("zone");
	}

	@Override
	protected void onPlayerCommand(Player player, String[] args) throws HeavenException
	{
		if (!player.hasPermission(RPGpermissions.ZONES))
		{
			ChatUtil.sendMessage(player, NO_PERMISSION);
			return;
		}

		if (args.length == 0)
		{
			ArrayList<Zone> foundZones = ZoneManager.getZones(player.getLocation());
			if (foundZones.size() == 0)
				ChatUtil.sendMessage(player, IN_NO_ZONE);
			else
			{
				ChatUtil.sendMessage(player, FOUND_X_ZONES, foundZones.size());
				for (Zone z : foundZones)
					ChatUtil.sendMessage(player, ChatColor.DARK_GREEN + "- " + z.get_name() + ChatColor.GOLD
							+ " | lvl: " + z.get_zoneLevel());
			}
		}
		else
		{
			if (args[0].equalsIgnoreCase("create"))
			{
				if (args.length != 3)
					sendUsage(player);
				else
				{
					Selection s = HeavenRPG.getWorldEdit().getSelection(player);
					if (s == null)
						ChatUtil.sendMessage(player, NO_SELECTION);
					else
					{
						ZoneManager.createZone(args[1], Integer.parseInt(args[2]),
								new CuboidSelection(s.getWorld(), s.getMinimumPoint(), s.getMaximumPoint()));
						RPGFiles.saveZones();
						ChatUtil.sendMessage(player, SELECTION_SET);
					}
				}
				return;
			}
			else if (args[0].equalsIgnoreCase("remove"))
			{
				if (args.length != 2)
					sendUsage(player);
				else
				{
					if (ZoneManager.getZoneByName(args[1]) != null)
					{
						ZoneManager.removeZone(ZoneManager.getZoneByName(args[1]));
						ChatUtil.sendMessage(player, ZONE_DELTED, args[1]);
						return;
					}
					ChatUtil.sendMessage(player, ZONE_INVALID, args[1]);
				}
			}
			else if (ZoneManager.getZoneByName(args[0]) != null)
				ChatUtil.sendMessage(player, ZONE_INFO, ZoneManager.getZoneByName(args[0]).getName(),
						ZoneManager.getZoneByName(args[0]).getZoneLevel() + "");
			else
				sendUsage(player);
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
		ChatUtil.sendMessage(sender, "/{zone} | Affiche la zone dans laquelle vous vous trouvez actuellement.");
		ChatUtil.sendMessage(sender, "/{zone} <nom de zone unique> | Affiche des informations sur les zones.");
		ChatUtil.sendMessage(sender, "/{zone} create <nom de la zone> <niveau> | Crée une nouvelle zone.");
		ChatUtil.sendMessage(sender, "/{zone} remove <nom de la zone> | Supprime une zone.");
	}

}

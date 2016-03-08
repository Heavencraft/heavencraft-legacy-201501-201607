package fr.heavencraft.heavenrp.dungeon;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.heavencraft.heavencore.bukkit.commands.AbstractCommandExecutor;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.utils.DevUtil;
import fr.heavencraft.heavencore.utils.chat.ChatUtil;
import fr.heavencraft.heavenrp.HeavenRP;

public class DungeonCommand extends AbstractCommandExecutor
{
	// Command usage
	private final static String ADMIN_LIST = "/donjon {admin} list : " + ChatColor.WHITE + "Liste les donjons.";
	private final static String ADMIN_INFO = "/donjon {admin} info <donjon> : " + ChatColor.WHITE
			+ "Donne des informations détailés sur un donjon.";
	private final static String ADMIN_CREATE = "/donjon {admin} create <nom> <joueurs requis> <outX> <outY> <outZ> <outYaw> <outPitch> : "
			+ ChatColor.WHITE + "Crée un nouvau donjon. (Spawn = Votre position)";
	private final static String ADMIN_DELETE = "/donjon {admin} delete <donjon> : " + ChatColor.WHITE
			+ "Supprime un dojon.";
	private final static String ADMIN_ADDROOM = "/donjon {admin} addroom <donjon> <minX> <minY> <minZ> <maxX> <maxY> <maxZ> <trigX> <trigY> <trigZ> : "
			+ ChatColor.WHITE + "Crée une nouvelle salle. (Spawn = Votre position)";
	private final static String ADMIN_DELROOM = "/donjon {admin} delroom <idSalle> : " + ChatColor.WHITE
			+ "Supprime une salle.";

	public DungeonCommand(HeavenRP plugin)
	{
		super(plugin, "djn");
	}

	@Override
	protected void onPlayerCommand(final Player player, final String[] args) throws HeavenException
	{
		// Check if no parameter is given.

		// Do we access admin commands?
		if (args.length < 2 || !args[0].equalsIgnoreCase("admin"))
		{
			sendUsage(player);
			return;
		}

		// LIST DUNGEONS
		if (args[1].equalsIgnoreCase("list"))
		{
			DungeonManager.PrintDungeonList(player);
			return;
		}

		// DUNGEON INFO
		if (args[1].equalsIgnoreCase("info"))
		{
			if (args.length != 3)
				throw new HeavenException("/donjon admin info <nom>");
			DungeonManager.PrintDungeonInfo(player, args[2]);
			return;
		}

		// CREATE DUNGEON
		if (args[1].equalsIgnoreCase("create"))
		{
			if (args.length != 9)
			{
				throw new HeavenException(ADMIN_CREATE);
			}
			final String dgName = args[2];
			final int necessaryPlayerAmount = DevUtil.toUint(args[3]);

			final Location out = new Location(player.getWorld(), Float.parseFloat(args[4]),
					Float.parseFloat(args[5]), Float.parseFloat(args[6]), Float.parseFloat(args[7]),
					Float.parseFloat(args[8]));

			DungeonManager.CreateDungeon(player, dgName, necessaryPlayerAmount, player.getLocation(), out);
			return;
		}

		// DELETE DUNGEON
		if (args[1].equalsIgnoreCase("delete"))
		{
			if (args.length != 3)
				throw new HeavenException(ADMIN_DELETE);
			final String dgName = args[2];
			DungeonManager.DeleteDungeon(player, dgName);
			return;
		}

		// DUNGEON ADD ROOM
		if (args[1].equalsIgnoreCase("addroom"))
		{
			if (args.length != 12)
				throw new HeavenException(ADMIN_ADDROOM);
			// Get spawn
			Location spawn = player.getLocation();
			// Build corners
			Location corner1 = new Location(player.getWorld(), DevUtil.toInt(args[3]), DevUtil.toInt(args[4]),
					DevUtil.toInt(args[5]));
			Location corner2 = new Location(player.getWorld(), DevUtil.toInt(args[6]), DevUtil.toInt(args[7]),
					DevUtil.toInt(args[8]));
			// Build trigger
			Location trigger = new Location(player.getWorld(), DevUtil.toInt(args[9]), DevUtil.toInt(args[10]),
					DevUtil.toInt(args[11]));

			DungeonManager.AddDungeonRoom(player, args[2], spawn, trigger, corner1, corner2);
			return;
		}

		// DUNGEON REMOVE ROOM
		if (args[1].equalsIgnoreCase("delroom"))
		{
			if (args.length != 3)
				throw new HeavenException(ADMIN_DELROOM);

			DungeonManager.RemoveDungeonRoom(player, DevUtil.toUint(args[2]));
			return;
		}

		sendUsage(player);
		return;
	}

	@Override
	protected void onConsoleCommand(CommandSender sender, String[] args) throws HeavenException
	{
		throw new HeavenException("Uniquement utilisable depuis le jeu.");
	}

	@Override
	protected void sendUsage(CommandSender sender)
	{
		ChatUtil.sendMessage(sender, ADMIN_LIST);
		ChatUtil.sendMessage(sender, ADMIN_INFO);
		ChatUtil.sendMessage(sender, ADMIN_CREATE);
		ChatUtil.sendMessage(sender, ADMIN_DELETE);
		ChatUtil.sendMessage(sender, ADMIN_ADDROOM);
		ChatUtil.sendMessage(sender, ADMIN_DELROOM);
	}

}

package fr.heavencraft.rpg.donjon;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.bukkit.selections.Selection;

import fr.heavencraft.Utils.ChatUtil;
import fr.heavencraft.exceptions.HeavenException;
import fr.heavencraft.rpg.HeavenCommand;
import fr.heavencraft.rpg.HeavenRPG;
import fr.heavencraft.rpg.RPGpermissions;
import fr.heavencraft.rpg.donjon.Dungeon.DungeonRoom;

public class DungeonCommand extends HeavenCommand{
	private final static String DUNGEON_ALREADY_EXIST = "Ce donjon existe déjà!";
	private final static String DUNGEON_DOES_NOT_EXIST = "Ce donjon n'existe pas!";
	private final static String NO_PERMISSION = "Vous n'avez pas la permission";
	private final static String DUNGEON_CREATED = "Donjon crée avec succes!";
	private final static String DUNGEON_DELETED = "Donjon supprimé avec succes!";
	private final static String DUNGEON_LOBBY_SET = "Lobby du donjon définie!";
	private final static String DUNGEON_ROOM_CREATED = "Salle de donjon n{%1$s} crée avec succes!";
	private final static String DUNGEON_ROOM_DELETED = "Salle de donjon n{%1$s} supprimé avec succes!";
	private final static String DUNGEON_ROOM_UPDATED = "Salle de donjon a été mise a jour!";
	private final static String DUNGEON_ROOM_TRIGGER_DEFINED = "Le trigger de la salle a été mise a jour!";
	private final static String NO_SELECTION = "Vous devez d'abord faire une selection avec World Edit.";
	private final static String DEBUG_MODE_ACTIVE = "Le mode débug est activé.";
	private final static String DEBUG_MODE_INACTIVE = "Le mode débug est désactivé.";
	private final static String KICKED_PLAYER = "Le joueur a été renvoyé du donjon.";
	private final static String DUNGEON_LEAVED = "Vous avez quitté le donjon.";
	private final static String DUNGEON_CANNOT_LEAVE_WHILE_RUNNING = "Vous ne pouvez pas quitter tant que le donjon est en cours.";

	
	public DungeonCommand()
	{
		super("donjon");
	}

	@Override
	protected void onPlayerCommand(Player player, String[] args) throws HeavenException {

		if(args.length == 0)
		{
			sendUsage(player);
			return;
		}
		
		if(args[0].equalsIgnoreCase("quitter") || args[0].equalsIgnoreCase("q")) {
			Dungeon dg = DungeonManager.getDungeonByUser(player);
			if(dg == null)
				return;
			if(dg.is_Running()) {
				ChatUtil.sendMessage(player,  DUNGEON_CANNOT_LEAVE_WHILE_RUNNING);
			}
			else
			{
				dg.handlePlayerDisconnect(player);
				ChatUtil.sendMessage(player,  DUNGEON_LEAVED);
			}
				
			
			return;
		}
		
		if (!player.hasPermission(RPGpermissions.DONJON_ADMIN))
		{
			ChatUtil.sendMessage(player, NO_PERMISSION);
			return;
		}
		
		if(args[0].equalsIgnoreCase("list"))
		{
			ChatUtil.sendMessage(player, "Donjons: ");
			for(Dungeon dg : DungeonManager.get_dungeons())
			{
				ChatUtil.sendMessage(player, " - " + dg.get_name());
			}	
			return;
		}
		else if(args[0].equalsIgnoreCase("debug")) 
		{
			if(DungeonManager.is_debug()) {
				DungeonManager.set_debug(false);
				ChatUtil.sendMessage(player, DEBUG_MODE_INACTIVE);
			}else{
				DungeonManager.set_debug(true);
				ChatUtil.sendMessage(player, DEBUG_MODE_ACTIVE);
			}
			return;
		}
		else if(args[0].equalsIgnoreCase("kick"))
		{
			if(args.length < 2)
				return;
			Player pl = HeavenRPG.getInstance().getServer().getPlayer(args[1]);
			if(pl == null)
				return;
			Dungeon dg = DungeonManager.getDungeonByUser(pl);
			if(dg == null)
				return;
			dg.handlePlayerDisconnect(pl);
			ChatUtil.sendMessage(player, KICKED_PLAYER);
			return;
		}
		else if(args[0].equalsIgnoreCase("create"))
		{
			if(args.length!= 3)
			{
				sendUsage(player);
				return;
			}			
			//Vérifier que le donjon n'exite pas déjà
			if(DungeonManager.getDungeonByName(args[1]) != null)
			{
				ChatUtil.sendMessage(player, DUNGEON_ALREADY_EXIST);
				return;
			}

			//Créer l'instance
			DungeonManager.createDungeon(args[1], Integer.parseInt(args[2]));
			ChatUtil.sendMessage(player, DUNGEON_CREATED);
			return;
		}
		else if(args[0].equalsIgnoreCase("delete"))
		{
			if(args.length!= 2)
			{
				sendUsage(player);
				return;
			}

			Dungeon dg = DungeonManager.getDungeonByName(args[1]);
			if(dg == null)
			{
				ChatUtil.sendMessage(player, DUNGEON_DOES_NOT_EXIST);
				return;
			}
			DungeonManager.deleteDungeon(dg);
			ChatUtil.sendMessage(player, DUNGEON_DELETED);
			return;
		}

		if(DungeonManager.getDungeonByName(args[0]) == null)
		{
			ChatUtil.sendMessage(player, DUNGEON_DOES_NOT_EXIST);
			return;
		}

		Dungeon dg = DungeonManager.getDungeonByName(args[0]);
		if(args.length == 1)
		{
			// Envoyer l'état du donjon
			ChatUtil.sendMessage(player, "~~ " + dg.get_name());
			if(dg.is_Running())
				ChatUtil.sendMessage(player, "     │ " + ChatColor.DARK_AQUA + "Running: " + ChatColor.GREEN + "true" );
			else
				ChatUtil.sendMessage(player, "     │ " + ChatColor.DARK_AQUA + "Running: " + ChatColor.RED + "false" );
			ChatUtil.sendMessage(player, "     │ " + ChatColor.DARK_AQUA + "Player: " + ChatColor.GREEN + dg.get_requiredPlayerAmmount() );
			ChatUtil.sendMessage(player, "     │ " + ChatColor.DARK_AQUA + "Actual Room: " + ChatColor.GREEN + dg.getActualRoom() + ChatColor.RED + " / " + dg.get_rooms().size());
			ChatUtil.sendMessage(player, "     │ " + ChatColor.DARK_AQUA + "Tot.Rooms: " + ChatColor.GREEN + dg.get_rooms().size());

			for(DungeonRoom dgr : dg.get_rooms())
			{
				ChatUtil.sendMessage(player, "     ├ " + dgr.get_index() + " | mobs: " + dgr.get_mobs().size());
			}

			return;
		}

		if(args[1].equalsIgnoreCase("lobby"))
		{
			DungeonManager.setLobby(dg, player.getLocation());
			ChatUtil.sendMessage(player, DUNGEON_LOBBY_SET);
			return;
		}
		else if(args[1].equalsIgnoreCase("room"))
		{
			DungeonRoom dgr = dg.getDungeonRoomByIndex(Integer.parseInt(args[2]));

			//La salle n'existe pas, la créer
			if(dgr == null)
			{
				Selection s = HeavenRPG.getWorldEdit().getSelection(player);
				if(s == null)
				{
					ChatUtil.sendMessage(player, NO_SELECTION);
					return;
				}

				DungeonManager.createDungeonRoom(dg, Integer.parseInt(args[2]), player.getLocation(),s);
				ChatUtil.sendMessage(player, DUNGEON_ROOM_CREATED, args[2]);
				return;
			}
			if(args.length != 4)
			{
				sendUsage(player);
				return;
			}

			if(args[3].equalsIgnoreCase("delete"))
			{
				//supprimer le dgr
				DungeonManager.deleteDungeonRoom(dg, dgr);
				ChatUtil.sendMessage(player, DUNGEON_ROOM_DELETED, args[2]);
				return;
			}
			else if(args[3].equalsIgnoreCase("spawn"))
			{
				//Mettre a jour le spawn du room
				DungeonManager.updateDungeonRoomSpawn(dg, dgr, player.getLocation());
				ChatUtil.sendMessage(player, DUNGEON_ROOM_UPDATED, args[2]);
				return;
			}
			else if(args[3].equalsIgnoreCase("trigger"))
			{
				// Trigger
				Location l = player.getLocation();
				l.setY(l.getY() -1);
				DungeonManager.updateDungeonRoomTrigger(dg, dgr, l);
				ChatUtil.sendMessage(player, DUNGEON_ROOM_TRIGGER_DEFINED);
			}
			else
			{
				sendUsage(player);
				return;
			}
		}

	}

	@Override
	protected void onConsoleCommand(CommandSender sender, String[] args) throws HeavenException {	
		if(args.length == 0)
			return;

		if(args[0].equalsIgnoreCase("kick"))
		{
			Player pl = HeavenRPG.getInstance().getServer().getPlayer(args[1]);
			if(pl == null)
				return;
			Dungeon dg = DungeonManager.getDungeonByUser(pl);
			if(dg == null)
				return;
			if(dg.get_requiredPlayerAmmount() != 0)
				return;
			dg.handlePlayerDisconnect(pl);
			ChatUtil.sendMessage(sender, KICKED_PLAYER);
			return;
		}
		
		ChatUtil.sendMessage(sender, "Cette commande n'est pas utilisable depuis la console.");
	}

	@Override
	protected void sendUsage(CommandSender sender) {
		ChatUtil.sendMessage(sender, "/{donjon} quitter | Fait quitter le donjon.");
		if (!sender.hasPermission(RPGpermissions.DONJON_ADMIN))
			return;	
		ChatUtil.sendMessage(sender, "/{donjon} create <nom donjon> <joueurs> | Crée un nouveau donjon et définie le nombre de joueurs requis.");
		ChatUtil.sendMessage(sender, "/{donjon} delete <nom donjon> | Supprime le donjon.");
		ChatUtil.sendMessage(sender, "/{donjon} list | Affiche une liste des donjons.");
		ChatUtil.sendMessage(sender, "/{donjon} debug | Active le mode débug.");
		ChatUtil.sendMessage(sender, "/{donjon} kick <joueur> | Force un joueur a quitter le donjon.");
		ChatUtil.sendMessage(sender, "/{donjon} <nom donjon> lobby | Définie le spawn de la lobby.");
		ChatUtil.sendMessage(sender, "/{donjon} <nom donjon> room <index 0-99> | Définie le spawn de la salle séléctionée au WE.");
		ChatUtil.sendMessage(sender, "/{donjon} <nom donjon> room <index 0-99> delete | Supprime la salle.");
		ChatUtil.sendMessage(sender, "/{donjon} <nom donjon> room <index 0-99> spawn | Modifie le spawn de la salle.");
		ChatUtil.sendMessage(sender, "/{donjon} <nom donjon> room <index 0-99> trigger | Définie le bloc en dessous comme le bloc trigger.");
	}
}

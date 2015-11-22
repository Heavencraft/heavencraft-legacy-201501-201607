package fr.heavencraft.heavenrp.commands.dungeon;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.heavencraft.heavencore.bukkit.commands.AbstractCommandExecutor;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.utils.chat.ChatUtil;
import fr.heavencraft.heavenrp.HeavenRP;
import fr.heavencraft.heavenrp.RPPermissions;

public class DungeonCommand extends AbstractCommandExecutor
{
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

	public DungeonCommand(HeavenRP plugin)
	{
		super(plugin, "adonjon", RPPermissions.DUNGEON_ADMIN);
	}
	
	@Override
	protected void onPlayerCommand(Player player, String[] args) throws HeavenException
	{
		// Check if no parameter is given.
		if(args.length == 0)
		{
			sendUsage(player);
			return;
		}
		// LIST DUNGEONS
		if(args[0].equalsIgnoreCase("list"))
		{
			ChatUtil.sendMessage(player, "Donjons: ");
			return;
		}
		
		//TODO Commandes de creation des donjons
	}

	@Override
	protected void onConsoleCommand(CommandSender sender, String[] args) throws HeavenException
	{	
	}

	@Override
	protected void sendUsage(CommandSender sender)
	{	
		ChatUtil.sendMessage(sender, "/{adonjon} list : Liste & etats des donjons.");
	}

}

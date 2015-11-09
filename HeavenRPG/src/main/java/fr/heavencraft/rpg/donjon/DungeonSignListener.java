package fr.heavencraft.rpg.donjon;

import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.block.SignChangeEvent;

import fr.heavencraft.Utils.ChatUtil;
import fr.heavencraft.exceptions.HeavenException;
import fr.heavencraft.rpg.RPGpermissions;
import fr.heavencraft.rpg.SignListener;

public class DungeonSignListener extends SignListener {
	private final static String DUNGEON_DOES_NOT_EXIST = "Ce donjon n'existe pas!";
	private final static String DUNGEON_DOES_NOT_HAVE_THIS_ROOM = "Cette salle c'est effondrée...!";
	private final static String YOU_ARE_NOT_IN_DUNGEON = "Vous n'ètes dans aucun donjon!";

	public DungeonSignListener()
	{
		super("Donjon", RPGpermissions.DONJON_SIGN);
	}

	@Override
	protected boolean onSignPlace(Player player, SignChangeEvent event) throws HeavenException {
		return true;
	}

	@Override
	protected void onSignClick(Player player, Sign sign) throws HeavenException {

		if(sign.getLine(1).equalsIgnoreCase("suivant"))
		{
			Dungeon dg = DungeonManager.getDungeonByUser(player);
			if(dg == null)
			{
				ChatUtil.sendMessage(player, YOU_ARE_NOT_IN_DUNGEON);
				return;
			}
			
			int idx = Integer.parseInt(sign.getLine(2));
			
			if(!dg.hasRoomWithIndex(idx))
			{
				ChatUtil.sendMessage(player, DUNGEON_DOES_NOT_HAVE_THIS_ROOM);
				return;
			}
			
			dg.handleChangeRoomAttemp(player, idx);	
			return;
		}
		else if(sign.getLine(1).equalsIgnoreCase("fin"))
		{
			Dungeon dg = DungeonManager.getDungeonByUser(player);
			if(dg == null)
			{
				ChatUtil.sendMessage(player, YOU_ARE_NOT_IN_DUNGEON);
				return;
			}
			dg.handleEndDungeonAttemp(player);
			return;
		}
		else
		{

			// Faire entrer le joueur dans le donjon écrit dans la ligne 1
			Dungeon dg = DungeonManager.getDungeonByName(sign.getLine(1));

			if(dg == null)
			{
				ChatUtil.sendMessage(player, DUNGEON_DOES_NOT_EXIST);
				return;
			}			
			dg.handleJoinAttemp(player);
			return;
		}
	}
}

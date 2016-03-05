package fr.heavencraft.heavenrp.dungeon;

import org.bukkit.ChatColor;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.block.SignChangeEvent;

import fr.heavencraft.heavencore.bukkit.listeners.AbstractSignListener;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.utils.DevUtil;
import fr.heavencraft.heavencore.utils.chat.ChatUtil;
import fr.heavencraft.heavenrp.HeavenRP;
import fr.heavencraft.heavenrp.RPPermissions;
import fr.heavencraft.heavenrp.database.users.UserProvider;

public class DungeonSignListener extends AbstractSignListener
{
	private static final String NOT_ENOUGH_ARGUMENTS = "Il manque des arguments a ce panneau.";
	
	public DungeonSignListener(HeavenRP plugin)
	{
		super(plugin, "Donjon", RPPermissions.DUNGEON_ADMIN);
	}
	
	public enum DungeonSignType {
		JOIN("Join", ChatColor.BLUE + "Entrer"),
		NEXT("Next", ChatColor.BLUE + "Continuer"),
		EXIT("Exit", ChatColor.BLUE + "Vers la sortie"),
		LEAVE("Quitter", ChatColor.BLUE + "Quitter");
		
		private String signText = "";
		private String signDisplayText = "";
		
		DungeonSignType(String text, String displayed){
			this.signText = text;
			this.signDisplayText = displayed;
		}
		
		public String toString(){
			return this.signText;
		}
		public String getDisplay(){
			return this.signDisplayText;
		}
	}

	@Override
	protected boolean onSignPlace(Player player, SignChangeEvent event) throws HeavenException
	{
		final String line = event.getLine(1);
		
		// ## Signs without arguments
		if(line.equalsIgnoreCase(DungeonSignType.EXIT.toString())) {
			event.setLine(1, DungeonSignType.EXIT.getDisplay());
			return true;
		}
		
		if(line.equalsIgnoreCase(DungeonSignType.LEAVE.toString())) {
			event.setLine(1, DungeonSignType.LEAVE.getDisplay());
			return true;
		}
		
		// ## Signs with argument
		final String argument = event.getLine(2);
		// Has an argument been passed?
		if(argument == null || argument.length() <= 0){
			ChatUtil.sendMessage(player, NOT_ENOUGH_ARGUMENTS);
			return false;
		}
		
		// Join sign
		/* [Donjon]
		 * JOIN
		 * <dungeonName>
		 */
		if(line.equalsIgnoreCase(DungeonSignType.JOIN.toString())) {
			event.setLine(1, DungeonSignType.JOIN.getDisplay());
			return true;
		}
		// Next sign
		/* [Donjon]
		 * NEXT
		 * <dungeonRoomId>
		 */
		if(line.equalsIgnoreCase(DungeonSignType.NEXT.toString())) {
			if(DevUtil.toUint(argument) >= 0){
				event.setLine(1, DungeonSignType.NEXT.getDisplay());
				return true;
			}
		}
		
		return false;
	}

	@Override
	protected void onSignClick(Player player, Sign sign) throws HeavenException
	{
		final String line = sign.getLine(1);
		
		if(line.equals(DungeonSignType.NEXT.getDisplay())) {
			
			return;
		}
		if(line.equals(DungeonSignType.EXIT.getDisplay())) {
			
			return;
		}
		if(line.equals(DungeonSignType.JOIN.getDisplay())) {
			DungeonManager.PlayerJoin(player);
			return;
		}
		if(line.equals(DungeonSignType.LEAVE.getDisplay())) {
			
			return;
		}
	}

}

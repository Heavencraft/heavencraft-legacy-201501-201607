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

public class DungeonSignListener extends AbstractSignListener
{
	private static final String NOT_ENOUGH_ARGUMENTS = "Il manque des arguments a ce panneau.";
	private static final String WRONG_OPERATOR = "L'opérateur est érroné.";

	public DungeonSignListener(HeavenRP plugin)
	{
		super(plugin, "Donjon", RPPermissions.DUNGEON_ADMIN);
	}

	public enum DungeonSignType
	{
		JOIN("Join", ChatColor.BLUE + "Entrer"),
		NEXT("Next", ChatColor.BLUE + "Continuer"),
		LEAVE("Leave", ChatColor.BLUE + "Quitter"),
		END("End", ChatColor.BLUE + "Sortie");

		private String signText = "";
		private String signDisplayText = "";

		DungeonSignType(String text, String displayed)
		{
			this.signText = text;
			this.signDisplayText = displayed;
		}

		public String toString()
		{
			return this.signText;
		}

		public String getDisplay()
		{
			return this.signDisplayText;
		}
	}

	@Override
	protected boolean onSignPlace(Player player, SignChangeEvent event) throws HeavenException
	{
		final String operand = event.getLine(1);

		// ## Signs without arguments
		if (operand.equalsIgnoreCase(DungeonSignType.LEAVE.toString()))
		{
			event.setLine(1, DungeonSignType.LEAVE.getDisplay());
			return true;
		}

		if (operand.equalsIgnoreCase(DungeonSignType.END.toString()))
		{
			event.setLine(1, DungeonSignType.END.getDisplay());
			return true;
		}

		// ## Signs with argument
		final String argument = event.getLine(2);
		// Has an argument been passed?
		if (argument == null || argument.length() <= 0)
		{
			ChatUtil.sendMessage(player, NOT_ENOUGH_ARGUMENTS);
			return false;
		}

		// Join sign
		/*
		 * [Donjon] JOIN <dungeonName>
		 */
		if (operand.equalsIgnoreCase(DungeonSignType.JOIN.toString()))
		{
			event.setLine(1, DungeonSignType.JOIN.getDisplay());
			return true;
		}
		// Next sign
		/*
		 * [Donjon] NEXT <dungeonRoomId>
		 */
		if (operand.equalsIgnoreCase(DungeonSignType.NEXT.toString()))
		{
			if (DevUtil.toUint(argument) >= 0)
			{
				event.setLine(1, DungeonSignType.NEXT.getDisplay());
				return true;
			}
		}
		ChatUtil.sendMessage(player, WRONG_OPERATOR);
		return false;
	}

	@Override
	protected void onSignClick(Player player, Sign sign) throws HeavenException
	{
		final String line = sign.getLine(1);

		if (line.equals(DungeonSignType.NEXT.getDisplay()))
		{
			DungeonManager.AttempNextRoom(player, DevUtil.toUint(sign.getLine(2)));
			return;
		}
		if (line.equals(DungeonSignType.JOIN.getDisplay()))
		{
			DungeonManager.PlayerJoin(player, sign.getLine(2));
			return;
		}
		if (line.equals(DungeonSignType.LEAVE.getDisplay()))
		{
			DungeonManager.PlayerLeave(player, false);
			return;
		}
		if (line.equals(DungeonSignType.END.getDisplay()))
		{
			DungeonManager.AttempEndDungeon(player);
			return;
		}
	}

}

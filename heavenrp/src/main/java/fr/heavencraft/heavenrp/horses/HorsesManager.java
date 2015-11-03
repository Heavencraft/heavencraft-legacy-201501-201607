package fr.heavencraft.heavenrp.horses;

import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;

import fr.heavencraft.heavencore.utils.chat.ChatUtil;
import fr.heavencraft.heavenrp.RPPermissions;

public class HorsesManager
{
	public static boolean isWild(Horse horse)
	{
		return !horse.isTamed();
	}

	public static boolean canUse(Horse horse, Player player)
	{
		if (player.hasPermission(RPPermissions.HORSE_BYPASS))
			return true;
		else if (isWild(horse))
			return true;
		else if (horse.getOwner() == null)
			return false;
		else
			return horse.getOwner().getUniqueId().equals(player.getUniqueId());
	}

	public static void sendWarning(Horse horse, Player player)
	{
		if (horse.getOwner() == null)
			ChatUtil.sendMessage(player,
					"Ce cheval est apprivoisé mais n'a pas de propriétaire. Merci de contacter un administrateur.");
		else
			ChatUtil.sendMessage(player, "Ce cheval appartient à {%1$s}.", horse.getOwner().getName());
	}
}
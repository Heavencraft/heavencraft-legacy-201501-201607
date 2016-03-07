package fr.heavencraft.heavenrp.dungeon;

import org.bukkit.entity.Player;

import fr.heavencraft.heavencore.utils.chat.ChatUtil;

public class DungeonUtils
{
	/**
	 * Displays a list of available dungeons and their state
	 * @param p
	 */
	public static void ListDungeons(final Player p) {
		ChatUtil.sendMessage(p, "###### Donjons ######");
		int i = 1; // Counter
		for(DungeonManager.Dungeon dg : DungeonManager.Dungeons.values()){
			char linePrefix = (i >= DungeonManager.Dungeons.values().size()) ? '└' : '├';
			ChatUtil.sendMessage(p, "%1$c ({%2$d}) %3$s | Joueurs: {%4$d}/{%5$d}",
					linePrefix, dg.getId(), dg.getName(), dg.PlayerCount, dg.getRequiredPlayer());
			++i;
		}
	}
}

package fr.hc.cinematics.components;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class CinematicsHelper
{

	private final static Map<String, String> cinematicsText = new HashMap<String, String>();
	private final static String MAP_SPLIT = "_";
	private final String HEADING = "&7EVT &c[";
	private final String SPLIT = "&c]&7 --> ";

	public CinematicsHelper()
	{
		// Cinematic_Timer, text.
		addFormatedEntry("0" + "0" + MAP_SPLIT + "51", "&cMythridor", "Bon je t'ai réanimé Connard !");
		addFormatedEntry("0" + MAP_SPLIT + "55", "&cInfirmière",
				"Hé ! Réveillez vous, le général vous attend au front !");
	}

	public static void NPCSpeak(Player player, Integer timer, Integer cinematic)
	{
		String index = cinematic.toString() + MAP_SPLIT + timer.toString();
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', cinematicsText.get(index)));
	}

	private void addFormatedEntry(final String index, final String name, final String text)
	{
		cinematicsText.put(index, HEADING + name + SPLIT + text);
	}

}

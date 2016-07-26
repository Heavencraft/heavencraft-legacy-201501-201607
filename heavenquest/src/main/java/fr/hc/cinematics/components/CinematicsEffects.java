package fr.hc.cinematics.components;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class CinematicsEffects
{

	private final static Map<String, PotionEffect> cinematicsEffect = new HashMap<String, PotionEffect>();
	private final static String MAP_SPLIT = "_";

	public CinematicsEffects()
	{
		// Cinematic_Timer, text.
		cinematicsEffect.put("0" + MAP_SPLIT + "1",
				new PotionEffect(PotionEffectType.LEVITATION, 100, 40, false, false, null));
		cinematicsEffect.put("0" + MAP_SPLIT + "53",
				new PotionEffect(PotionEffectType.LEVITATION, 1, 1, false, false, null));
	}

	public static void castSimpleEffect(Player player, Integer timer, Integer cinematic)
	{
		String index = cinematic.toString() + MAP_SPLIT + timer.toString();
		player.addPotionEffect(cinematicsEffect.get(index));
	}

	public static void removeEffects(Player player, Integer timer, Integer cinematic)
	{
		for (PotionEffect effect : player.getActivePotionEffects())
			player.removePotionEffect(effect.getType());
	}

}

package fr.hc.cinematics.components;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class CinematicsSeeSight
{

	private final static Map<String, PotionEffect> cinematicsEffect = new HashMap<String, PotionEffect>();
	private final static String MAP_SPLIT = "_";

	public CinematicsSeeSight()
	{
		// Cinematic_Timer, text.
		cinematicsEffect.put("0" + MAP_SPLIT + "0",
				new PotionEffect(PotionEffectType.BLINDNESS, 120, 255, false, false, null));
		cinematicsEffect.put("0" + MAP_SPLIT + "1",
				new PotionEffect(PotionEffectType.SLOW, 120, 255, false, false, null));
		cinematicsEffect.put("0" + MAP_SPLIT + "2",
				new PotionEffect(PotionEffectType.BLINDNESS, 20, 255, false, false, null));
		cinematicsEffect.put("0" + MAP_SPLIT + "5",
				new PotionEffect(PotionEffectType.BLINDNESS, 10, 255, false, false, null));
		cinematicsEffect.put("0" + MAP_SPLIT + "6",
				new PotionEffect(PotionEffectType.BLINDNESS, 10, 255, false, false, null));
		cinematicsEffect.put("0" + MAP_SPLIT + "8",
				new PotionEffect(PotionEffectType.BLINDNESS, 10, 255, false, false, null));
		cinematicsEffect.put("0" + MAP_SPLIT + "10",
				new PotionEffect(PotionEffectType.BLINDNESS, 10, 255, false, false, null));
		cinematicsEffect.put("0" + MAP_SPLIT + "13",
				new PotionEffect(PotionEffectType.BLINDNESS, 10, 255, false, false, null));
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

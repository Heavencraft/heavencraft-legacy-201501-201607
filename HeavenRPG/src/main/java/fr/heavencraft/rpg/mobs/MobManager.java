package fr.heavencraft.rpg.mobs;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;

import fr.heavencraft.rpg.HeavenRPG;
import fr.heavencraft.rpg.zones.Zone;

public class MobManager {

	private static String[] barArray = new String[21];
	private static ArrayList<RPGMob> entities = new ArrayList<RPGMob>();

	public MobManager()
	{
		barArray = new String[21];
		barArray = getDefaultsBars();
	}

	public static void createMob(LivingEntity mob, Zone zone)
	{	
		RPGMob rmob = new RPGMob(mob,zone.get_zoneLevel(), zone);	
		if(!entities.contains(rmob))
			entities.add(rmob);
	}
	public static void addMob(RPGMob mob)
	{
		if(!entities.contains(mob))
			entities.add(mob);
	}

	public static void removeMob(RPGMob mob)
	{
		if(entities.contains(mob))
			entities.remove(mob);
	}

	public static RPGMob getRPGMob(LivingEntity entity)
	{
		for(RPGMob mob : entities)
		{
			if(mob.getMob()== entity)
				return mob;
		}
		return null;
	}

	/**
	 * Supprime tout les mobs spéciaux
	 */
	public static void killAllMobs()
	{
		for(RPGMob mob : getRPGMobs())
			mob.getMob().remove();
		entities.clear();
	}

	public static ArrayList<RPGMob> getRPGMobs()
	{
		return entities;
	}

	public static void showMobHealthBar (final RPGMob mob) {

		if(!entities.contains(mob))
			return;

		Bukkit.getScheduler().scheduleSyncDelayedTask(HeavenRPG.getInstance(), new Runnable() {
			public void run() {

				//check for compatibility
				double health = mob.getMob().getHealth();
				double max = mob.getMob().getMaxHealth();

				//if the health is 0
				if (health <= 0.0) 
					return;

				if(health >= max - 2.0) {
					if(mob.getSpawningName() == null || mob.getSpawningName().equalsIgnoreCase(""))
						mob.getMob().setCustomName("§r§6[Lv." + mob.getLevel() + "]");
					else
						mob.getMob().setCustomName("§r§6[Lv." + mob.getLevel() + "] §r" + mob.getSpawningName());
				}
				else
				{
					barArray = MobManager.getDefaultsBars();		
					mob.getMob().setCustomName("§r§6[Lv." + mob.getLevel() + "] §r" + barArray[roundUpPositiveWithMax(((health/max) * 20.0), 20)]);
				}
			}});
	}

	public static void hideBar(RPGMob mob) {
		if(!entities.contains(mob))
			return;

		String cname = mob.getMob().getCustomName();
		if (cname != null && !cname.startsWith("§r")) {
			//it's a real name! Don't touch it!
			return;
		}
		mob.getMob().setCustomName("");
		mob.getMob().setCustomNameVisible(false);
	}


	public static int roundUpPositive(double d) {
		int i = (int) d;
		double remainder = d - i;
		if (remainder > 0.0) {
			i++;
		}
		if (i<0) return 0;
		return i;
	}

	public static int roundUpPositiveWithMax(double d, int max) {
		int result = roundUpPositive(d);
		if (d > max) return max;
		return result;
	}

	/*
	 *  Used to retrieve the array that contains the health bars from the configs
	 */
	public static String[] getDefaultsBars() {

		String[] barArray = new String[21];

		int mobBarStyle = 5;

		if (mobBarStyle == 2)
		{
			barArray[0] = "§c|§7|||||||||||||||||||"; 		barArray[1] = "§c|§7|||||||||||||||||||";
			barArray[2] = "§c||§7||||||||||||||||||"; 		barArray[3] = "§c|||§7|||||||||||||||||";
			barArray[4] = "§c||||§7||||||||||||||||"; 		barArray[5] = "§e|||||§7|||||||||||||||";
			barArray[6] = "§e||||||§7||||||||||||||"; 		barArray[7] = "§e|||||||§7|||||||||||||";
			barArray[8] = "§e||||||||§7||||||||||||"; 		barArray[9] = "§e|||||||||§7|||||||||||";
			barArray[10] = "§e||||||||||§7||||||||||"; 		barArray[11] = "§a|||||||||||§7|||||||||";
			barArray[12] = "§a||||||||||||§7||||||||"; 		barArray[13] = "§a|||||||||||||§7|||||||";
			barArray[14] = "§a||||||||||||||§7||||||"; 		barArray[15] = "§a|||||||||||||||§7|||||";
			barArray[16] = "§a||||||||||||||||§7||||"; 		barArray[17] = "§a|||||||||||||||||§7|||";
			barArray[18] = "§a||||||||||||||||||§7||"; 		barArray[19] = "§a|||||||||||||||||||§7|";
			barArray[20] = "§a||||||||||||||||||||";
		}
		else if (mobBarStyle == 3)
		{
			barArray[0] = "§c❤§7❤❤❤❤❤❤❤❤❤"; 			barArray[1] = "§c❤§7❤❤❤❤❤❤❤❤❤";
			barArray[2] = "§c❤§7❤❤❤❤❤❤❤❤❤"; 			barArray[3] = "§c❤❤§7❤❤❤❤❤❤❤❤";
			barArray[4] = "§c❤❤§7❤❤❤❤❤❤❤❤"; 				barArray[5] = "§e❤❤❤§7❤❤❤❤❤❤❤";
			barArray[6] = "§e❤❤❤§7❤❤❤❤❤❤❤"; 				barArray[7] = "§e❤❤❤❤§7❤❤❤❤❤❤";
			barArray[8] = "§e❤❤❤❤§7❤❤❤❤❤❤"; 				barArray[9] = "§e❤❤❤❤❤§7❤❤❤❤❤";
			barArray[10] = "§e❤❤❤❤❤§7❤❤❤❤❤"; 				barArray[11] = "§a❤❤❤❤❤❤§7❤❤❤❤";
			barArray[12] = "§a❤❤❤❤❤❤§7❤❤❤❤"; 				barArray[13] = "§a❤❤❤❤❤❤❤§7❤❤❤";
			barArray[14] = "§a❤❤❤❤❤❤❤§7❤❤❤"; 				barArray[15] = "§a❤❤❤❤❤❤❤❤§7❤❤";
			barArray[16] = "§a❤❤❤❤❤❤❤❤§7❤❤"; 				barArray[17] = "§a❤❤❤❤❤❤❤❤❤§7❤";
			barArray[18] = "§a❤❤❤❤❤❤❤❤❤§7❤";					barArray[19] = "§a❤❤❤❤❤❤❤❤❤❤";
			barArray[20] = "§a❤❤❤❤❤❤❤❤❤❤";
		}
		else if (mobBarStyle == 4) 
		{
			barArray[0] = "§a▌§8▌▌▌▌▌▌▌▌▌▌▌▌▌▌▌▌▌▌▌"; 		barArray[1] = "§a▌§8▌▌▌▌▌▌▌▌▌▌▌▌▌▌▌▌▌▌▌";
			barArray[2] = "§a▌▌§8▌▌▌▌▌▌▌▌▌▌▌▌▌▌▌▌▌▌"; 		barArray[3] = "§a▌▌▌§8▌▌▌▌▌▌▌▌▌▌▌▌▌▌▌▌▌";
			barArray[4] = "§a▌▌▌▌§8▌▌▌▌▌▌▌▌▌▌▌▌▌▌▌▌"; 		barArray[5] = "§a▌▌▌▌▌§8▌▌▌▌▌▌▌▌▌▌▌▌▌▌▌";
			barArray[6] = "§a▌▌▌▌▌▌§8▌▌▌▌▌▌▌▌▌▌▌▌▌▌"; 		barArray[7] = "§a▌▌▌▌▌▌▌§8▌▌▌▌▌▌▌▌▌▌▌▌▌";
			barArray[8] = "§a▌▌▌▌▌▌▌▌§8▌▌▌▌▌▌▌▌▌▌▌▌"; 		barArray[9] = "§a▌▌▌▌▌▌▌▌▌§8▌▌▌▌▌▌▌▌▌▌▌";
			barArray[10] = "§a▌▌▌▌▌▌▌▌▌▌§8▌▌▌▌▌▌▌▌▌▌"; 		barArray[11] = "§a▌▌▌▌▌▌▌▌▌▌▌§8▌▌▌▌▌▌▌▌▌";
			barArray[12] = "§a▌▌▌▌▌▌▌▌▌▌▌▌§8▌▌▌▌▌▌▌▌"; 		barArray[13] = "§a▌▌▌▌▌▌▌▌▌▌▌▌▌§8▌▌▌▌▌▌▌";
			barArray[14] = "§a▌▌▌▌▌▌▌▌▌▌▌▌▌▌§8▌▌▌▌▌▌"; 		barArray[15] = "§a▌▌▌▌▌▌▌▌▌▌▌▌▌▌▌§8▌▌▌▌▌";
			barArray[16] = "§a▌▌▌▌▌▌▌▌▌▌▌▌▌▌▌▌§8▌▌▌▌"; 		barArray[17] = "§a▌▌▌▌▌▌▌▌▌▌▌▌▌▌▌▌▌§8▌▌▌";
			barArray[18] = "§a▌▌▌▌▌▌▌▌▌▌▌▌▌▌▌▌▌▌§8▌▌"; 		barArray[19] = "§a▌▌▌▌▌▌▌▌▌▌▌▌▌▌▌▌▌▌▌§8▌";
			barArray[20] = "§a▌▌▌▌▌▌▌▌▌▌▌▌▌▌▌▌▌▌▌▌";
		}
		else if (mobBarStyle == 5) 
		{
			barArray[0] = "§c█§0█████████"; 			barArray[1] = "§c█§0█████████";
			barArray[2] = "§c█§0█████████"; 			barArray[3] = "§c██§0████████";
			barArray[4] = "§c██§0████████"; 			barArray[5] = "§e███§0███████";
			barArray[6] = "§e███§0███████"; 			barArray[7] = "§e████§0██████";
			barArray[8] = "§e████§0██████"; 			barArray[9] = "§e█████§0█████";
			barArray[10] = "§e█████§0█████"; 			barArray[11] = "§a██████§0████";
			barArray[12] = "§a██████§0████"; 			barArray[13] = "§a███████§0███";
			barArray[14] = "§a███████§0███";			barArray[15] = "§a████████§0██";
			barArray[16] = "§a████████§0██"; 			barArray[17] = "§a█████████§0█";
			barArray[18] = "§a█████████§0█"; 			barArray[19] = "§a██████████";
			barArray[20] = "§a██████████";
		}
		else
		{
			//default (1 or anything else)
			barArray[0] = "§c▌                   "; 		barArray[1] = "§c▌                   ";
			barArray[2] = "§c█                  "; 		barArray[3] = "§c█▌                 ";
			barArray[4] = "§c██                "; 		barArray[5] = "§e██▌               ";
			barArray[6] = "§e███              "; 			barArray[7] = "§e███▌             ";
			barArray[8] = "§e████            "; 			barArray[9] = "§e████▌           ";
			barArray[10] = "§e█████          "; 			barArray[11] = "§a█████▌         ";
			barArray[12] = "§a██████        "; 			barArray[13] = "§a██████▌       ";
			barArray[14] = "§a███████      "; 			barArray[15] = "§a███████▌     ";
			barArray[16] = "§a████████    "; 				barArray[17] = "§a████████▌   ";
			barArray[18] = "§a█████████  "; 				barArray[19] = "§a█████████▌ ";
			barArray[20] = "§a██████████";
		}

		return barArray;
	}
}

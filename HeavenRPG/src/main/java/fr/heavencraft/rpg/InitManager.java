package fr.heavencraft.rpg;

import org.bukkit.Bukkit;

import fr.heavencraft.Utils.ChatUtil;
import fr.heavencraft.Utils.DevUtils;
import fr.heavencraft.rpg.Parchemins.ParcheminCommand;
import fr.heavencraft.rpg.Parchemins.ParcheminProvider;
import fr.heavencraft.rpg.Parchemins.ParcheminsListener;
import fr.heavencraft.rpg.donjon.DungeonCommand;
import fr.heavencraft.rpg.donjon.DungeonListener;
import fr.heavencraft.rpg.donjon.DungeonManager;
import fr.heavencraft.rpg.donjon.DungeonSignListener;
import fr.heavencraft.rpg.mobs.MobManager;
import fr.heavencraft.rpg.player.RPGPlayerListener;
import fr.heavencraft.rpg.zones.ZoneCommand;
import fr.heavencraft.rpg.zones.ZoneListener;
import fr.heavencraft.rpg.zones.ZoneManager;

public class InitManager {
	public static void init()
	{
		initCommands();
		initListeners();
		initOther();
	}
	
	private static void initCommands()
	{
		
		/*
		 * from HeavenRPG
		 */

		// Zone
		new ZoneCommand();
		new ParcheminCommand();
		new DungeonCommand();
		
		// Task is runned after done loading
		Bukkit.getScheduler().runTaskLater(HeavenRPG.getInstance(), new Runnable()
		{
			@Override
			public void run()
			{
				ZoneManager.loadAllZones();
				ParcheminProvider.LoadParchemins();
				DungeonManager.loadDungeons();
			}
		}, 0);	
		
	}

	private static void initListeners()
	{
		new RPGPlayerListener();
		new ZoneListener();
		new ParcheminsListener();
		new DungeonSignListener();
		new DungeonListener();
	}
	
	private static void initOther()
	{
		new MobManager();
		new ChatUtil();
		new DevUtils();
	}
	
}

package fr.heavencraft.adventwool;

import java.util.Date;
import java.util.Random;

import org.bukkit.plugin.java.JavaPlugin;

public class AdventWoolPlugin extends JavaPlugin
{
	private final Random _rand = new Random();
	private AdventWoolEvent adventWool;

	@Override
	public void onEnable()
	{
		super.onEnable();

		adventWool = new AdventWoolEvent(this);
		new AdventWoolListener(this);

	}

	public int randNext(int min, int max)
	{
		return _rand.nextInt(max - min) + min;
	}

	public void setAventDate(String player)
	{
		getConfig().set("adventwool." + player, new Date().getDate());
		saveConfig();
	}

	public boolean canAdventWool(String player)
	{
		return getConfig().getInt("adventwool." + player) != new Date().getDate();
	}

	public AdventWoolEvent getAdventWool()
	{
		// TODO Auto-generated method stub
		return adventWool;
	}
}

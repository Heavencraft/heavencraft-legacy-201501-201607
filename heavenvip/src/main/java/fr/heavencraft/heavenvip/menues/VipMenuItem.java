package fr.heavencraft.heavenvip.menues;

import fr.heavencraft.heavencore.utils.menu.options.Option;

public class VipMenuItem
{
	private final Option option;
	private final int x;
	private final int y;
	
	public VipMenuItem(int x, int y, Option opt) {
		this.option = opt;
		this.x = x;
		this.y = y;
	}
	
	public Option getOption()
	{
		return option;
	}
	public int getX()
	{
		return x;
	}
	public int getY()
	{
		return y;
	}
}

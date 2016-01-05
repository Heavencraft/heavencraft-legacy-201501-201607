package fr.heavencraft.heavenvip.menues;

import java.util.Collection;

import org.bukkit.entity.Player;

import fr.heavencraft.heavencore.utils.menu.Menu;

public interface IVipMenu
{
	public Menu getMenu(Player p);
	
	public Collection<VipMenuItem> getOptions(Player p);
}

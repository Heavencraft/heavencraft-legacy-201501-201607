package fr.heavencraft.heavenrp.dungeon;

import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.block.SignChangeEvent;

import fr.heavencraft.heavencore.bukkit.listeners.AbstractSignListener;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavenrp.HeavenRP;
import fr.heavencraft.heavenrp.RPPermissions;

public class DungeonSignListener extends AbstractSignListener
{

	public DungeonSignListener(HeavenRP plugin)
	{
		super(plugin, "Donjon", RPPermissions.DUNGEON_ADMIN);
	}

	@Override
	protected boolean onSignPlace(Player player, SignChangeEvent event) throws HeavenException
	{
		//TODO Check if dungeon exists when line 1 = JOIN
		//TODO Check if room exist in dungeon when 1 = NEXT && 2 = ROOMID
		
		return true;
	}

	@Override
	protected void onSignClick(Player player, Sign sign) throws HeavenException
	{
		// TODO Auto-generated method stub
		
	}

}

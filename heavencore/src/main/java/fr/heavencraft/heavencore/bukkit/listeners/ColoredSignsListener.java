package fr.heavencraft.heavencore.bukkit.listeners;

import java.util.regex.Pattern;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.SignChangeEvent;

import fr.heavencraft.heavencore.CorePermissions;
import fr.heavencraft.heavencore.bukkit.HeavenPlugin;

/**
 * This listener allows to place colored signs
 * 
 * @author lorgan17
 */
public class ColoredSignsListener extends AbstractListener<HeavenPlugin>
{
	public ColoredSignsListener(HeavenPlugin plugin)
	{
		super(plugin);
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	private void onSignChange(SignChangeEvent event)
	{
		if (event.getPlayer().hasPermission(CorePermissions.COLORED_SIGNS))
		{
			final Pattern pattern = Pattern.compile("\\&([0-9A-Fa-f])");

			for (int i = 0; i != 4; i++)
				event.setLine(i, pattern.matcher(event.getLine(i)).replaceAll("§$1"));
		}
	}
}
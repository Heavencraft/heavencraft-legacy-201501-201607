package fr.heavencraft.heavencore.bukkit.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.weather.ThunderChangeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

import fr.heavencraft.heavencore.bukkit.HeavenPlugin;

/**
 * ClearWeatherListener Listener which keeps the weather clear.
 * 
 * @author lorgan17
 */
public final class ClearWeatherListener extends AbstractListener<HeavenPlugin>
{
	public ClearWeatherListener(HeavenPlugin plugin)
	{
		super(plugin);
	}

	@EventHandler(ignoreCancelled = true)
	private void onWeatherChange(WeatherChangeEvent event)
	{
		if (event.toWeatherState())
			event.setCancelled(true);
	}

	@EventHandler(ignoreCancelled = true)
	private void onThunderChange(ThunderChangeEvent event)
	{
		if (event.toThunderState())
			event.setCancelled(true);
	}
}
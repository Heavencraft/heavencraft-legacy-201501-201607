package fr.hc.scrolls;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;

import fr.hc.worlds.WorldsManager;

public class ScrollProvider
{
	private static ScrollProvider scrollProvider = null;

	public static ScrollProvider getInstance()
	{
		if (scrollProvider == null)
		{
			scrollProvider = new ScrollProvider();
		}
		return scrollProvider;
	}

	private Map<String, Scroll> scrolls = new HashMap<String, Scroll>();

	/**
	 * Loads all scrolls
	 */
	private ScrollProvider()
	{
		/*
		 * Teleportation Scrolls
		 */
		// Tour de garde
		String[] loreTourDeGarde = { "&eTéléportation", "&eVers le donjon de la Tour de Garde" };
		Scroll scrollTourDeGarde = new TeleportationScroll("Téléporation: La tour de garde",
				Arrays.asList(loreTourDeGarde),
				new Location(WorldsManager.getWorld(), -1932.0, 64.0, -1179.0, 91f, 8f));
		scrolls.put(scrollTourDeGarde.getItem().getItemMeta().getDisplayName(), scrollTourDeGarde);

	}

	/**
	 * Returns the scroll corresponding to the itemName
	 * 
	 * @param itemName
	 * @return
	 */
	public Scroll getScroll(String itemName)
	{
		return scrolls.get(itemName);
	}

	/**
	 * Returns the scroll corresponding to the index
	 * 
	 * @param index
	 * @return
	 */
	public Scroll getScroll(int index)
	{
		int size = scrolls.values().size();
		if (index >= size || index < 0)
			return null;
		Scroll[] array = new Scroll[scrolls.values().size()];
		scrolls.values().toArray(array);
		return array[index];
	}

	/**
	 * Returns a collection of scrolls
	 * 
	 * @return
	 */
	public Scroll[] getScrolls()
	{
		Scroll[] array = new Scroll[scrolls.values().size()];
		scrolls.values().toArray(array);
		return array;
	}

}

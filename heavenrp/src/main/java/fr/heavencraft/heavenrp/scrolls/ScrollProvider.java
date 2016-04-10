package fr.heavencraft.heavenrp.scrolls;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;

import fr.heavencraft.heavenrp.worlds.WorldsManager;

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

		// Poste d'extraction n°2
		String[] lorePosteExtraction2 = { "&eTéléportation", "&eVers le donjon du Poste d'extraction n°2" };
		Scroll scrollPosteExtraction2 = new TeleportationScroll("Téléporation: Poste d'extraction n°2",
				Arrays.asList(lorePosteExtraction2),
				new Location(WorldsManager.getWorld(), 2495.0, 69.0, -973.0, 0f, 0f));
		scrolls.put(scrollPosteExtraction2.getItem().getItemMeta().getDisplayName(), scrollPosteExtraction2);

		// Tchernocraft
		String[] loreTchernocraft = { "&eTéléportation", "&eVers le donjon de Tchernocraft" };
		Scroll scrollTchernocraft = new TeleportationScroll("Téléporation: Tchernocraft",
				Arrays.asList(loreTchernocraft), new Location(WorldsManager.getWorld(), 4165.0, 66.0, 854.0, 0f, 0f));
		scrolls.put(scrollTchernocraft.getItem().getItemMeta().getDisplayName(), scrollTchernocraft);

		// Montage ne neige
		String[] loreSnowMountain = { "&eTéléportation", "&eVers le donjon de la Montagne de Neige" };
		Scroll scrollSnowMountain = new TeleportationScroll("Téléporation: la Montagne de Neige",
				Arrays.asList(loreSnowMountain),
				new Location(WorldsManager.getWorld(), -1581.0, 110.0, -2307.0, 37f, 0f));
		scrolls.put(scrollSnowMountain.getItem().getItemMeta().getDisplayName(), scrollSnowMountain);

		// Temple des sables
		String[] loreSandTemple = { "&eTéléportation", "&eVers le Temple des Sables" };
		Scroll scrollSandTemple = new TeleportationScroll("Téléporation: le Temple des Sables",
				Arrays.asList(loreSandTemple), new Location(WorldsManager.getWorld(), -1364.0, 64.0, -240.0, -90f, 0f));
		scrolls.put(scrollSandTemple.getItem().getItemMeta().getDisplayName(), scrollSandTemple);

		// Du mineur
		String[] loreMines = { "&eTéléportation", "&eVers les Mines" };
		Scroll scrollMines = new TeleportationScroll("Téléporation: les Mines",
				Arrays.asList(loreMines), new Location(WorldsManager.getWorld(), -4174.0, 76.0, -196.0, 63f, 0f));
		scrolls.put(scrollMines.getItem().getItemMeta().getDisplayName(), scrollMines);
		
		// Les mines de charbon
		String[] loreCoalMines = { "&eTéléportation", "&eVers les Mines de charbon" };
		Scroll scrollCoalMines = new TeleportationScroll("Téléporation: les Mines de charbon",
				Arrays.asList(loreCoalMines), new Location(WorldsManager.getWorld(), 2263.0, 75.0, -1619.0, 66f, 0f));
		scrolls.put(scrollCoalMines.getItem().getItemMeta().getDisplayName(), scrollCoalMines);

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

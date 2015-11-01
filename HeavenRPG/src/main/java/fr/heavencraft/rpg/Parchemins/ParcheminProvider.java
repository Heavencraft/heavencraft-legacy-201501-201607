package fr.heavencraft.rpg.Parchemins;

import java.util.ArrayList;

import org.bukkit.inventory.ItemStack;

import fr.heavencraft.rpg.Parchemins.Parchemins.ParcheminAuraDeLaBienfaisansce;
import fr.heavencraft.rpg.Parchemins.Parchemins.ParcheminAntreDesPoissions;
import fr.heavencraft.rpg.Parchemins.Parchemins.ParcheminDuChampignionium;
import fr.heavencraft.rpg.Parchemins.Parchemins.ParcheminExtractionPoste2;
import fr.heavencraft.rpg.Parchemins.Parchemins.ParcheminLePetitPetDuNord;
import fr.heavencraft.rpg.Parchemins.Parchemins.ParcheminLeSouffleDuNecromantien;
import fr.heavencraft.rpg.Parchemins.Parchemins.ParcheminMineDeCharbon;
import fr.heavencraft.rpg.Parchemins.Parchemins.ParcheminPortailDuMineur;
import fr.heavencraft.rpg.Parchemins.Parchemins.ParcheminPortailDuTempleDesSables;
import fr.heavencraft.rpg.Parchemins.Parchemins.ParcheminPortailMontageDeNeige;
import fr.heavencraft.rpg.Parchemins.Parchemins.ParcheminPousseeQuantique;
import fr.heavencraft.rpg.Parchemins.Parchemins.ParcheminTchernocraft;
import fr.heavencraft.rpg.Parchemins.Parchemins.ParcheminTonnereDivin;
import fr.heavencraft.rpg.Parchemins.Parchemins.ParcheminTourDeGarde;

public class ParcheminProvider {
	private static ArrayList<IParchemin> parchemins = new ArrayList<IParchemin>();
	
	public static void LoadParchemins()
	{
		addParchemin(new ParcheminLePetitPetDuNord());
		addParchemin(new ParcheminTonnereDivin());
		addParchemin(new ParcheminPousseeQuantique());
		addParchemin(new ParcheminAuraDeLaBienfaisansce());
		addParchemin(new ParcheminLeSouffleDuNecromantien());
		addParchemin(new ParcheminPortailDuMineur());
		addParchemin(new ParcheminPortailDuTempleDesSables());
		addParchemin(new ParcheminTourDeGarde());
		addParchemin(new ParcheminPortailMontageDeNeige());
		addParchemin(new ParcheminTchernocraft());
		addParchemin(new ParcheminExtractionPoste2());
		addParchemin(new ParcheminAntreDesPoissions());
		addParchemin(new ParcheminMineDeCharbon());
		addParchemin(new ParcheminDuChampignionium());
	}
	
	
	public static void addParchemin(IParchemin p)
	{
		if(!parchemins.contains(p))
			parchemins.add(p);
	}
	
	public static void removeParchemin(IParchemin p)
	{
		if(parchemins.contains(p))
			parchemins.remove(p);
	}
	
	public static ArrayList<IParchemin> getParchemins()
	{
		return parchemins;
	}
	
	/**
	 * Retourne le parchemin correspondant a l'item, en vérifiant le nom de celui-ci
	 * @param item
	 * @return
	 */
	public static IParchemin getParcheminByItem(ItemStack item)
	{
		for(IParchemin p: parchemins)
		{
			if(p.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(item.getItemMeta().getDisplayName()))
				return p;
		}
		return null;
	}
}

package fr.heavencraft.heavenrp.structureblock;

import org.bukkit.Material;
import org.bukkit.util.Vector;

public abstract class StructureBlock
{
	// smeltery properties
	final static Vector smelterySize = new Vector(9, 10, 9);
	final static String SMELTERYFILENAME = "/smeltery.cfg";
	static Material[][][] smelteryLayers = new Material[(int) smelterySize.getY()][(int) smelterySize
			.getZ()][(int) smelterySize.getX()];;

	public static void loadStructure()
	{
		// fill smeltery
		smelteryLayers = StructureBlockReader.fillStructureList(SMELTERYFILENAME, smelteryLayers);
	}
}

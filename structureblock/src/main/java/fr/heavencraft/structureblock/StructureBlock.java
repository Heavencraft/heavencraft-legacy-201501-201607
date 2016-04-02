package fr.heavencraft.structureblock;

import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public class StructureBlock extends JavaPlugin
{
	// smeltery Properties
	final Vector smelterySize = new Vector(9, 10, 9);
	final String SMELTERYFILENAME = "/smeltery.cfg";
	final Material[][][] smelteryLayers = StructureBlockReader.fillStructureList(SMELTERYFILENAME, smelterySize,
			this);

	public void onEnable()
	{
		new StructureBlockSmelteryListener(this, smelterySize);
		new StructureBlockSmelteryInventoryListener(this);
	}
}

package fr.heavencraft.structureblock;

import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public class StructureBlock extends JavaPlugin
{

	// Smeltery Properties
	final Vector smelterySize = new Vector(9, 10, 9);
	final String smelteryFileName = "/smeltery.cfg";
	final Material[][][] smelteryLayers = StructureBlockReader.fillStructureList(smelteryFileName, smelterySize,
			this);

	@Override
	public void onEnable()
	{
		new StructureBlockSmelteryListener(this, smelterySize);
		new StructureBlockSmelteryInventoryListener(this);
	}

}

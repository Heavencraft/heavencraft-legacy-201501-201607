package fr.heavencraft.heavenrp.structureblock;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.bukkit.Material;
import org.bukkit.util.Vector;

import fr.heavencraft.heavenrp.HeavenRP;

public class StructureProvider
{
	private final File structuresDirectory = new File(HeavenRP.getInstance().getDataFolder(), "structures");
	private final Collection<Structure> structures = new ArrayList<Structure>();

	public void loadConfig()
	{
		if (!structuresDirectory.exists() || !structuresDirectory.isDirectory())
		{
			// Print warning ?
			return;
		}

		for (final File structureFile : structuresDirectory.listFiles())
			loadStructureFile(structureFile);
	}

	private void loadStructureFile(File file)
	{
		// fill 3D list by reading a configuration file
		try (BufferedReader reader = new BufferedReader(new FileReader(file)))
		{
			String line;
			int y = 0;

			final Structure structure = new Structure();
			while ((line = reader.readLine()) != null)
			{
				line = line.trim();

				// empty lines of comments
				if (line.isEmpty() || line.startsWith("#"))
					continue;

				// upper layer
				if (line.startsWith("+"))
				{
					y++;
					continue;
				}

				addMaterial(structure, line, y);
			}

			structures.add(structure);
		}
		catch (final IOException e)
		{
			e.printStackTrace();
		}
	}

	public void addMaterial(Structure structure, String line, int y)
	{
		// search Z
		final String[] splitEquals = line.split("=", 2);
		final int z = Integer.parseInt(splitEquals[0]);

		int x = 0;

		for (String blockName : splitEquals[1].split("\\|"))
		{
			x++;
			blockName = blockName.trim();

			// We don't care about block type
			if (blockName.isEmpty())
				continue;

			structure.setMaterial(new Vector(x, y, z), Material.getMaterial(blockName));
		}
	}
}
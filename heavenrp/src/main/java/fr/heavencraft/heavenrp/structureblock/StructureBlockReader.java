package fr.heavencraft.heavenrp.structureblock;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.bukkit.Material;

import fr.heavencraft.heavenrp.HeavenRP;

public class StructureBlockReader
{

	/**
	 * define a material 3D list with by reading a configuration file
	 * 
	 * @param fileName
	 * @param layers
	 * @return
	 */
	static Material[][][] fillStructureList(String fileName, Material[][][] layers)
	{

		// fill 3D list by reading a configuration file
		try (final BufferedReader reader = new BufferedReader(
				new FileReader(HeavenRP.getInstance().getDataFolder().getPath() + fileName)))
		{
			String line;
			int y = 0;

			while ((line = reader.readLine()) != null)
			{
				line = line.trim();

				// empty lines of comments
				if (line.isEmpty() || line.startsWith("#"))
					continue;

				// upper layer
				else if (line.startsWith("+"))
					y++;

				// define layer Y
				else
					layers = addMaterial(line, y, layers);
			}
		}
		catch (final FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (final IOException e)
		{
			e.printStackTrace();
		}
		return layers;
	}
}

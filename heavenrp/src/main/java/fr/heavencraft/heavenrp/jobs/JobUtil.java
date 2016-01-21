package fr.heavencraft.heavenrp.jobs;

import java.util.ArrayList;
import java.util.Collection;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;

import fr.heavencraft.heavencore.exceptions.HeavenException;

public class JobUtil
{
	private static final double U0 = 100; // Xp to level 2
	private static final double XP_MULTIPLIER = 1.05;

	// Xp needed to go to the next level :
	// U0 = 100
	// U1 = U0 * 1,05
	// U2 = U1 * 1,05 = U0 * 1,05^2
	// ----------------------------------
	// Un = U0 * 1,05^n

	// Total Xp needed to go to a level from 0 :
	// L0 = U0
	// L1 = U0 + U1 = U0 + (U0 * 1,05) = U0 * (1 + 1,05)
	// L2 = U0 + U1 + U2 = U0 + (U0 * 1,05) + (U0 * 1,05^2) = U0 * (1 + 1,05 + 1,05^2)
	// ----------------------------------
	// L(n) = U0 * P(n)
	// P(n) = 1 + 1,05 * P(n-1)

	public static int getLevelFromXp(long xp)
	{
		int level = 1;
		double levelXp = 1;
		final double xpLimit = xp / U0;

		while (true)
		{
			if (levelXp > xpLimit)
				return level;

			level++;
			levelXp = 1 + XP_MULTIPLIER * levelXp;
		}
	}

	public static void addActions(String input, Job job) throws HeavenException
	{
		final String[] inputData = input.split("\\.");

		if (inputData.length != 3 || inputData[0].length() != 1)
			throw new HeavenException("Invalid job action '%1$s'");

		final JobActionType actionType = JobActionType.getActionTypeByCode(inputData[0].charAt(0));
		final int points = Integer.parseInt(inputData[2]);

		if (actionType.getExpectedParamClass() == BlockType.class)
		{
			for (final BlockType blockType : getBlockTypes(inputData[1]))
			{
				job.addAction(new JobAction(actionType, blockType), points);
			}
		}
		else if (actionType.getExpectedParamClass() == EntityType.class)
		{
			job.addAction(new JobAction(actionType, getEntityType(inputData[1])), points);
		}
	}

	private static Collection<BlockType> getBlockTypes(String input) throws HeavenException
	{
		final Collection<BlockType> blockTypes = new ArrayList<BlockType>();
		final int index = input.indexOf(':');

		// "Material:data"
		if (index != -1)
		{
			final Material type = getMaterial(input.substring(0, index));
			final byte data = Byte.parseByte(input.substring(index + 1));

			blockTypes.add(new BlockType(type, data));
		}
		// "Material"
		else
		{
			final Material type = getMaterial(input);

			for (byte data = 0; data != 16; data++)
			{
				blockTypes.add(new BlockType(type, data));
			}
		}

		return blockTypes;
	}

	private static Material getMaterial(String name) throws HeavenException
	{
		final Material material = Material.getMaterial(name);

		if (material == null)
			throw new HeavenException("Invalid Material '%1$s'", name);

		return material;
	}

	private static EntityType getEntityType(String name) throws HeavenException
	{
		final EntityType entityType = EntityType.valueOf(name);

		if (entityType == null)
			throw new HeavenException("Invalid EntityType '%1$s'", name);

		return entityType;
	}
}
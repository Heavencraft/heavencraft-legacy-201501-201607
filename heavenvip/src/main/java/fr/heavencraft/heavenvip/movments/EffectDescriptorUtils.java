package fr.heavencraft.heavenvip.movments;

import java.util.ArrayList;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.utils.DevUtil;
import fr.heavencraft.heavencore.utils.particles.ParticleEffect;

public class EffectDescriptorUtils
{
	/**
	 * Translates a formatted data sting to a usable effect: 
	 * Simple:
	 * {effectID}:{amount}|{effectID}:{amount}|... 
	 * With Color:
	 * {effectID}:{amount}:color.{0-255}.{0-255}.{0-255}
	 * With Note Color
	 * {effectID}:{amount}:note.{0-24}
	 * 
	 * @param data
	 * @return
	 * @throws HeavenException
	 */
	public static ArrayList<AppliedDescriptorProperties> translateEffectString(String data) throws HeavenException
	{
		ArrayList<AppliedDescriptorProperties> effect = new ArrayList<AppliedDescriptorProperties>();

		// Token each element separated by |
		String[] descriptorBlocs = data.split("\\|");
		for (int i = 0; i < descriptorBlocs.length; i++)
		{
			// extract amount
			String[] tEffectParameters = descriptorBlocs[i].split(":");

			// Do we have more than 3 parameters? {effectID}:{amount}:{speed}:[SOMETHEING]
			if (tEffectParameters.length < 3)
				throw new HeavenException(
						"Incomplete Particle Effect Description String (not enougth parameters): " + data);

			ParticleEffect pe = ParticleEffect.fromId(Integer.parseInt(tEffectParameters[0]));
			if (pe == null)
				throw new HeavenException("Unkonwn Particle Effect ID used: " + tEffectParameters[0]);

			// Do we have additional data?
			if (tEffectParameters.length == 4)
			{
				// {effectID}:{amount}:{speed}:{SOMETHEING}
				// Token the {SOMETHING} bloc in format
				// {typeOfData}.{xxx}.{xxx}...
				String[] tArguments = tEffectParameters[3].split("\\,");
				if (tArguments[0].equalsIgnoreCase("color"))
				{
					// Ordinary Color: color,{R 0-255},{G 0-255},{B 0-255}
					if (tArguments.length != 4)
						throw new HeavenException(
								"Invalid token for effect parameter count. Color: color.{R 0-255}.{G 0-255}.{B 0-255}, but got: "
										+ tEffectParameters[2]);
					ParticleEffect.OrdinaryColor oc = new ParticleEffect.OrdinaryColor(Color.fromRGB(
							DevUtil.toUint(tArguments[1]), DevUtil.toUint(tArguments[2]),
							DevUtil.toUint(tArguments[3])));
					
					// Color format {effectID}:{amount}:{speed}:color,{255},{255},{255}
					AppliedDescriptorProperties tp = new AppliedDescriptorProperties(pe,
							Integer.parseInt(tEffectParameters[1]), oc);
					effect.add(tp);
				} 
				else if(tArguments[0].equalsIgnoreCase("note")) 
				{
					if (tArguments.length != 2)
						throw new HeavenException(
								"Invalid token for effect parameter count. Note Color: note,{0-24}, but got: "
										+ tEffectParameters[2]);
					ParticleEffect.NoteColor nc = new ParticleEffect.NoteColor(DevUtil.toUint(tArguments[1]));
					
					// Note Color format {effectID}:{amount}:note,{0-24}
					AppliedDescriptorProperties tp = new AppliedDescriptorProperties(pe,
							Integer.parseInt(tEffectParameters[1]), nc);
					effect.add(tp);
				}
				else
				{
					throw new HeavenException("Unkown effect description parameter type: " + tArguments[0] + " of " + descriptorBlocs[i]);
				}

			}
			else if (tEffectParameters.length == 2)
			{
				// Simple format {effectID}:{amount}:{speed}
				AppliedDescriptorProperties tp = new AppliedDescriptorProperties(pe,
						Integer.parseInt(tEffectParameters[1]), Float.parseFloat(tEffectParameters[2]));
				effect.add(tp);
			}
			else
				throw new HeavenException("Invalid Particle Effect data: " + data);
		}

		return effect;
	}

	/**
	 * Spawns a wing for a defined player
	 * 
	 * @param p
	 * @param c1
	 * @param c2
	 * @param c3
	 */
	public static void SpawnEing(final Player p, ParticleEffect.OrdinaryColor c1, ParticleEffect.OrdinaryColor c2,
			ParticleEffect.OrdinaryColor c3)
	{
		Location loc = p.getLocation().clone();
		loc.setPitch(0.0F);
		loc.add(0.0D, 1.8D, 0.0D);
		loc.add(loc.getDirection().multiply(-0.2D));
		ParticleEffect.OrdinaryColor color = c1;
		ParticleEffect.OrdinaryColor color2 = c2;
		ParticleEffect.OrdinaryColor color4 = c3;

		Location loc1R = loc.clone();
		loc1R.setYaw(loc1R.getYaw() + 110.0F);
		Location loc2R = loc1R.clone().add(loc1R.getDirection().multiply(1));
		ParticleEffect.REDSTONE.display(color2, loc2R.add(0.0D, 0.8D, 0.0D), 30.0D);

		Location loc3R = loc1R.clone().add(loc1R.getDirection().multiply(0.8D));
		ParticleEffect.REDSTONE.display(color2, loc3R.add(0.0D, 0.6D, 0.0D), 30.0D);
		Location loc4R = loc1R.clone().add(loc1R.getDirection().multiply(0.6D));
		ParticleEffect.REDSTONE.display(color2, loc4R.add(0.0D, 0.4D, 0.0D), 30.0D);
		Location loc5R = loc1R.clone().add(loc1R.getDirection().multiply(0.4D));
		ParticleEffect.REDSTONE.display(color2, loc5R.clone().add(0.0D, -0.2D, 0.0D), 30.0D);
		Location loc6R = loc1R.clone().add(loc1R.getDirection().multiply(0.2D));
		ParticleEffect.REDSTONE.display(color2, loc6R.add(0.0D, -0.2D, 0.0D), 30.0D);
		int zu = 0;
		while (zu <= 3)
		{
			zu++;
			ParticleEffect.OrdinaryColor color3;
			// ParticleUtils.OrdinaryColor color3;
			if (zu == 4)
			{
				color3 = color2;
			}
			else
			{
				color3 = color;
			}
			if ((color4 != null) && ((zu == 4) || (zu == 3)))
			{
				color3 = color4;
			}
			ParticleEffect.REDSTONE.display(color2, loc2R.add(0.0D, -0.2D, 0.0D), 30.0D);
			ParticleEffect.REDSTONE.display(color3, loc3R.add(0.0D, -0.2D, 0.0D), 30.0D);
			ParticleEffect.REDSTONE.display(color3, loc4R.add(0.0D, -0.2D, 0.0D), 30.0D);
			ParticleEffect.REDSTONE.display(color3, loc5R.add(0.0D, -0.2D, 0.0D), 30.0D);
			ParticleEffect.REDSTONE.display(color3, loc6R.add(0.0D, -0.2D, 0.0D), 30.0D);
		}
		Location loc1L = loc.clone();
		loc1L.setYaw(loc1L.getYaw() - 110.0F);
		Location loc2L = loc1L.clone().add(loc1L.getDirection().multiply(1));
		ParticleEffect.REDSTONE.display(color2, loc2L.add(0.0D, 0.8D, 0.0D), 30.0D);

		Location loc3L = loc1L.clone().add(loc1L.getDirection().multiply(0.8D));
		ParticleEffect.REDSTONE.display(color2, loc3L.add(0.0D, 0.6D, 0.0D), 30.0D);
		Location loc4L = loc1L.clone().add(loc1L.getDirection().multiply(0.6D));
		ParticleEffect.REDSTONE.display(color2, loc4L.add(0.0D, 0.4D, 0.0D), 30.0D);
		Location loc5L = loc1L.clone().add(loc1L.getDirection().multiply(0.4D));
		ParticleEffect.REDSTONE.display(color2, loc5L.clone().add(0.0D, -0.2D, 0.0D), 30.0D);
		Location loc6L = loc1L.clone().add(loc1L.getDirection().multiply(0.2D));
		ParticleEffect.REDSTONE.display(color2, loc6L.add(0.0D, -0.2D, 0.0D), 30.0D);

		zu = 0;
		while (zu <= 3)
		{
			zu++;
			ParticleEffect.OrdinaryColor color3;
			// ParticleUtils.OrdinaryColor color3;
			if (zu == 4)
			{
				color3 = color2;
			}
			else
			{
				color3 = color;
			}
			if ((color4 != null) && ((zu == 4) || (zu == 3)))
			{
				color3 = color4;
			}
			ParticleEffect.REDSTONE.display(color2, loc2L.add(0.0D, -0.2D, 0.0D), 30.0D);
			ParticleEffect.REDSTONE.display(color3, loc3L.add(0.0D, -0.2D, 0.0D), 30.0D);
			ParticleEffect.REDSTONE.display(color3, loc4L.add(0.0D, -0.2D, 0.0D), 30.0D);
			ParticleEffect.REDSTONE.display(color3, loc5L.add(0.0D, -0.2D, 0.0D), 30.0D);
			ParticleEffect.REDSTONE.display(color3, loc6L.add(0.0D, -0.2D, 0.0D), 30.0D);
		}
	}

}

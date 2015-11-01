package fr.heavencraft.rpg.artifacts.effects;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import fr.heavencraft.exceptions.HeavenException;

/**
 * Effect which throw an strike lightning effect on near targets
 */
public class ThunderEffect extends TargetEffect
{
	public ThunderEffect(EffectTarget target, int radius) throws HeavenException
	{
		super(target, radius);
	}

	@Override
	public void doEffect(Player player) throws HeavenException
	{
		for (Entity entity : getTargetEntities(player))
			entity.getWorld().strikeLightningEffect(entity.getLocation());
	}
}
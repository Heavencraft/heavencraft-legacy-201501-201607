package fr.heavencraft.rpg.artifacts.effects;

import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import fr.heavencraft.exceptions.HeavenException;

/**
 * Effect which deals damages to near targets
 */
public class DamageEffect extends TargetEffect
{
	private final double _damage;

	public DamageEffect(EffectTarget target, int radius, double damage) throws HeavenException
	{
		super(target, radius);
		_damage = damage;
	}

	@Override
	public void doEffect(Player player) throws HeavenException
	{
		for (Entity entity : getTargetEntities(player))
			if (entity instanceof Damageable)
				((Damageable) entity).damage(_damage, player);
	}
}
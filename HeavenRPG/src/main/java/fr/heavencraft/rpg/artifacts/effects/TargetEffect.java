package fr.heavencraft.rpg.artifacts.effects;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;

import fr.heavencraft.exceptions.HeavenException;

/**
 * Effect which have targets based on player's location
 */
public abstract class TargetEffect extends Effect
{
	public enum EffectTarget
	{
		PLAYERS,
		MOBS
	}

	private final EffectTarget _target;
	private final int _radius;

	public TargetEffect(EffectTarget target, int radius) throws HeavenException
	{
		if (target == null)
			throw new HeavenException("TargetEffect : target is null.");

		_target = target;
		_radius = radius;
	}

	protected List<Entity> getTargetEntities(Player player) throws HeavenException
	{
		List<Entity> entities = player.getNearbyEntities(_radius, _radius, _radius);
		List<Entity> result = new ArrayList<Entity>();

		switch (_target)
		{
			case PLAYERS:
				for (Entity entity : entities)
					if (entity instanceof Player)
						result.add(entity);
				break;

			case MOBS:
				for (Entity entity : entities)
					if (entity instanceof Monster || entity instanceof Ghast || entity instanceof Slime)
						result.add(entity);
				break;

			default:
				throw new HeavenException("TargetEffect : invalid target : %1$s", _target);
		}

		return result;
	}
}
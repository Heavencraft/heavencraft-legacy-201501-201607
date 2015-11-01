package fr.heavencraft.rpg.artifacts.effects;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;

import fr.heavencraft.exceptions.HeavenException;
import fr.heavencraft.rpg.artifacts.effects.TargetEffect.EffectTarget;

// Java < 8 doesn't allow static method in interfaces, so we use a abtract class instead
public abstract class Effect
{
	public abstract void doEffect(Player player) throws HeavenException;

	/*
	 * Cache to avoid multiples instances of the same effect
	 */

	private static final Map<EffectType, Effect> _effectCache = new HashMap<EffectType, Effect>();

	public static Effect getEffectFromEffectType(EffectType type) throws HeavenException
	{
		Effect effect = _effectCache.get(type);

		if (effect != null)
			return effect;

		switch (type)
		{
			case SMALL_DAMAGE_MOBS_3:
				effect = new DamageEffect(EffectTarget.MOBS, 3, 2.3);
				break;

			case THUNDER_MOBS_3:
				effect = new ThunderEffect(EffectTarget.MOBS, 3);
				break;

			default:
				throw new HeavenException("Type d'effet invalide : {%1$s}", type);
		}

		_effectCache.put(type, effect);
		return effect;
	}
}
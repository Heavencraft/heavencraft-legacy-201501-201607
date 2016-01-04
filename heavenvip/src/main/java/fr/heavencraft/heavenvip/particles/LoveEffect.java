package fr.heavencraft.heavenvip.particles;

import org.bukkit.entity.Player;

import fr.heavencraft.heavencore.utils.particles.ParticleUtils;
import fr.heavencraft.heavenvip.vipeffects.EffectApplicationEventType;
import fr.heavencraft.heavenvip.vipeffects.VipEffect;

public class LoveEffect implements VipEffect
{
	@Override
	public void activate(EffectApplicationEventType effectApplicationEventType, Player player)
	{
		if(effectApplicationEventType != EffectApplicationEventType.TICK_1S)
			return;
		
		ParticleUtils.ENCHANTMENT_TABLE.display(3, 0, 0, 1, 30, player.getLocation(), 20);
	}

}

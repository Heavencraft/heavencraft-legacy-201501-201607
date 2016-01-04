package fr.heavencraft.heavenvip.vipeffects;

import org.bukkit.entity.Player;

public interface VipEffect
{
	void activate(EffectApplicationEventType effectApplicationEventType, Player player);
}

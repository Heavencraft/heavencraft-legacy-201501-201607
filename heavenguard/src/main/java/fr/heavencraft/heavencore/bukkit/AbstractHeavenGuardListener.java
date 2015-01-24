package fr.heavencraft.heavencore.bukkit;

import fr.heavencraft.heavenguard.bukkit.HeavenGuard;

public abstract class AbstractHeavenGuardListener extends AbstractBukkitListener
{
	protected AbstractHeavenGuardListener()
	{
		super(HeavenGuard.PLUGIN_NAME);
	}
}
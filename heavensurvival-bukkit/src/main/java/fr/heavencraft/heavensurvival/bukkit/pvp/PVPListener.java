package fr.heavencraft.heavensurvival.bukkit.pvp;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import fr.heavencraft.heavencore.bukkit.listeners.AbstractListener;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.utils.chat.ChatUtil;
import fr.heavencraft.heavensurvival.bukkit.BukkitHeavenSurvival;
import fr.heavencraft.heavensurvival.common.users.SurvivalUserProvider;

public class PVPListener extends AbstractListener<BukkitHeavenSurvival>
{
	public PVPListener(BukkitHeavenSurvival plugin)
	{
		super(plugin);
	}

	@EventHandler(ignoreCancelled = true)
	private void onEntityDamageByEntity(EntityDamageByEntityEvent event) throws HeavenException
	{
		final Entity damaged = event.getEntity();
		Player defender;

		if (damaged instanceof Player)
			defender = (Player) damaged;
		else
			return;

		final Entity damager = event.getDamager();
		Player attacker;

		if (damager instanceof Player)
			attacker = (Player) damager;
		else if (damager instanceof Projectile && ((Projectile) damager).getShooter() instanceof Player)
			attacker = (Player) ((Projectile) damager).getShooter();
		else
			return;

		if (!SurvivalUserProvider.get().getUserByUniqueId(attacker.getUniqueId()).isPvp())
		{
			ChatUtil.sendMessage(attacker, "Vous n'êtes pas en mode PVP.");
			event.setCancelled(true);
			return;
		}

		if (!SurvivalUserProvider.get().getUserByUniqueId(defender.getUniqueId()).isPvp())
		{
			ChatUtil.sendMessage(attacker, "{%1$s] n'est pas en mode PVP.", defender.getName());
			event.setCancelled(true);
			return;
		}
	}
}
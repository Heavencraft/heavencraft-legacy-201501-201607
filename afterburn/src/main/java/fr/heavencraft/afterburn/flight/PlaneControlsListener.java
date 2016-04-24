package fr.heavencraft.afterburn.flight;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import fr.heavencraft.afterburn.Afterburn;
import fr.heavencraft.heavencore.bukkit.HeavenPlugin;
import fr.heavencraft.heavencore.bukkit.listeners.AbstractListener;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.utils.actionbar.ActionBar;
import fr.heavencraft.heavencore.utils.chat.ChatUtil;
import fr.heavencraft.heavencore.utils.particles.ParticleEffect;

public class PlaneControlsListener extends AbstractListener<HeavenPlugin>
{

	public PlaneControlsListener(HeavenPlugin plugin)
	{
		super(plugin);
	}

	@EventHandler
	public void afterburnerClickEvent(PlayerInteractEvent event) throws HeavenException
	{
		final Player p = event.getPlayer();

		// He must be gliding
		if (!p.isGliding())
			return;
		// Click of nether star
		if (p.getInventory().getItemInMainHand().getType() != Material.NETHER_STAR)
			return;

		// Remove 1 item
		int amount = p.getInventory().getItemInMainHand().getAmount();
		if (amount == 1)
			p.getInventory().setItemInMainHand(null);
		else
			p.getInventory().getItemInMainHand().setAmount(amount - 1);

		p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 20, 10));
		p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 40, 10));
		// Wait before teleport
		new BukkitRunnable()
		{
			int n = 0;

			public void run()
			{
				p.setVelocity(p.getVelocity().multiply(1.2));
				ActionBar ab = new ActionBar(String.format("Velocity: {%1$.2f} {%2$.2f} {%3$.2f}",
						p.getVelocity().getX(), p.getVelocity().getY(), p.getVelocity().getZ()));
				ab.send(p);
				n++;
				if (n >= 10)
					cancel();
			}
		}.runTaskTimer(Afterburn.get(), 5, 3);

		event.setCancelled(true);
	}
}

package fr.heavencraft.afterburn.flight;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.WitherSkull;
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
				if(!p.isGliding())
					cancel();
				p.setVelocity(p.getVelocity().multiply(1.2));
				// norm of vector is very slow
				ActionBar ab = new ActionBar(String.format("Vitesse: %1$.2fm/s", p.getVelocity().length()));
				ab.send(p);
				n++;
				if (n >= 10)
					cancel();
			}
		}.runTaskTimer(Afterburn.get(), 5, 3);

		event.setCancelled(true);
	}
	
	@EventHandler
	public void minigunClickEvent(PlayerInteractEvent event) throws HeavenException
	{
		final Player p = event.getPlayer();

		// He must be gliding
		if (!p.isGliding())
			return;
		if (p.getInventory().getItemInMainHand().getType() != Material.STONE_BUTTON)
			return;

		// Remove 1 item
		int amount = p.getInventory().getItemInMainHand().getAmount();
		if (amount == 1)
			p.getInventory().setItemInMainHand(null);
		else
			p.getInventory().getItemInMainHand().setAmount(amount - 1);
		
		Snowball sb = (Snowball) p.launchProjectile(Snowball.class);
		sb.setVelocity(p.getVelocity().multiply(20));
		
		event.setCancelled(true);
	}
	
	@EventHandler
	public void missileClickEvent(PlayerInteractEvent event) throws HeavenException
	{
		final Player p = event.getPlayer();

		// He must be gliding
		if (!p.isGliding())
			return;
		if (p.getInventory().getItemInMainHand().getType() != Material.FIREWORK_CHARGE)
			return;

		// Remove 1 item
		int amount = p.getInventory().getItemInMainHand().getAmount();
		if (amount == 1)
			p.getInventory().setItemInMainHand(null);
		else
			p.getInventory().getItemInMainHand().setAmount(amount - 1);
		
		WitherSkull sb = (WitherSkull) p.launchProjectile(WitherSkull.class);
		System.out.println("ISSILE");
		sb.setVelocity(p.getVelocity().multiply(20));
		
		event.setCancelled(true);
	}
}

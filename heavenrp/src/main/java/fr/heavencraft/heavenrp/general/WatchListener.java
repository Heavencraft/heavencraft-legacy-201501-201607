package fr.heavencraft.heavenrp.general;

import java.util.List;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import fr.heavencraft.heavencore.bukkit.HeavenPlugin;
import fr.heavencraft.heavencore.bukkit.listeners.AbstractListener;
import fr.heavencraft.heavenrp.RPPermissions;

public class WatchListener extends AbstractListener<HeavenPlugin>
{
	public WatchListener(HeavenPlugin plugin)
	{
		super(plugin);
	}

	@EventHandler(ignoreCancelled = true)
	public void onPlayerInteract(PlayerInteractEvent event)
	{
		final Player player = event.getPlayer();

		if (!player.hasPermission(RPPermissions.WATCH))
			return;

		final ItemStack item = event.getItem();

		if (item != null && item.getType() == Material.WATCH)
		{
			final World world = player.getWorld();

			switch (event.getAction())
			{
				case LEFT_CLICK_AIR:
					world.strikeLightningEffect(player.getTargetBlock((Set<Material>) null, 120).getLocation());
					event.setCancelled(true);
					break;

				case RIGHT_CLICK_AIR:
					world.strikeLightningEffect(player.getTargetBlock((Set<Material>) null, 120).getLocation());
					event.setCancelled(true);
					break;

				case LEFT_CLICK_BLOCK:
					world.strikeLightningEffect(event.getClickedBlock().getLocation());
					event.setCancelled(true);
					break;

				case RIGHT_CLICK_BLOCK:
					world.strikeLightningEffect(event.getClickedBlock().getLocation());
					List<Entity> nearbyE = player.getNearbyEntities(1,  1, 1);
					if(nearbyE.size() >= 1 && nearbyE.get(0) instanceof LivingEntity){
						((LivingEntity)nearbyE.get(0)).damage(5.0);
					}
					event.setCancelled(true);
					break;

				default:
					break;
			}
		}
	}
}
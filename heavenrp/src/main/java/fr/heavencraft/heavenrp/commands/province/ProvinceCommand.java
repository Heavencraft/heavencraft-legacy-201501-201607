package fr.heavencraft.heavenrp.commands.province;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.heavencraft.heavencore.bukkit.commands.AbstractCommandExecutor;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.utils.DevUtil;
import fr.heavencraft.heavencore.utils.chat.ChatUtil;
import fr.heavencraft.heavenrp.HeavenRP;
import fr.heavencraft.heavenrp.RPPermissions;
import fr.heavencraft.heavenrp.provinces.ProvincesManager;
import fr.heavencraft.heavenrp.provinces.ProvincesManager.Province;

public class ProvinceCommand extends AbstractCommandExecutor
{
	private final static String PROVINCE_LIST_ENTRY = "- {%1$s} | Niveau: {%2$d} ({%3$d} Points)";
	private final static String PROVINCE_EFFECT_STATE_CHANGE = "Les effets due au niveau des provinces sont: {%1$s}.";
	private final static String PROVINCE_POINTS_UPDATED = "Le nombre de points a été mis a jour.";
	private final static String PROVINCE_POINTS_BROADCAST = "La province {%1$s}({%2$d}) a atteint {%3$d} points.";
	
	public ProvinceCommand(HeavenRP plugin)
	{
		super(plugin, "provinces");
	}

	@Override
	protected void onPlayerCommand(Player player, String[] args) throws HeavenException
	{
		// List provinces
		if(args.length == 0) {
			List<Province> provinces = ProvincesManager.getProvinces();
			for(int i = 0; i < provinces.size(); i++) {
				final int pts = provinces.get(i).getPoints();
				ChatUtil.sendMessage(player, PROVINCE_LIST_ENTRY, 
						provinces.get(i).getName(), 
						ProvincesManager.getLevel(pts),
						pts);
			}
			return;
		}
		// Authorized user, set points
		if(args[0].equalsIgnoreCase("effects")) {
			// Does the user has the permission?
			if(!player.hasPermission(RPPermissions.PROVINCE_EFFECT))
				throw new HeavenException("Vous n'avez pas la permission.");
			// Change state of effect application
			if(ProvincesManager.applyEffects()) {
				ProvincesManager.setApplyEffects(false);
				ChatUtil.sendMessage(player.getName(), PROVINCE_EFFECT_STATE_CHANGE, "désactivés");
				return;
			}
			else {
				ProvincesManager.setApplyEffects(true);
				ChatUtil.sendMessage(player.getName(), PROVINCE_EFFECT_STATE_CHANGE, "activés");
				return;
			}
		}
		// Set/add/remove points
		if(args[0].equalsIgnoreCase("set") || args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("remove")) {
			// Does the user has the permission?
			if(!player.hasPermission(RPPermissions.PROVINCE_POINTS))
				throw new HeavenException("Vous n'avez pas la permission.");
			// Do we have the right amount of args?
			if(args.length != 3)
				throw new HeavenException("Commande Invalide");
			// Parse args
			Province p = ProvincesManager.getProvinceByName(args[1]);
			// Check if province != null
			if(p == null) 
				throw new HeavenException("Province Invalide");
			// Execute
			int amount = p.getPoints();
			int newPoints = amount;
			int requestedPoints = DevUtil.toUint(args[2]);
			// Set to new point count
			if(args[0].equalsIgnoreCase("set"))
				newPoints = requestedPoints;
			// Add new amount to existing amount
			if(args[0].equalsIgnoreCase("add"))
				newPoints = amount + requestedPoints;
			// Remove some points
			if(args[0].equalsIgnoreCase("remove"))
				if((amount - requestedPoints) > 0)
					newPoints = amount - requestedPoints;
				else 
					newPoints = 0;
			// Update points
			ProvincesManager.setPoints(p, newPoints);
			// Inform player
			ChatUtil.sendMessage(player, PROVINCE_POINTS_UPDATED);
			// If it is %10 OR amount is over 10 pts  than newPoints
			if(((newPoints % 10 == 0) || ((amount + 10) <= newPoints)) && newPoints != amount)
					ChatUtil.sendMessage(player, PROVINCE_POINTS_BROADCAST,
							p.getName(), ProvincesManager.getLevel(newPoints), newPoints);
			return;
		}	
		
		sendUsage(player);
	}

	@Override
	protected void onConsoleCommand(CommandSender sender, String[] args) throws HeavenException
	{
		throw new HeavenException("Commande interdite depuis la console.");
	}

	@Override
	protected void sendUsage(CommandSender sender)
	{
		ChatUtil.sendMessage(sender, "/{provinces} | Liste les provinces et leurs niveaux.");
		ChatUtil.sendMessage(sender, "/{provinces} effects | Active où désactive l'application des effets.");
		ChatUtil.sendMessage(sender, "/{provinces} <set/add/remove> <province> <points> | Met a jour le nombre de points d'une province.");
	}

}

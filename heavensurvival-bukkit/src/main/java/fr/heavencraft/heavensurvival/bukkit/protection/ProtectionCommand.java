package fr.heavencraft.heavensurvival.bukkit.protection;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.heavencraft.heavencore.bukkit.commands.AbstractCommandExecutor;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.utils.chat.ChatUtil;
import fr.heavencraft.heavenguard.api.Region;
import fr.heavencraft.heavenguard.api.RegionProvider;
import fr.heavencraft.heavenguard.common.HeavenGuardInstance;
import fr.heavencraft.heavensurvival.bukkit.BukkitHeavenSurvival;
import fr.heavencraft.heavensurvival.bukkit.teleport.NotEnoughNuggetsException;
import fr.heavencraft.heavensurvival.bukkit.teleport.NotOwnerException;
import fr.heavencraft.heavensurvival.bukkit.worlds.WorldsManager;
import fr.heavencraft.heavensurvival.common.users.SurvivalUser;
import fr.heavencraft.heavensurvival.common.users.SurvivalUserProvider;

public class ProtectionCommand extends AbstractCommandExecutor
{
	public ProtectionCommand(BukkitHeavenSurvival plugin)
	{
		super(plugin, "protection");
	}

	private static RegionProvider getRegionProvider()
	{
		return HeavenGuardInstance.get().getRegionProvider();
	}

	private static void checkRegionOwnership(Region region, Player player) throws NotOwnerException
	{
		if (!region.isMember(player.getUniqueId(), true))
			throw new NotOwnerException(region);
	}

	@Override
	protected void onPlayerCommand(Player player, String[] args) throws HeavenException
	{
		if (!player.getWorld().equals(WorldsManager.get().getWorld()))
		{
			ChatUtil.sendMessage(player, "Cette commande n'est pas accessible dans ce monde.");
			return;
		}

		if (args.length == 0)
		{
			sendUsage(player);
			return;
		}

		final String playerName = player.getName();

		if (args[0].equalsIgnoreCase("creer"))
		{
			SelectionManager.enable(player);
			ChatUtil.sendMessage(player, "Bienvenue dans l'assistant de protection.");
			ChatUtil.sendMessage(player,
					"Utilisez le clic gauche et le clic droit avec un bâton pour délimiter votre protection");
			ChatUtil.sendMessage(player, "Puis faites /protection valider ou /protection annuler");
		}

		else if (args[0].equalsIgnoreCase("annuler"))
		{
			SelectionManager.disable(player);
			ChatUtil.sendMessage(player, "Création de la protection annulée.");
		}

		else if (args[0].equalsIgnoreCase("valider"))
		{
			final Selection selection = SelectionManager.getSelection(player);

			if (hasCollision(selection))
				throw new HeavenException("Une protection existe déjà ici.");

			final int amount = selection.getPrice();

			if (!MoneyManager.hasEnough(player, amount))
				throw new NotEnoughNuggetsException(amount);

			final String regionName = createRegionName(player);
			final Region region = getRegionProvider().createRegion(regionName, WorldsManager.get().getWorld().getName(), //
					selection.getMinX(), 0, selection.getMinZ(), //
					selection.getMaxX(), 0xFF, selection.getMaxZ());

			region.addMember(player.getUniqueId(), true);

			MoneyManager.pay(player, amount);

			SelectionManager.disable(player);
			ChatUtil.sendMessage(player, "Votre protection a été créée.");
			ChatUtil.sendMessage(player,
					"Vous pouvez utiliser /protection ajouter pour ajouter des membres à votre protection.");
		}

		else if (args[0].equalsIgnoreCase("supprimer") && args.length == 2)
		{
			final String regionName = args[1];

			final Region region = getRegionProvider().getRegionByName(regionName);

			checkRegionOwnership(region, player);
			getRegionProvider().deleteRegion(regionName);

			ChatUtil.sendMessage(player, "La protection {" + regionName + "} a été supprimée.");
		}

		else if (args[0].equalsIgnoreCase("ajouter") && args.length == 3)
		{
			final Region region = getRegionProvider().getRegionByName(args[1]);
			final SurvivalUser user = SurvivalUserProvider.get().getUserByName(args[2]);

			checkRegionOwnership(region, player);

			region.addMember(user.getUniqueId(), false);

			ChatUtil.sendMessage(player, "Le joueur {%1$s} est désormais membre de la protection {%2$s}.", user,
					region);
		}

		else if (args[0].equalsIgnoreCase("enlever") && args.length == 3)
		{
			final Region region = getRegionProvider().getRegionByName(args[1]);
			final SurvivalUser user = SurvivalUserProvider.get().getUserByName(args[2]);

			checkRegionOwnership(region, player);

			region.removeMember(user.getUniqueId(), false);

			ChatUtil.sendMessage(player, "Le joueur {%1$s} n'est plus membre de la protection {%2$s}.", user, region);
		}

		else
			sendUsage(player);
	}

	@Override
	protected void onConsoleCommand(CommandSender sender, String[] args) throws HeavenException
	{
		ChatUtil.sendMessage(sender, "Cette commande n'est pas utilisable depuis la console.");
	}

	@Override
	protected void sendUsage(CommandSender sender)
	{
		ChatUtil.sendMessage(sender, "/{protection} creer : lance l'assistant de protection.");
		ChatUtil.sendMessage(sender, "/{protection} valider : valider la création d'une protection.");
		ChatUtil.sendMessage(sender, "/{protection} annuler : annule la création d'une protection");
		ChatUtil.sendMessage(sender, "/{protection} ajouter <protection> <joueur> : ajoute un joueur à la protection.");
		ChatUtil.sendMessage(sender,
				"/{protection} enlever <protection> <joueur> : enlève un joueur de la protection.");
		ChatUtil.sendMessage(sender, "/{protection} supprimer <protection> : supprime la protection.");
	}

	private static boolean hasCollision(Selection selection)
	{
		for (final Region region : getRegionProvider().getRegionsInWorld("world"))
			if (selection.hasCollision(region))
				return true;

		return false;

	}

	private static String createRegionName(Player player)
	{
		int i = 1;
		final String playerName = player.getName();
		String regionName = null;

		while (getRegionProvider().regionExists(regionName = playerName + '_' + i++))
			;

		return regionName;
	}
}
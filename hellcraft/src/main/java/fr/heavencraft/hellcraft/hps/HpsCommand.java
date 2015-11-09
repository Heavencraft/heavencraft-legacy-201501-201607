package fr.heavencraft.hellcraft.hps;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.heavencraft.heavencore.CorePermissions;
import fr.heavencraft.heavencore.bukkit.commands.AbstractCommandExecutor;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.utils.chat.ChatUtil;
import fr.heavencraft.heavencore.utils.player.PlayerUtil;
import fr.heavencraft.hellcraft.HellCraft;
import fr.heavencraft.hellcraft.HellCraftPermissions;
import fr.heavencraft.hellcraft.back.BackListener;
import fr.heavencraft.hellcraft.worlds.WorldsManager;

public class HpsCommand extends AbstractCommandExecutor
{

	private static final String FORMAT_WP = "§4[§6%1$s§4] §6%2$s";

	private final HellCraft plugin;

	public HpsCommand(HellCraft plugin)
	{
		super(plugin, "hps");

		this.plugin = plugin;
	}

	private static void addPermission(Player player, String perm)
	{
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "pex user " + player.getName() + " add " + perm);
	}

	@Override
	protected void onPlayerCommand(Player player, String[] args) throws HeavenException
	{
		if (args.length != 0)
		{
			switch (args[0].toLowerCase())
			{
				case "l":
				case "liste":
					liste(player);
					return;

				case "info":
					if (args.length >= 2)
						info(player, args[1]);
					return;

				case "back":
					back(player);
					return;

				case "heal":
					heal(player);
					return;

				case "diam":
					diam(player);
					return;

				case "vip":
					vip(player);
					return;

				case "reduc":
					reduc(player);
					return;

				default:
					break;
			}
		}

		ChatUtil.sendMessage(player, FORMAT_WP, "HPS §aINFO",
				"Votre solde: §2" + plugin.getHpsManager().getBalance(player.getName()));
		ChatUtil.sendMessage(player, FORMAT_WP, "HPS §aINFO", "Faites /hps liste pour une liste des produits.");
		return;
	}

	private void giveDiamondStuff(Player p)
	{
		final ItemStack[] items = { new ItemStack(Material.DIAMOND_HELMET, 1),
				new ItemStack(Material.DIAMOND_CHESTPLATE, 1), new ItemStack(Material.DIAMOND_LEGGINGS, 1),
				new ItemStack(Material.DIAMOND_BOOTS, 1) };

		p.getInventory().addItem(items);

		final ItemStack Epee = new ItemStack(Material.DIAMOND_SWORD);
		Epee.addUnsafeEnchantment(Enchantment.KNOCKBACK, 2);
		Epee.addUnsafeEnchantment(Enchantment.FIRE_ASPECT, 1);
		Epee.addUnsafeEnchantment(Enchantment.DAMAGE_UNDEAD, 2);
		p.getInventory().addItem(new ItemStack[] { Epee });

		final ItemStack Pomme = new ItemStack(Material.GOLDEN_APPLE);
		Pomme.setDurability((short) 1);
		p.getInventory().addItem(new ItemStack[] { Pomme });
	}

	@Override
	protected void onConsoleCommand(CommandSender sender, String[] args)
	{
		ChatUtil.sendMessage(sender, "Cette commande ne peut pas être utilisée depuis la {console}.");
	}

	@Override
	protected void sendUsage(CommandSender sender)
	{
	}

	private void liste(CommandSender sender)
	{
		ChatUtil.sendMessage(sender, FORMAT_WP, "HPS", "┌───────── Boutique HPS ─────────┐");
		ChatUtil.sendMessage(sender, FORMAT_WP, "HPS", " ─────── Avantages Ponctuels ─────── ");
		ChatUtil.sendMessage(sender, FORMAT_WP, "HPS", " - Restaure la vie, {5 HPS}: {/hps heal}");
		ChatUtil.sendMessage(sender, FORMAT_WP, "HPS", " - Retour à la dernière position, {5 HPS}: {/hps back}");
		ChatUtil.sendMessage(sender, FORMAT_WP, "HPS", " ─────── Avantages permanent ─────── ");
		ChatUtil.sendMessage(sender, FORMAT_WP, "HPS", " - Equipement Diamant, {15 HPS}: {/hps diam}");
		ChatUtil.sendMessage(sender, FORMAT_WP, "HPS",
				" - Carte de réduction 33% boutique, {25 HPS}: {/hps reduc}");
		ChatUtil.sendMessage(sender, FORMAT_WP, "HPS", " - VIP (Téléportation, -33%, etc.) {50HPS}: {/hps vip}");

		ChatUtil.sendMessage(sender, FORMAT_WP, "HPS",
				"Note: pour avoir plus d'information sur un kit: {/hps into <kit>}");
		ChatUtil.sendMessage(sender, "");
	}

	private void info(CommandSender sender, String kit)
	{
		switch (kit.toLowerCase())
		{
			case "back":
				ChatUtil.sendMessage(sender, FORMAT_WP, "HPS",
						"Le /hps back vous permet de retourner au dernier point avant votre décès.");
				ChatUtil.sendMessage(sender, FORMAT_WP, "HPS", "La commande est risquée, soyez prudant.");
				ChatUtil.sendMessage(sender, FORMAT_WP, "HPS", "Prix: 5HPS.");
				break;

			case "heal":
				ChatUtil.sendMessage(sender, FORMAT_WP, "HPS", "Le /hps heal restaure votre vie et votre faim.");
				ChatUtil.sendMessage(sender, FORMAT_WP, "HPS", "Prix: 5HPS.");
				break;

			case "diam":
				ChatUtil.sendMessage(sender, FORMAT_WP, "HPS",
						"Le /hps diam vous offre un équipement de combat a base de diamant.");
				ChatUtil.sendMessage(sender, FORMAT_WP, "HPS", "Prix: 33HPS.");
				ChatUtil.sendMessage(sender, FORMAT_WP, "HPS",
						"Assurez vous d'avoir de la place dans votre inventaire.");
				break;

			case "vip":
				ChatUtil.sendMessage(sender, FORMAT_WP, "HPS",
						"Le VIP est un ensemble de commandes et d'avantages stratégiques pour simplifier votre vie.");
				ChatUtil.sendMessage(sender, FORMAT_WP, "HPS",
						"Le TPA (/rejoindre <joueur>) permet de se téléporter a un joueur.");
				ChatUtil.sendMessage(sender, FORMAT_WP, "HPS",
						"Le PTIME (/ptime <day|night>) permet de mettre le jour ou la nuit.");
				ChatUtil.sendMessage(sender, FORMAT_WP, "HPS",
						"Le /spawn permet de retourner au spawn à tout moment.");
				// ChatUtil.sendMessage(sender, FORMAT_WP, "HPS",
				// "Le compass (/compass) permet de connaitre la direction dans
				// laquelle nous marchons.");
				// ChatUtil.sendMessage(sender, FORMAT_WP, "HPS",
				// "Le getpos (/getpos) permet de savoir ces positions.");
				ChatUtil.sendMessage(sender, FORMAT_WP, "HPS", "Une réduction 33% sur toute la boutique!");
				ChatUtil.sendMessage(sender, FORMAT_WP, "HPS", "Un équipement en diamant.");
				ChatUtil.sendMessage(sender, FORMAT_WP, "HPS", "Prix: 50HPS.");
				break;

			case "reduc":
				ChatUtil.sendMessage(sender, FORMAT_WP, "HPS",
						"Il s'agit d'une réduction 33% sur toute la boutique et sur tout les achats!");
				ChatUtil.sendMessage(sender, FORMAT_WP, "HPS", "Prix: 25HPS.");
				ChatUtil.sendMessage(sender, FORMAT_WP, "HPS", "Ne s'accumule pas avec la réduction VIP.");
				break;
		}
	}

	private void back(Player player) throws HeavenException
	{
		if (!WorldsManager.getWorldSpawn().equals(player.getWorld()))
			throw new HeavenException("Vous devez être au spawn pour utiliser cette commande.");

		final Location deathLocation = BackListener.getDeathLocation(player.getName());

		if (deathLocation == null)
			throw new HeavenException("Vous devez être mort au moins une fois pour utiliser cette commande.");

		plugin.getHpsManager().removeBalance(player.getName(), 5);
		ChatUtil.sendMessage(player, FORMAT_WP, "HPS Confirmation", " Vous avez acheté le /back, 5HPS débités.");

		PlayerUtil.teleportPlayer(player, deathLocation,
				"Vous avez été téléporté à l'endroit où vous étiez mort.");
	}

	private void heal(Player player) throws HeavenException
	{
		plugin.getHpsManager().removeBalance(player.getName(), 5);

		player.setFoodLevel(20);
		player.setHealth(player.getMaxHealth());
		player.setFireTicks(0);
		ChatUtil.sendMessage(player, FORMAT_WP, "HPS §2Confirmation", "Vous avez été soigné, 5HPS débités.");
	}

	private void diam(Player player) throws HeavenException
	{
		if (!player.hasPermission(HellCraftPermissions.DIAMOND_GRADE))
		{
			plugin.getHpsManager().removeBalance(player.getName(), 33);
			giveDiamondStuff(player);
			addPermission(player, HellCraftPermissions.DIAMOND_GRADE);
			ChatUtil.sendMessage(player, FORMAT_WP, "HPS §2Confirmation",
					"Vous avez recu votre equipement, 33HPS débités.");
		}
		else
		{
			ChatUtil.sendMessage(player, FORMAT_WP, "HPS", " Vous possedez déjà cet avantage.");
		}
	}

	private void vip(Player player) throws HeavenException
	{
		if (!player.hasPermission(HellCraftPermissions.VIP_GRADE))
		{
			plugin.getHpsManager().removeBalance(player.getName(), 50);
			giveDiamondStuff(player);
			addPermission(player, CorePermissions.REJOINDRE_COMMAND);
			addPermission(player, HellCraftPermissions.PTIME_COMMAND);
			addPermission(player, HellCraftPermissions.SPAWN_COMMAND);
			addPermission(player, "BossShop.PriceMultiplier.Money1");
			addPermission(player, HellCraftPermissions.VIP_GRADE);
			player.setFoodLevel(20);
			player.setHealth(player.getMaxHealth());
			player.setFireTicks(0);
			ChatUtil.sendMessage(player, FORMAT_WP, "HPS §2Confirmation",
					" Vous avez acheté le kit VIP, 50 HPS débités.");
		}
		else
		{
			ChatUtil.sendMessage(player, FORMAT_WP, "HPS", " Vous possedez déjà cet avantage.");
		}
	}

	private void reduc(Player player) throws HeavenException
	{
		if (!player.hasPermission("BossShop.PriceMultiplier.Money1"))
		{
			plugin.getHpsManager().removeBalance(player.getName(), 25);
			addPermission(player, "BossShop.PriceMultiplier.Money1");
			ChatUtil.sendMessage(player, FORMAT_WP, "HPS §2Confirmation",
					" Vous avez acheté la carte de réduction, 25 HPS débités.");
		}
		else
		{
			ChatUtil.sendMessage(player, FORMAT_WP, "HPS", " Vous possedez déjà cet avantage.");
		}
	}
}
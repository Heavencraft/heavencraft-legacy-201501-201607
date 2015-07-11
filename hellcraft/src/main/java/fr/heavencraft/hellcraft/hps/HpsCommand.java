package fr.heavencraft.hellcraft.hps;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.heavencraft.heavencore.bukkit.commands.AbstractCommandExecutor;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.hellcraft.HellCraft;

public class HpsCommand extends AbstractCommandExecutor
{
	private static final String FORMAT_WP = "§4[§6%1$s§4] §6%2$s";

	private final HellCraft plugin;

	public HpsCommand(HellCraft plugin)
	{
		super(plugin, "hps");

		this.plugin = plugin;
	}

	@Override
	protected void onPlayerCommand(Player player, String[] args) throws HeavenException
	{
		if (args.length == 0)
		{
			plugin.sendMessage(player, FORMAT_WP, "HPS §aINFO", "Votre solde: §2"
					+ plugin.getHpsManager().getBalance(player.getName()));
			plugin.sendMessage(player, FORMAT_WP, "HPS §aINFO", "Faites /hps liste pour une liste des produits.");
			return;
		}

		if ((args[0].equalsIgnoreCase("liste")) || (args[0].equalsIgnoreCase("l")))
		{
			plugin.sendMessage(player, FORMAT_WP, "HPS", "┌───────── Boutique HPS ─────────┐");
			plugin.sendMessage(player, FORMAT_WP, "HPS", " ─────── Avantages Ponctuels ─────── ");
			plugin.sendMessage(player, FORMAT_WP, "HPS", " - Restaure la vie, §55 HPS§6: §c/hps heal");
			plugin.sendMessage(player, FORMAT_WP, "HPS",
					" - Retour a la dernière position, §55 HPS§6: §c/hps back");
			plugin.sendMessage(player, FORMAT_WP, "HPS", " - Equipement Diamant, §515 HPS§6: §c/hps diam");
			plugin.sendMessage(player, FORMAT_WP, "HPS", " ─────── Avantages permissions ─────── ");
			plugin.sendMessage(player, FORMAT_WP, "HPS",
					" - Carte de réduction 33% boutique, §525 HPS§6: §c/hps reduc");
			plugin.sendMessage(player, FORMAT_WP, "HPS", " - VIP, §550HPS§6: §c/hps vip");

			plugin.sendMessage(player, FORMAT_WP, "HPS",
					"Note: pour avoir plus d'information sur un kit: §c/hps <kit> info");
			player.sendMessage("");
		}
		else if (args[0].equalsIgnoreCase("back"))
		{
			if (args.length >= 2)
			{
				plugin.sendMessage(player, FORMAT_WP, "HPS",
						"Le /hps back vous permet de retourner au dernier point avant votre décès.");
				plugin.sendMessage(player, FORMAT_WP, "HPS", "La commande est risquée, soyez prudant.");
				plugin.sendMessage(player, FORMAT_WP, "HPS", "Prix: 5HPS.");
			}
			else
			{
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "pex user " + player.getName()
						+ " add essentials.back");
				Bukkit.dispatchCommand(player, "back");

				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "pex user " + player.getName()
						+ " remove essentials.back");
				final Player tmpPlayer = player;

				Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable()
				{
					public void run()
					{
						if (tmpPlayer.getLocation().getWorld().getName().equalsIgnoreCase("city"))
							try
							{
								plugin.getHpsManager().removeBalance(tmpPlayer.getName(), 5);
								tmpPlayer.sendMessage(String.format(FORMAT_WP, new Object[]
								{ "HPS §2Confirmation", " Vous avez acheté le /back, 5HPS débités." }));
							}
							catch (final HeavenException e)
							{
								plugin.sendMessage(tmpPlayer, FORMAT_WP, "HPS §2Notification",
										" Vous n'avez pas été débités, car le /back ne vous a pas téléporté sur le bon monde.");
							}
					}
				}, 60L);
			}

		}
		else if (args[0].equalsIgnoreCase("heal"))
		{
			if (args.length >= 2)
			{
				plugin.sendMessage(player, FORMAT_WP, "HPS", "Le /hps heal restaure votre vie et votre faim.");
				plugin.sendMessage(player, FORMAT_WP, "HPS", "Prix: 5HPS.");
			}
			else
			{
				plugin.getHpsManager().removeBalance(player.getName(), 5);
				player.setFoodLevel(20);
				player.setHealth(player.getMaxHealth());
				player.setFireTicks(0);
				plugin.sendMessage(player, FORMAT_WP, "HPS §2Confirmation", "Vous avez été soigné, 5HPS débités.");
			}
		}
		else if (args[0].equalsIgnoreCase("diam"))
		{
			if (args.length >= 2)
			{
				plugin.sendMessage(player, FORMAT_WP, "HPS",
						"Le /hps diam vous offre un équipement de combat a base de diamant.");
				plugin.sendMessage(player, FORMAT_WP, "HPS", "Prix: 33HPS.");
				plugin.sendMessage(player, FORMAT_WP, "HPS",
						"Assurez vous d'avoir de la place dans votre inventaire.");
			}
			else
			{
				plugin.getHpsManager().removeBalance(player.getName(), 33);
				giveDiamondStuff(player);
				plugin.sendMessage(player, FORMAT_WP, "HPS §2Confirmation",
						"Vous avez recu votre equipement, 33HPS débités.");
			}
		}
		else if (args[0].equalsIgnoreCase("vip"))
		{
			if (args.length >= 2)
			{
				plugin.sendMessage(player, FORMAT_WP, "HPS",
						"Le VIP est un ensemble de commandes et d'avantages stratégiques pour simplifier votre vie.");
				plugin.sendMessage(player, FORMAT_WP, "HPS",
						"Le TPA (/tpa <joueur>) permet de se téléporter a un joueur.");
				plugin.sendMessage(player, FORMAT_WP, "HPS",
						"Le PTIME (/ptime <day|night>) permet de mettre le jour ou la nuit.");
				plugin.sendMessage(player, FORMAT_WP, "HPS",
						"Le compass (/compass) permet de connaitre la direction dans laquelle nous marchons.");
				plugin.sendMessage(player, FORMAT_WP, "HPS",
						"Le getpos (/getpos) permet de savoir ces positions.");
				plugin.sendMessage(player, FORMAT_WP, "HPS", "Une réduction 33% sur toute la boutique!");
				plugin.sendMessage(player, FORMAT_WP, "HPS", "Un équipement en diamant.");
				plugin.sendMessage(player, FORMAT_WP, "HPS", "Prix: 50HPS.");
			}
			else if (!player.hasPermission("essentials.compass"))
			{
				plugin.getHpsManager().removeBalance(player.getName(), 50);
				giveDiamondStuff(player);
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "pex user " + player.getName()
						+ " add essentials.tpa");
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "pex user " + player.getName()
						+ " add essentials.ptime");
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "pex user " + player.getName()
						+ " add essentials.compass");
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "pex user " + player.getName()
						+ " add essentials.getpos");
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "pex user " + player.getName()
						+ " add BossShop.PriceMultiplier.Money1");
				plugin.sendMessage(player, FORMAT_WP, "HPS §2Confirmation",
						" Vous avez acheté le kit VIP, 50 HPS débités.");
			}
			else
			{
				plugin.sendMessage(player, FORMAT_WP, "HPS", " Vous ètes déjà VIP.");
			}

		}
		else if (args[0].equalsIgnoreCase("reduc"))
		{
			if (args.length >= 2)
			{
				plugin.sendMessage(player, FORMAT_WP, "HPS",
						"Il s'agit d'une réduction 33% sur toute la boutique et sur tout les achats!");
				plugin.sendMessage(player, FORMAT_WP, "HPS", "Prix: 25HPS.");
				plugin.sendMessage(player, FORMAT_WP, "HPS", "Ne s'accumule pas avec la réduction VIP.");
			}
			else if (!player.hasPermission("BossShop.PriceMultiplier.Money1"))
			{
				plugin.getHpsManager().removeBalance(player.getName(), 25);
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "pex user " + player.getName()
						+ " add BossShop.PriceMultiplier.Money1");
				plugin.sendMessage(player, FORMAT_WP, "HPS §2Confirmation",
						" Vous avez acheté la carte de réduction, 25 HPS débités.");
			}
			else
			{
				plugin.sendMessage(player, FORMAT_WP, "HPS", " Vous possedez déjà cet avantage.");
			}

		}
		else
		{
			plugin.sendMessage(player, FORMAT_WP, "HPS", "Argument inconnu, faites /hps liste.");
		}
	}

	private void giveDiamondStuff(Player p)
	{
		final ItemStack[] items =
		{ new ItemStack(Material.DIAMOND_HELMET, 1), new ItemStack(Material.DIAMOND_CHESTPLATE, 1),
				new ItemStack(Material.DIAMOND_LEGGINGS, 1), new ItemStack(Material.DIAMOND_BOOTS, 1) };

		p.getInventory().addItem(items);

		final ItemStack Epee = new ItemStack(Material.DIAMOND_SWORD);
		Epee.addUnsafeEnchantment(Enchantment.KNOCKBACK, 2);
		Epee.addUnsafeEnchantment(Enchantment.FIRE_ASPECT, 1);
		Epee.addUnsafeEnchantment(Enchantment.DAMAGE_UNDEAD, 2);
		p.getInventory().addItem(new ItemStack[]
		{ Epee });

		final ItemStack Pomme = new ItemStack(Material.GOLDEN_APPLE);
		Pomme.setDurability((short) 1);
		p.getInventory().addItem(new ItemStack[]
		{ Pomme });
	}

	@Override
	protected void onConsoleCommand(CommandSender sender, String[] args)
	{
		plugin.sendMessage(sender, "Cette commande ne peut pas être utilisée depuis la {console}.");
	}

	@Override
	protected void sendUsage(CommandSender sender)
	{
	}
}
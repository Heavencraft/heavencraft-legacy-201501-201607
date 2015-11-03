package fr.heavencraft.heavenrp.commands.economy;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.heavencraft.heavencore.bukkit.commands.AbstractCommandExecutor;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.utils.chat.ChatUtil;
import fr.heavencraft.heavenrp.HeavenRP;
import fr.heavencraft.heavenrp.RPPermissions;
import fr.heavencraft.heavenrp.database.bankaccounts.BankAccountType;
import fr.heavencraft.heavenrp.database.bankaccounts.BankAccountsManager;

public class LivretproCommand extends AbstractCommandExecutor
{
	public LivretproCommand(HeavenRP plugin)
	{
		super(plugin, "livretpro", RPPermissions.LIVRETPRO);
	}

	@Override
	protected void onPlayerCommand(Player player, String[] args) throws HeavenException
	{
		onConsoleCommand(player, args);
	}

	@Override
	protected void onConsoleCommand(CommandSender sender, String[] args) throws HeavenException
	{
		switch (args.length)
		{
			case 2:
				if (args[0].equalsIgnoreCase("+ville"))
				{
					BankAccountsManager.createBankAccount(args[1], BankAccountType.TOWN);
					ChatUtil.sendMessage(sender, "Le livret pro de la ville {%1$s} a bien été créé", args[1]);
				}
				break;

			default:
				sendUsage(sender);
				break;
		}
	}

	@Override
	protected void sendUsage(CommandSender sender)
	{
		ChatUtil.sendMessage(sender, "/{livretpro} +ville <nom de la ville>");
		ChatUtil.sendMessage(sender, "/{livretpro} -ville <nom de la ville>");
	}
}
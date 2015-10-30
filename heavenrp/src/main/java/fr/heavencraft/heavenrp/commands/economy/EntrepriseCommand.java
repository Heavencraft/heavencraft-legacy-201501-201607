package fr.heavencraft.heavenrp.commands.economy;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.heavencraft.heavencore.bukkit.commands.AbstractCommandExecutor;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.utils.PlayerUtil;
import fr.heavencraft.heavenrp.HeavenRP;
import fr.heavencraft.heavenrp.RPPermissions;
import fr.heavencraft.heavenrp.database.users.UserProvider;
import fr.heavencraft.heavenrp.economy.enterprise.EnterprisesManager;
import fr.heavencraft.heavenrp.economy.enterprise.EnterprisesManager.Enterprise;
import fr.heavencraft.heavenrp.exceptions.NotEnterpriseOwnerException;

public class EntrepriseCommand extends AbstractCommandExecutor
{
	public EntrepriseCommand(HeavenRP plugin)
	{
		super(plugin, "entreprise");
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
				// /entreprise <nom de l'entreprise> info
				if (args[1].equalsIgnoreCase("info"))
					info(sender, args[0]);

				// /entreprise <nom de l'entreprise> supprimer
				else if (args[1].equalsIgnoreCase("supprimer") && sender.hasPermission(RPPermissions.ENTREPRISE))
					delete(sender, args[0]);

				else
					sendUsage(sender);
				break;
			case 3:
				// /entreprise <nom de l'entreprise> créer <nom du joueur>
				if (args[1].equalsIgnoreCase("creer") && sender.hasPermission(RPPermissions.ENTREPRISE))
					create(sender, args[0], PlayerUtil.getExactName(args[2]));

				// /entreprise <nom de l'entreprise> +propriétaire <nom du
				// joueur>
				else if (args[1].equalsIgnoreCase("+proprietaire"))
					addOwner(sender, args[0], PlayerUtil.getExactName(args[2]));

				// /entreprise <nom de l'entreprise> -propriétaire <nom du
				// joueur>
				else if (args[1].equalsIgnoreCase("-proprietaire"))
					removeOwner(sender, args[0], PlayerUtil.getExactName(args[2]));

				// /entreprise <nom de l'entreprise> +membre <nom du joueur>
				else if (args[1].equalsIgnoreCase("+membre"))
					addMember(sender, args[0], PlayerUtil.getExactName(args[2]));

				// /entreprise <nom de l'entreprise> -membre <nom du joueur>
				else if (args[1].equalsIgnoreCase("-membre"))
					removeMember(sender, args[0], PlayerUtil.getExactName(args[2]));
				else
					sendUsage(sender);
				break;

			default:
				sendUsage(sender);
				break;

		}
	}

	@Override
	protected void sendUsage(CommandSender sender)
	{
		if (sender.hasPermission(RPPermissions.ENTREPRISE))
		{
			ChatUtil.sendMessage(sender, "/{entreprise} <nom de l'entreprise> creer <nom du propriétaire>");
			ChatUtil.sendMessage(sender, "/{entreprise} <nom de l'entreprise> supprimer");
		}

		ChatUtil.sendMessage(sender, "/{entreprise} <nom de l'entreprise> info");
		ChatUtil.sendMessage(sender, "/{entreprise} <nom de l'entreprise> +proprietaire <nom du membre>");
		ChatUtil.sendMessage(sender, "/{entreprise} <nom de l'entreprise> -proprietaire <nom du membre>");
		ChatUtil.sendMessage(sender, "/{entreprise} <nom de l'entreprise> +membre <nom du membre>");
		ChatUtil.sendMessage(sender, "/{entreprise} <nom de l'entreprise> -membre <nom du membre>");
	}

	private static void create(CommandSender sender, String name, String owner) throws HeavenException
	{
		if (!sender.hasPermission(RPPermissions.ENTREPRISE))
			return;

		EnterprisesManager.createEnterprise(name);
		Enterprise enterprise = EnterprisesManager.getEnterpriseByName(name);

		enterprise.addMember(UserProvider.getUserByName(owner), true);

		ChatUtil.sendMessage(sender, "L'entreprise {%1$s} a été crée.", name);
	}

	private static void delete(CommandSender sender, String name) throws HeavenException
	{
		if (!sender.hasPermission(RPPermissions.ENTREPRISE))
			return;

		EnterprisesManager.deleteEnterprise(name);
		ChatUtil.sendMessage(sender, "L'entreprise {%1$s} vient d'être supprimée.", name);
	}

	private static void info(CommandSender sender, String name) throws HeavenException
	{
		Enterprise enterprise = EnterprisesManager.getEnterpriseByName(name);
		String owners = "", members = "";

		for (String owner : enterprise.getMembers(true))
			owners += (owners == "" ? "" : ", ") + owner;

		for (String member : enterprise.getMembers(false))
			members += (members == "" ? "" : ", ") + member;

		ChatUtil.sendMessage(sender, "Entreprise {%1$s}", enterprise.getName());
		ChatUtil.sendMessage(sender, "Propriétaires : %1$s", owners);
		ChatUtil.sendMessage(sender, "Membres : %1$s", members);
	}

	private static void addOwner(CommandSender sender, String name, String owner) throws HeavenException
	{
		Enterprise enterprise = EnterprisesManager.getEnterpriseByName(name);

		if (!enterprise.isMember(sender.getName(), true))
			throw new NotEnterpriseOwnerException(name);

		enterprise.addMember(UserProvider.getUserByName(owner), true);
		ChatUtil.sendMessage(sender, "{%1$s} est désormais propriétaire de l'entreprise {%2$s}.", owner, name);
	}

	private static void removeOwner(CommandSender sender, String name, String owner) throws HeavenException
	{
		Enterprise enterprise = EnterprisesManager.getEnterpriseByName(name);

		if (!enterprise.isMember(sender.getName(), true))
			throw new NotEnterpriseOwnerException(name);

		enterprise.removeMember(UserProvider.getUserByName(owner));
		ChatUtil.sendMessage(sender, "{%1$s} n'est désormais plus propriétaire de l'entreprise {%2$s}.", owner,
				name);
	}

	private static void addMember(CommandSender sender, String name, String member) throws HeavenException
	{
		Enterprise enterprise = EnterprisesManager.getEnterpriseByName(name);

		if (!enterprise.isMember(sender.getName(), true))
			throw new NotEnterpriseOwnerException(name);

		enterprise.addMember(UserProvider.getUserByName(member), false);
		ChatUtil.sendMessage(sender, "{%1$s} est désormais membre de l'entreprise {%2$s}.", member, name);
	}

	private static void removeMember(CommandSender sender, String name, String member) throws HeavenException
	{
		Enterprise enterprise = EnterprisesManager.getEnterpriseByName(name);

		if (!enterprise.isMember(sender.getName(), true))
			throw new NotEnterpriseOwnerException(name);

		enterprise.removeMember(UserProvider.getUserByName(member));
		ChatUtil.sendMessage(sender, "{%1$s} n'est désormais plus membre de l'entreprise {%2$s}.", member, name);
	}
}
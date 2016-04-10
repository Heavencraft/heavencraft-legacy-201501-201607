package fr.heavencraft.heavenrp.scrolls;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.heavencraft.heavencore.bukkit.commands.AbstractCommandExecutor;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.utils.DevUtil;
import fr.heavencraft.heavencore.utils.chat.ChatUtil;
import fr.heavencraft.heavenrp.HeavenRP;
import fr.heavencraft.heavenrp.RPPermissions;

public class ScrollCommand extends AbstractCommandExecutor
{

	public ScrollCommand(HeavenRP plugin)
	{
		super(plugin, "scrolls", RPPermissions.SCROLL_GIVE);
	}

	@Override
	protected void onPlayerCommand(Player player, String[] args) throws HeavenException
	{
		// scroll <id>
		if(args.length < 1)
		{
			sendUsage(player);
			return;
		}
		
		int id = DevUtil.toUint(args[0]);
		Scroll scr = ScrollProvider.getInstance().getScroll(id);
		if(scr == null)
		{
			sendUsage(player);
			return;
		}
		player.getInventory().addItem(scr.getItem());
		
	}

	@Override
	protected void onConsoleCommand(CommandSender sender, String[] args) throws HeavenException
	{
		throw new HeavenException("Uniquement utilisable depuis le jeu.");
	}

	@Override
	protected void sendUsage(CommandSender sender)
	{
		ChatUtil.sendMessage(sender, "");
		ChatUtil.sendMessage(sender, "==== {Liste des Parchemins} ====");
		int i = 0;
		for(Scroll scr : ScrollProvider.getInstance().getScrolls())
		{
			ChatUtil.sendMessage(sender, "%1$d : %2$s", i++, scr.getItem().getItemMeta().getDisplayName());
		}
		ChatUtil.sendMessage(sender, "");
		ChatUtil.sendMessage(sender, "/scroll <id> : Pour recevoir le parchemin.");
	}
	
}

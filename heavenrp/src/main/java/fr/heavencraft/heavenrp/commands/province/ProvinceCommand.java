package fr.heavencraft.heavenrp.commands.province;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.heavencraft.heavencore.bukkit.commands.AbstractCommandExecutor;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.utils.chat.ChatUtil;
import fr.heavencraft.heavenrp.HeavenRP;
import fr.heavencraft.heavenrp.provinces.ProvincesManager;
import fr.heavencraft.heavenrp.provinces.ProvincesManager.Province;

public class ProvinceCommand extends AbstractCommandExecutor
{
	private final static String PROVINCE_LIST_ENTRY = "- {%1$s} | Niveau: {%2$d}";
	public ProvinceCommand(HeavenRP plugin)
	{
		super(plugin, "provinces");
	}

	@Override
	protected void onPlayerCommand(Player player, String[] args) throws HeavenException
	{
		List<Province> provinces = ProvincesManager.getProvinces();
		for(int i = 0; i > provinces.size(); i++) {
			ChatUtil.sendMessage(player, PROVINCE_LIST_ENTRY, 
					provinces.get(i).getName(), 
					ProvincesManager.getLevel(provinces.get(i).getPoints()));
		}	
	}

	@Override
	protected void onConsoleCommand(CommandSender sender, String[] args) throws HeavenException
	{
		
	}

	@Override
	protected void sendUsage(CommandSender sender)
	{
	}

}

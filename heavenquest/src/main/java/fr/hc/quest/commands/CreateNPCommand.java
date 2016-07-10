package fr.hc.quest.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import fr.hc.quest.MyFirstGoal;
import fr.heavencraft.heavencore.bukkit.HeavenPlugin;
import fr.heavencraft.heavencore.bukkit.commands.AbstractCommandExecutor;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.utils.chat.ChatUtil;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;

public class CreateNPCommand extends AbstractCommandExecutor
{

	public CreateNPCommand(HeavenPlugin plugin)
	{
		super(plugin, "bobby");
	}

	@Override
	public void onPlayerCommand(Player player, String[] args) throws HeavenException
	{
		NPCRegistry registry = CitizensAPI.getNPCRegistry();
		NPC npc = registry.createNPC(EntityType.BAT, "Bobby");

		npc.spawn(player.getLocation());
		npc.getDefaultGoalController().addGoal(new MyFirstGoal(npc, player), 1);
		// npc.addTrait(NpcListener.class);
		ChatUtil.sendMessage(player, "Vous venez d'invoquer Bobby..");
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

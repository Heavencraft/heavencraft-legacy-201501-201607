package fr.hc.quest;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import fr.hc.quest.goals.StraightToLocationGoal;
import fr.hc.quest.goals.TurnArroundGoal;
import fr.heavencraft.heavencore.bukkit.HeavenPlugin;
import fr.heavencraft.heavencore.bukkit.commands.AbstractCommandExecutor;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.utils.chat.ChatUtil;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;

public class TestCommand extends AbstractCommandExecutor
{
	public TestCommand(HeavenPlugin plugin)
	{
		super(plugin, "test");
	}

	@Override
	protected void onPlayerCommand(Player player, String[] args) throws HeavenException
	{
		if (args.length != 1)
		{
			sendUsage(player);
			return;
		}

		final Location location = player.getLocation();

		switch (args[0].toLowerCase())
		{
			case "turnarround":
			{
				final NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, "Turn arround");
				npc.getDefaultGoalController().addGoal(new TurnArroundGoal(npc, location, 15, 0), 1);
				npc.spawn(location);
				break;
			}
			case "disperser":
				for (int i = 0; i != 8; i++)
				{
					final NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, "Disperser");
					npc.getDefaultGoalController()
							.addGoal(new StraightToLocationGoal(npc, player.getLocation().add(15, 0, 15)), 2);
					npc.getDefaultGoalController().addGoal(new TurnArroundGoal(npc, location, 15, i * 45), 1);
					npc.spawn(location);
				}
			default:
				sendUsage(player);
		}
	}

	@Override
	protected void onConsoleCommand(CommandSender sender, String[] args) throws HeavenException
	{
		// TODO Auto-generated method stub

	}

	@Override
	protected void sendUsage(CommandSender sender)
	{
		ChatUtil.sendMessage(sender, "/{test} turnarround");
		ChatUtil.sendMessage(sender, "/{test} disperser");
	}
}
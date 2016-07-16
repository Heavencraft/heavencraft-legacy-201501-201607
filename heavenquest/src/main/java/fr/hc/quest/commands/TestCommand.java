package fr.hc.quest.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import fr.hc.quest.goals.StraightToLocationGoal;
import fr.hc.quest.goals.TurnArroundGoal;
import fr.hc.quest.npc.DefenseSoldier;
import fr.heavencraft.heavencore.bukkit.HeavenPlugin;
import fr.heavencraft.heavencore.bukkit.commands.AbstractCommandExecutor;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.utils.DevUtil;
import fr.heavencraft.heavencore.utils.chat.ChatUtil;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;

public class TestCommand extends AbstractCommandExecutor
{
	private static final Location CITADEL_SPAWN = new Location(Bukkit.getWorld("world"), 1100, 4, 781);
	private static final Location CITADEL_1 = new Location(Bukkit.getWorld("world"), 1095, 8, 782);
	private static final Location CITADEL_2 = new Location(Bukkit.getWorld("world"), 1095, 8, 770);
	private static final Location CITADEL_3 = new Location(Bukkit.getWorld("world"), 1107, 8, 770);
	private static final Location CITADEL_4 = new Location(Bukkit.getWorld("world"), 1107, 8, 782);

	public TestCommand(HeavenPlugin plugin)
	{
		super(plugin, "test");
	}

	@Override
	protected void onPlayerCommand(Player player, String[] args) throws HeavenException
	{
		if (args.length == 0)
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
				npc.setProtected(false);
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
				break;
			case "defense":
				new DefenseSoldier(player.getLocation(), new Location(player.getWorld(), DevUtil.toInt(args[1]),
						DevUtil.toInt(args[2]), DevUtil.toInt(args[3])));
				break;
			case "citadel":
				new DefenseSoldier(CITADEL_1, CITADEL_1);
				new DefenseSoldier(CITADEL_2, CITADEL_2);
				new DefenseSoldier(CITADEL_3, CITADEL_3);
				new DefenseSoldier(CITADEL_4, CITADEL_4);
				break;
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
		ChatUtil.sendMessage(sender, "/{test} defense x y z");
		ChatUtil.sendMessage(sender, "/{test} citadel");
	}
}
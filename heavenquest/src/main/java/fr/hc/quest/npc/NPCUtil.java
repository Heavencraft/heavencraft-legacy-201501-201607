package fr.hc.quest.npc;

import net.citizensnpcs.api.npc.NPC;

public class NPCUtil
{
	private static final String TEAM_CF = "team";

	public static void setTeam(NPC npc, String team)
	{
		npc.data().set(TEAM_CF, team);
	}

	public static boolean checkSameTeam(NPC npc1, NPC npc2)
	{
		final String team1 = npc1.data().get(TEAM_CF);
		if (team1 == null)
			return false;
		return team1.equals(npc2.data().get(TEAM_CF));
	}
}

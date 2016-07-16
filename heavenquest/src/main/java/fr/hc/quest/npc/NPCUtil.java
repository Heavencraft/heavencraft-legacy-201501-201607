package fr.hc.quest.npc;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.util.NMS;
import net.jafama.FastMath;

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

	private static Location FROM_LOCATION = new Location(null, 0, 0, 0);

	public static void faceLocation(Entity entity, Location to)
	{
		if (entity.getWorld() != to.getWorld())
			return;

		final Location fromLocation = entity.getLocation(FROM_LOCATION);

		final double xDiff = to.getX() - fromLocation.getX();
		final double yDiff = to.getY() - fromLocation.getY();
		final double zDiff = to.getZ() - fromLocation.getZ();

		final double distanceXZ = FastMath.sqrt(xDiff * xDiff + zDiff * zDiff);
		final double distanceY = FastMath.sqrt(distanceXZ * distanceXZ + yDiff * yDiff);

		double yaw = FastMath.toDegrees(FastMath.acos(xDiff / distanceXZ));
		final double pitch = FastMath.toDegrees(FastMath.acos(yDiff / distanceY)) - 90;

		if (zDiff < 0)
			yaw += FastMath.abs(180 - yaw) * 2;

		NMS.look(entity, (float) yaw - 90, (float) pitch);
	}

	public static Vector getDirectionToLookAt(Entity entity, Location to)
	{
		if (entity.getWorld() != to.getWorld())
			return null;

		final Location fromLocation = entity.getLocation(FROM_LOCATION);

		final double xDiff = to.getX() - fromLocation.getX();
		final double yDiff = to.getY() - fromLocation.getY();
		final double zDiff = to.getZ() - fromLocation.getZ();

		final double distanceXZ = FastMath.sqrt(xDiff * xDiff + zDiff * zDiff);
		final double distanceY = FastMath.sqrt(distanceXZ * distanceXZ + yDiff * yDiff);

		return new Vector(xDiff / distanceY, yDiff / distanceY, zDiff / distanceY);
	}
}
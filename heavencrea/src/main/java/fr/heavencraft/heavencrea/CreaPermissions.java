package fr.heavencraft.heavencrea;

import fr.heavencraft.heavencore.CorePermissions;
import fr.heavencraft.heavencrea.worlds.WorldsManager;

public class CreaPermissions
{

	public static final String PARCELLE_SIGN = "heavencrea.signs.parcelle";
	public static final String TALENT_SIGN = "heavencrea.signs.talent";

	public static final String PLOT_COMMAND = "heavencrea.commands.plot";
	public static final String TPHOME_COMMAND = "heavencrea.commands.tphome";

	public static final String TALENT = CorePermissions.WORLD_ACCESS + WorldsManager.WORLD_TALENT;
	public static final String ARCHITECT = CorePermissions.WORLD_ACCESS + WorldsManager.WORLD_TALENT;
}
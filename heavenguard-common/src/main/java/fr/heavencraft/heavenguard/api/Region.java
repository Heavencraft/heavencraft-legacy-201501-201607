package fr.heavencraft.heavenguard.api;

import java.util.Collection;
import java.util.UUID;

import fr.heavencraft.heavencore.exceptions.HeavenException;

public interface Region
{
	String getName();

	boolean canBuilt(UUID player);

	/*
	 * Parent
	 */

	Region getParent();

	void setParent(String name) throws HeavenException;

	/*
	 * Coordonnées
	 */

	boolean contains(String world, int x, int y, int z);

	// For optimization purpose, if we already have checked the world
	boolean containsSameWorld(int x, int y, int z);

	void redefine(String world, int minX, int minY, int minZ, int maxX, int maxY, int maxZ) throws HeavenException;

	String getWorld();

	int getMinX();

	int getMinY();

	int getMinZ();

	int getMaxX();

	int getMaxY();

	int getMaxZ();

	/*
	 * Members
	 */

	void addMember(UUID player, boolean owner) throws HeavenException;

	boolean isMember(UUID player, boolean owner);

	void removeMember(UUID player, boolean owner) throws HeavenException;

	Collection<UUID> getMembers(boolean owner);

	/*
	 * FlagHandler
	 */

	FlagHandler getFlagHandler();
}
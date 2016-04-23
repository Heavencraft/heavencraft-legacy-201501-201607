package fr.heavencraft.heavencore.users;

import java.util.UUID;

import fr.heavencraft.heavencore.users.color.TabColor;

public interface User
{
	String getUniqueIdAsString();

	UUID getUniqueId();

	String getName();

	TabColor getTabColor();
}
package fr.heavencraft.heavencore.users;

import java.sql.Timestamp;
import java.util.UUID;

public interface User
{
	String getUniqueIdAsString();

	UUID getUniqueId();

	String getName();

	Timestamp getLastLogin();
}
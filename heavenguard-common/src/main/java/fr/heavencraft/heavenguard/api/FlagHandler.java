package fr.heavencraft.heavenguard.api;

import java.sql.Timestamp;

import fr.heavencraft.heavencore.exceptions.HeavenException;

public interface FlagHandler
{
	/*
	 * Boolean flags
	 */

	Boolean getBooleanFlag(Flag flag);

	void setBooleanFlag(Flag flag, Boolean value) throws HeavenException;

	/*
	 * ByteArray flags
	 */

	byte[] getByteArrayFlag(Flag flag);

	void setByteArrayFlag(Flag flag, byte[] value) throws HeavenException;

	/*
	 * Timestamp flags
	 */

	Timestamp getTimestampFlag(Flag flag);

	void setTimestampFlag(Flag flag, Timestamp value) throws HeavenException;
}
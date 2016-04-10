package fr.heavencraft.deprecated;

public interface DeprecatedHasUserProvider<T extends DeprecatedUser>
{
	DeprecatedUserProvider<T> getUserProvider();
}
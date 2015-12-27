package fr.heavencraft.heavenrp.jobs.actions;

public enum JobActionType
{
	BAKE('S'), // Cuire
	BREAK('B'), // Casser
	CRAFT('C'), // Crafter
	KILL('K'), // Kill
	FISH('P'); // Pécher

	private JobActionType(char id)
	{

	}
}
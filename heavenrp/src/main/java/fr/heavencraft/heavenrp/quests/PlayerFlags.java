package fr.heavencraft.heavenrp.quests;

import fr.heavencraft.heavenrp.questframework.IFlagList;
import fr.heavencraft.heavenrp.questframework.QfFlag;

/**
 * This enum should contain all player flags, which are shared across all quests
 * @author Manuel
 *
 */
public enum PlayerFlags implements IFlagList
{
	// Flag to show usage.
	FIRST_TEST_FLAG(new QfFlag("pFirstTestFlag")),
	SECOND_TEST_FLAG(new QfFlag("pSecondTestFlag"));
	
	private final QfFlag flag;

	private PlayerFlags(QfFlag flag)
	{
		this.flag = flag;
	}

	public QfFlag getFlag()
	{
		return flag;
	}	
}

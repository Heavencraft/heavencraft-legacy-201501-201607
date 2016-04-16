package fr.heavencraft.heavenrp.questframework;

/**
 * This special factory handles storage as registered quests
 * @author Manuel
 *
 */
public class QuestFactory
{	
	private static QuestFactory instance = null;
	/**
	 * Returns the instance of the QuestStore
	 * @return
	 */
	public static Object getInstance()
	{
		if(QuestFactory.instance == null)
			QuestFactory.instance = new QuestFactory();
		return QuestFactory.instance;
	}
	
	
	

}

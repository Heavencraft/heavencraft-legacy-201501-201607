package fr.heavencraft.heavenrp.jobs;

public class Test
{

	public static void main(String[] args)
	{
		Job.createJobFromString("METIER1=C.STONE.24|K.CREEPER.12");
		Job.createJobFromString("METIER2=C.DIRT.24|K.SKELETON.12");

		System.out.println(Job.getUniqueInstanceByName("METIER1").toString());
	}
}
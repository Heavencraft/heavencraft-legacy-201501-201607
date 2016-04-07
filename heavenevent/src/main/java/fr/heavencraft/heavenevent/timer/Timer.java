package fr.heavencraft.heavenevent.timer;

public class Timer
{

	public static void loadTimer()
	{

		TimerConfigurationEditor.loadDefaultConfig();

		if (TimerConfigurationEditor.start)
			TimerScoreboard.initScoreboard();
	}
}

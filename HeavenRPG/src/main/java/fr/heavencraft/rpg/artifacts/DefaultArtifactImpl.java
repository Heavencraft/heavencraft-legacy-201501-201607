package fr.heavencraft.rpg.artifacts;

import org.bukkit.entity.Player;

import fr.heavencraft.Utils.ChatUtil;
import fr.heavencraft.exceptions.HeavenException;
import fr.heavencraft.rpg.artifacts.effects.Effect;

public class DefaultArtifactImpl implements Artifact
{
	private final Effect[] _effects;

	DefaultArtifactImpl(Effect... effects)
	{
		_effects = effects;
	}

	@Override
	public boolean canUse(Player player)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void use(Player player)
	{
		for (Effect effect : _effects)
		{
			try
			{
				effect.doEffect(player);
			}
			catch (HeavenException ex)
			{
				ex.printStackTrace();
				ChatUtil.sendMessage(player, ex.getMessage());
			}
		}
	}
}
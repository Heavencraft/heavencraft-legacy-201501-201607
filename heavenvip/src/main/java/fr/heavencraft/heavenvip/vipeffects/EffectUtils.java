package fr.heavencraft.heavenvip.vipeffects;

import java.util.ArrayList;

public class EffectUtils
{
	public static ArrayList<Integer> translateEffectString(String data) {
		ArrayList<Integer> effect = new ArrayList<Integer>();
		
		// Token each element separated by |
		String[] raw = data.split("|");
		for(int i = 0; i < raw.length; i++) {
			effect.add(Integer.parseInt(raw[i]));
		}
		
		return effect;
	}
}

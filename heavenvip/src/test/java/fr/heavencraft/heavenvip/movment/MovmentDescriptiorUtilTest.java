package fr.heavencraft.heavenvip.movment;

import java.util.ArrayList;

import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.utils.particles.ParticleEffect;
import fr.heavencraft.heavenvip.movments.AppliedDescriptorProperties;
import fr.heavencraft.heavenvip.movments.EffectDescriptorUtils;
import junit.framework.TestCase;

public class MovmentDescriptiorUtilTest extends TestCase
{

	final String simpleEffect1 = "24:1:1.0";
	final String simpleEffect2 = "26:2:1.4";

	public void testMovmentDescriptorSimpleEffect() throws HeavenException
	{
		// General testing with effect 1
		ArrayList<AppliedDescriptorProperties> list = EffectDescriptorUtils.translateEffectString(simpleEffect1);
		assertEquals("Wrong effect count", 1, list.size(), 1);
		assertEquals("Wrong effect id", 24, list.get(0).getEffect().getId());
		assertEquals("Effect ammount error", 1, list.get(0).getAmount());
		assertEquals("Effect speed error", 1.0f, list.get(0).getSpeed(), 0.0001f);
		assertNull(list.get(0).getOrdinaryColor());
		assertNull(list.get(0).getNoteColor());
		// Repeatability with effect 2
		list = EffectDescriptorUtils.translateEffectString(simpleEffect2);
		assertEquals("Wrong effect count", 1, list.size());
		assertEquals("Effect should be id 26", 26, list.get(0).getEffect().getId());
		assertEquals("Effect ammount error", 2, list.get(0).getAmount());
		assertEquals("Effect speed error", 1.4f, list.get(0).getSpeed(), 0.0001f);

	}

	final String simpleEffectWithSeparator = "26:1:1.0|";

	public void testMovementDescriptorSimpleEffectSeparator()
	{
		// Separator for multiple entries entered for only one effect
		try
		{
			EffectDescriptorUtils.translateEffectString(simpleEffectWithSeparator);
			fail("Entered single effect with multiple effect separator");
		}
		catch (HeavenException ex)
		{
			assertTrue("Wrong error thrown",
					String.format(EffectDescriptorUtils.EXC_USELESS_SEPARATOR, simpleEffectWithSeparator)
							.equalsIgnoreCase(ex.getMessage()));
		}

	}

	final String simpleEffectFaultyParams = "24:2:1.4:7";

	public void testMovementDescriptorSimpleEffectBlocParameterCountfail()
	{
		// Separator for multiple entries entered for only one effect
		try
		{
			ArrayList<AppliedDescriptorProperties> list = EffectDescriptorUtils
					.translateEffectString(simpleEffectFaultyParams);
			fail("Entered single effect with more parameters than allowed");
		}
		catch (HeavenException ex)
		{
			assertTrue("Wrong error thrown",
					String.format(EffectDescriptorUtils.EXC_INVALID_AUXILARY_OPERATOR_USED, 7, simpleEffectFaultyParams)
							.equalsIgnoreCase(ex.getMessage()));
		}

	}

	final String simpleDualEffect1 = "24:3:1.4|26:2:1.1";

	public void testMovmentDescriptorDualEffect() throws HeavenException
	{
		// General testing with two effects: "24:3:1.4|26:2:1.1"
		ArrayList<AppliedDescriptorProperties> list = EffectDescriptorUtils.translateEffectString(simpleDualEffect1);
		assertEquals("Wrong effect count", 2, list.size());
		assertEquals("Wrong effect ID", 24, list.get(0).getEffect().getId());
		assertEquals("Effect ammount error", 3, list.get(0).getAmount());
		assertEquals("Effect speed error", 1.4f, list.get(0).getSpeed(), 0.0001f);
		assertNull(list.get(0).getOrdinaryColor());
		assertNull(list.get(0).getNoteColor());
		assertEquals("Wrong effect ID", 26, list.get(1).getEffect().getId());
		assertEquals("Effect ammount error", 2, list.get(1).getAmount());
		assertEquals("Effect speed error", 1.1f, list.get(1).getSpeed(), 0.0001f);
		assertNull(list.get(1).getOrdinaryColor());
		assertNull(list.get(1).getNoteColor());
	}

	final String simpleEffectColor = "15:3:1.4:color.255.25.10";

	public void testMovmentDescriptoAuxilaryPramColor() throws HeavenException
	{
		ArrayList<AppliedDescriptorProperties> list = EffectDescriptorUtils.translateEffectString(simpleEffectColor);
		assertEquals("Should only be one effect", 1, list.size());
		assertEquals("Wrong effect ID", 15, list.get(0).getEffect().getId());
		assertEquals("Effect ammount error", 3, list.get(0).getAmount());
		assertEquals("Effect speed error", 1.4f, list.get(0).getSpeed(), 0.0001f);
		assertEquals("Wrong effect color: red", 255, list.get(0).getOrdinaryColor().getRed());
		assertEquals("Wrong effect color: green", 25, list.get(0).getOrdinaryColor().getGreen());
		assertEquals("Wrong effect color: blue", 10, list.get(0).getOrdinaryColor().getBlue());
	}
	
	final String simpleEffectNote = "23:4:1.2:note.24";
	
	public void testMovmentDescriptoAuxilaryPrammNote() throws HeavenException
	{
		ArrayList<AppliedDescriptorProperties> list = EffectDescriptorUtils.translateEffectString(simpleEffectNote);
		assertEquals("Should only be one effect", 1, list.size());
		assertEquals("Wrong effect ID", 23, list.get(0).getEffect().getId());
		assertEquals("Effect ammount error", 4, list.get(0).getAmount());
		assertEquals("Effect speed error", 1.2f, list.get(0).getSpeed(), 0.0001f);
		assertEquals("Wrong effect color: red", 24, list.get(0).getNoteColor().getNoteId());
	}
}

package fr.heavencraft.heavenvip.movment;

import java.util.ArrayList;

import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavenvip.movments.AppliedDescriptorProperties;
import fr.heavencraft.heavenvip.movments.EffectDescriptorUtils;
import junit.framework.TestCase;

public class MovmentDescriptiorUtilTest extends TestCase
{
	final String simpleEffect1 = "24:1:1.0";
	final String simpleEffect2 = "26:2:1.4";
	final String simpleEffectFaultyParams = "24:2:1.4:7";
	final String simpleEffectWithSeparator = "26:1:1.0|";
	final String simpleDualEffect1 = "24:3:1.4|26:2:1.1";
	final String simpleEffectColor = "24:3:1.4:color.255.25";

	public void testMovmentDescriptorSimpleEffect() throws HeavenException
	{
		// General testing with effect 1
		ArrayList<AppliedDescriptorProperties> list = EffectDescriptorUtils.translateEffectString(simpleEffect1);
		assertEquals("Should only be one effect", list.size(), 1);
		assertEquals("Effect should be id 24", list.get(0).getEffect().getId(), 24);
		assertEquals("Effect ammount error", list.get(0).getAmount(), 1);
		assertEquals("Effect speed error", list.get(0).getSpeed(), 1.0f, 0.0001f);
		assertNull(list.get(0).getOrdinaryColor());
		assertNull(list.get(0).getNoteColor());
		// Repeatability with effect 2
		list = EffectDescriptorUtils.translateEffectString(simpleEffect2);
		assertEquals("Should only be one effect", list.size(), 1);
		assertEquals("Effect should be id 26", list.get(0).getEffect().getId(), 26);
		assertEquals("Effect ammount error", list.get(0).getAmount(), 2);
		assertEquals("Effect speed error", list.get(0).getSpeed(), 1.4f, 0.0001f);

	}

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

	public void testMovmentDescriptorDualEffect() throws HeavenException
	{
		// General testing with two effects: "24:3:1.4|26:2:1.1"
		ArrayList<AppliedDescriptorProperties> list = EffectDescriptorUtils.translateEffectString(simpleDualEffect1);
		assertEquals("Should only be one effect", list.size(), 2);
		assertEquals("Effect should be id 24", list.get(0).getEffect().getId(), 24);
		assertEquals("Effect ammount error", list.get(0).getAmount(), 3);
		assertEquals("Effect speed error", list.get(0).getSpeed(), 1.4f, 0.0001f);
		assertNull(list.get(0).getOrdinaryColor());
		assertNull(list.get(0).getNoteColor());
		assertEquals("Effect should be id 24", list.get(1).getEffect().getId(), 26);
		assertEquals("Effect ammount error", list.get(1).getAmount(), 2);
		assertEquals("Effect speed error", list.get(1).getSpeed(), 1.1f, 0.0001f);
		assertNull(list.get(1).getOrdinaryColor());
		assertNull(list.get(1).getNoteColor());
	}
	
	public void testMovmentDescriptoAuxilaryPramms() throws HeavenException
	{
		
	}

}

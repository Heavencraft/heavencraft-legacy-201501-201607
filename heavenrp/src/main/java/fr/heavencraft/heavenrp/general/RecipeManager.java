package fr.heavencraft.heavenrp.general;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

public class RecipeManager
{
	public RecipeManager()
	{
		// Stonebrick ronde
		ShapelessRecipe smoothRecipe = new ShapelessRecipe(new ItemStack(Material.SMOOTH_BRICK, 1, (short) 3))
				.addIngredient(4, Material.SMOOTH_BRICK);
		Bukkit.addRecipe(smoothRecipe);
		
		// Mossy cobblestone
		ShapedRecipe mossyCobblestone = new ShapedRecipe(new ItemStack(Material.MOSSY_COBBLESTONE, 1));
		mossyCobblestone.shape(new String[] { "vvv", "vcv", "vvv" });
		mossyCobblestone.setIngredient('v', Material.VINE);
		mossyCobblestone.setIngredient('c', Material.COBBLESTONE);
		Bukkit.addRecipe(mossyCobblestone);
		
		// Mossy stonebrick
		ShapedRecipe mossySmoothBrick = new ShapedRecipe(new ItemStack(Material.SMOOTH_BRICK, 1, (short) 1));
		mossySmoothBrick.shape(new String[] { "vvv", "vsv", "vvv" });
		mossySmoothBrick.setIngredient('v', Material.VINE);
		mossySmoothBrick.setIngredient('s', Material.SMOOTH_BRICK);
		Bukkit.addRecipe(mossySmoothBrick);
		
		// Cracked stonebrick
		ShapedRecipe crackedSmoothBrick = new ShapedRecipe(new ItemStack(Material.SMOOTH_BRICK, 1, (short) 2));
		crackedSmoothBrick.shape(new String[] { " v ", "vsv", " v " });
		crackedSmoothBrick.setIngredient('v', Material.SULPHUR);
		crackedSmoothBrick.setIngredient('s', Material.SMOOTH_BRICK);
		Bukkit.addRecipe(crackedSmoothBrick);
		
		// Saddle
		ShapedRecipe saddle = new ShapedRecipe(new ItemStack(Material.SADDLE, 1));
		saddle.shape(new String[] { "lll", "lil", "i i" });
		saddle.setIngredient('i', Material.IRON_INGOT);
		saddle.setIngredient('l', Material.LEATHER);
		Bukkit.addRecipe(saddle);
		
		// Mushroom Stem
		ShapelessRecipe mushroomStem = new ShapelessRecipe(new ItemStack(Material.HUGE_MUSHROOM_1, 1, (short) 15))
			 .addIngredient(2, Material.HUGE_MUSHROOM_1);
		Bukkit.addRecipe(mushroomStem);
		
		// Packed Ice
		ShapelessRecipe packedShapelessRecipe = new ShapelessRecipe(new ItemStack(Material.PACKED_ICE, 1)).addIngredient(4, Material.ICE);
		Bukkit.addRecipe(packedShapelessRecipe);
	}
}
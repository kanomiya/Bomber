package com.kanomiya.mcmod.bomber;

import java.util.function.Consumer;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod(modid = Bomber.MODID, version = Bomber.VERSION)
public class Bomber
{
	public static final String MODID = "com.kanomiya.mcmod.bomber";
	public static final String VERSION = "1.0.0";

	public static Item itemTntLauncher;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		itemTntLauncher = new Item()
		{
			@Override
			public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand)
			{
				if (playerIn.capabilities.isCreativeMode || 0 < playerIn.inventory.clearMatchingItems(Item.getItemFromBlock(Blocks.TNT), 0, 1, null))
				{
					if (! worldIn.isRemote)
					{
						Entity entity = new EntityTNTPrimed(worldIn, playerIn.posX, playerIn.posY +playerIn.getEyeHeight(), playerIn.posZ, playerIn);

						EntityEnderPearl dummyThrowable = new EntityEnderPearl(worldIn, playerIn);
						dummyThrowable.setHeadingFromThrower(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0f, 1.5f, 1.0f);

						entity.motionX = dummyThrowable.motionX;
						entity.motionY = dummyThrowable.motionY;
						entity.motionZ = dummyThrowable.motionZ;
						entity.rotationYaw = dummyThrowable.rotationYaw;
						entity.rotationPitch = dummyThrowable.rotationPitch;

						dummyThrowable = null;

						worldIn.spawnEntityInWorld(entity);

						return new ActionResult(EnumActionResult.SUCCESS, itemStackIn);
					}
				}

				return new ActionResult(EnumActionResult.PASS, itemStackIn);
			}
		}
		.setCreativeTab(CreativeTabs.COMBAT)
		.setRegistryName(new ResourceLocation(MODID, "itemTntLauncher"))
		.setUnlocalizedName("itemTntLauncher");

		GameRegistry.register(itemTntLauncher);

		GameRegistry.addRecipe(new ItemStack(itemTntLauncher),
					"IF",
					"II",
					'I', Items.IRON_INGOT,
					'F', Items.FLINT_AND_STEEL
				);

		if (event.getSide().isClient())
		{
			Consumer<Item> simpleRegister = (item) -> ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));

			simpleRegister.accept(itemTntLauncher);
		}

	}
}

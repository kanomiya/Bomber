package com.kanomiya.mcmod.bomber;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
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
				if (! worldIn.isRemote)
				{
					Entity entity = new EntityTNTPrimed(worldIn, playerIn.posX, playerIn.posY, playerIn.posZ, playerIn);

					EntityEnderPearl dummyThrowable = new EntityEnderPearl(worldIn, playerIn);
					dummyThrowable.setHeadingFromThrower(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0F, 1.5F, 1.0F);

					entity.motionX = dummyThrowable.motionX;
					entity.motionY = dummyThrowable.motionY;
					entity.motionZ = dummyThrowable.motionZ;
					entity.rotationYaw = dummyThrowable.rotationYaw;
					entity.rotationPitch = dummyThrowable.rotationPitch;

					dummyThrowable = null;

					worldIn.spawnEntityInWorld(entity);
				}

				return new ActionResult(EnumActionResult.PASS, itemStackIn);
			}
		}
		.setRegistryName(new ResourceLocation(MODID, "itemTntLauncher"))
		.setUnlocalizedName("itemTntLauncher");

		GameRegistry.register(itemTntLauncher);

	}
}

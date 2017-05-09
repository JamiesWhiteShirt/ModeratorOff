package me.lordsaad.modeoff.client;

import me.lordsaad.modeoff.Modeoff;
import me.lordsaad.modeoff.client.gui.GuiHandler;
import me.lordsaad.modeoff.common.CommonProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

/**
 * Created by LordSaad.
 */
public class ClientProxy extends CommonProxy {

	public void preInit(FMLPreInitializationEvent event) {
		super.preInit(event);

		NetworkRegistry.INSTANCE.registerGuiHandler(Modeoff.instance, new GuiHandler());
	}

	public void init(FMLInitializationEvent event) {
		super.init(event);
	}

	public void postInit(FMLPostInitializationEvent event) {
		super.postInit(event);
	}
}

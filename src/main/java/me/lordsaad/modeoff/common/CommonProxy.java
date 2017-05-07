package me.lordsaad.modeoff.common;

import com.teamwizardry.librarianlib.features.config.EasyConfigHandler;
import me.lordsaad.modeoff.Modeoff;
import me.lordsaad.modeoff.api.PlotAssigningManager;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.File;

/**
 * Created by LordSaad.
 */
public class CommonProxy {

	public static File directory;

	public void preInit(FMLPreInitializationEvent event) {
		EasyConfigHandler.init();

		File configFolder = event.getModConfigurationDirectory();

		if (!configFolder.exists()) {
			Modeoff.logger.info(configFolder.getPath() + " not found. Creating directory...");
			if (!configFolder.mkdirs()) {
				Modeoff.logger.error("SOMETHING WENT WRONG! Could not create config directory " + configFolder.getPath());
				return;
			}
			Modeoff.logger.info(configFolder.getPath() + " has been created successfully!");
		}

		directory = new File(configFolder, Modeoff.MOD_ID);
	}

	public void init(FMLInitializationEvent event) {

	}

	public void postInit(FMLPostInitializationEvent event) {

	}
}

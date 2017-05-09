package me.lordsaad.modeoff.common;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.teamwizardry.librarianlib.features.config.EasyConfigHandler;
import me.lordsaad.modeoff.Modeoff;
import me.lordsaad.modeoff.api.ConfigValues;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Created by LordSaad.
 */
public class CommonProxy {

	public static File directory;
	public static Set<UUID> teamMembers = new HashSet<>();
	public static Set<UUID> contestants = new HashSet<>();

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

		MinecraftForge.EVENT_BUS.register(new EventHandler());
	}

	public void init(FMLInitializationEvent event) {

	}

	public void postInit(FMLPostInitializationEvent event) {
		try {
			Modeoff.logger.info("Downloading list of team members and contestants...");
			{
				String urlString = ConfigValues.urlContestants;
				URL url = new URL(urlString);
				HttpURLConnection request = (HttpURLConnection) url.openConnection();
				request.connect();
				JsonParser parser = new JsonParser();
				JsonElement root = parser.parse(new InputStreamReader((InputStream) request.getContent()));
				JsonObject base = root.getAsJsonObject();
				if (base.has("list") && base.get("list").isJsonArray()) {
					JsonArray arrayContestants = base.getAsJsonArray();
					for (JsonElement element : arrayContestants) {
						if (element.isJsonObject()) {
							JsonObject obj = element.getAsJsonObject();
							if (obj.has("uuid"))
								contestants.add(UUID.fromString(obj.get("uuid").getAsString()));
						}
					}
				}
			}

			{
				String urlString = ConfigValues.urlTeam;
				URL url = new URL(urlString);
				HttpURLConnection request = (HttpURLConnection) url.openConnection();
				request.connect();
				JsonParser parser = new JsonParser();
				JsonElement root = parser.parse(new InputStreamReader((InputStream) request.getContent()));
				JsonObject base = root.getAsJsonObject();
				if (base.has("list") && base.get("list").isJsonArray()) {
					JsonArray arrayContestants = base.getAsJsonArray();
					for (JsonElement element : arrayContestants) {
						if (element.isJsonObject()) {
							JsonObject obj = element.getAsJsonObject();
							if (obj.has("uuid"))
								teamMembers.add(UUID.fromString(obj.get("uuid").getAsString()));
						}
					}
				}
			}
			Modeoff.logger.info("Finished downloading lists!");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

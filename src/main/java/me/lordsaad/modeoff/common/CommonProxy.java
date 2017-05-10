package me.lordsaad.modeoff.common;

import com.google.gson.*;
import com.teamwizardry.librarianlib.features.config.EasyConfigHandler;
import com.teamwizardry.librarianlib.features.kotlin.JsonMaker;
import com.teamwizardry.librarianlib.features.network.PacketHandler;
import me.lordsaad.modeoff.Modeoff;
import me.lordsaad.modeoff.api.ConfigValues;
import me.lordsaad.modeoff.api.RankManager;
import me.lordsaad.modeoff.client.gui.GuiHandler;
import me.lordsaad.modeoff.common.network.PacketManagerGui;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;

import java.awt.*;
import java.io.*;
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
			Modeoff.logger.info(configFolder.getName() + " not found. Creating directory...");
			if (!configFolder.mkdirs()) {
				Modeoff.logger.error("SOMETHING WENT WRONG! Could not create config directory " + configFolder.getName());
				return;
			}
			Modeoff.logger.info(configFolder.getName() + " has been created successfully!");
		}

		directory = new File(configFolder, Modeoff.MOD_ID);

		RankManager.INSTANCE.getClass();

		MinecraftForge.EVENT_BUS.register(new EventHandler());

		PacketHandler.register(PacketManagerGui.class, Side.CLIENT);
	}

	public void init(FMLInitializationEvent event) {
		NetworkRegistry.INSTANCE.registerGuiHandler(Modeoff.instance, new GuiHandler());
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

	private void initRanks(File directory) {
		File ranksFolder = new File(directory, "ranks");
		if (!ranksFolder.exists()) {
			Modeoff.logger.info(ranksFolder.getName() + " not found. Creating directory...");
			if (!ranksFolder.mkdirs()) {
				Modeoff.logger.error("SOMETHING WENT WRONG! Could not create config directory " + ranksFolder.getName());
				return;
			}
			Modeoff.logger.info(ranksFolder.getName() + " has been created successfully!");
		}
		RankManager.INSTANCE.directory = ranksFolder;

		File rankConfig = new File(ranksFolder, "ranks_config");
		try {
			if (!rankConfig.exists()) {
				Modeoff.logger.info(rankConfig.getName() + " file not found. Creating file...");
				if (!rankConfig.createNewFile()) {
					Modeoff.logger.fatal("SOMETHING WENT WRONG! Could not create config file " + rankConfig.getName());
					return;
				}

				JsonObject base = new JsonObject();
				JsonArray array = new JsonArray();

				JsonObject team = new JsonObject();
				team.addProperty("name", "Organizer");
				team.addProperty("color", Color.YELLOW.getRGB());
				team.addProperty("gm1", true);
				team.addProperty("claimable_plots", -1);
				team.addProperty("manage_others", true);

				JsonArray teamArray = new JsonArray();
				teamArray.add(new JsonPrimitive("LordSaad"));
				teamArray.add(new JsonPrimitive("Eladkay"));
				teamArray.add(new JsonPrimitive("wiresegal"));
				teamArray.add(new JsonPrimitive("escapee"));
				teamArray.add(new JsonPrimitive("prospector"));
				team.add("players", teamArray);

				array.add(team);

				JsonObject participant = new JsonObject();
				participant.addProperty("name", "Participant");
				participant.addProperty("color", Color.CYAN.getRGB());
				participant.addProperty("gm1", false);
				participant.addProperty("claimable_plots", 1);
				participant.addProperty("manage_others", false);
				array.add(participant);

				base.add("ranks", array);

				FileWriter writer = new FileWriter(rankConfig);
				writer.write(JsonMaker.serialize(base));
				writer.flush();
				writer.close();
				Modeoff.logger.info(rankConfig.getName() + " file has been created successfully!");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		RankManager.INSTANCE.config = rankConfig;
	}
}

package me.lordsaad.modeoff.api;

import com.google.gson.*;
import com.teamwizardry.librarianlib.features.kotlin.JsonMaker;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.io.*;

/**
 * Created by LordSaad.
 */
public class RankManager {
	public static RankManager INSTANCE = new RankManager();

	public File directory, config;

	private RankManager() {
	}

	@Nullable
	public Rank getPlayerRank(String playerName) {
		JsonElement json;
		try {
			json = new JsonParser().parse(new FileReader(config));
			if (json.isJsonObject() && json.getAsJsonObject().has("ranks") && json.getAsJsonObject().get("ranks").isJsonArray()) {
				for (JsonElement element : json.getAsJsonObject().getAsJsonArray("ranks")) {
					if (element.isJsonObject()) {
						JsonObject rank = element.getAsJsonObject();
						if (rank.has("players") && rank.get("players").isJsonArray()) {
							JsonArray players = rank.getAsJsonArray("players");
							for (JsonElement player1 : players) {
								if (player1.isJsonPrimitive() && player1.getAsJsonPrimitive().isString()) {
									String name = player1.getAsString();
									if (playerName.equals(name) || playerName.startsWith("Player")) {
										// FOUND RANK
										return new Rank(
												rank.getAsJsonPrimitive("name").getAsString(),
												new Color(rank.getAsJsonPrimitive("color").getAsInt()),
												rank.getAsJsonPrimitive("gm1").getAsBoolean(),
												rank.getAsJsonPrimitive("claimable_plots").getAsInt(),
												rank.getAsJsonPrimitive("manage_others").getAsBoolean());
									}
								}
							}
						}
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean setPlayerRank(String playerName, String rank) {
		boolean changed = false;
		removePlayerRank(playerName);
		try {
			JsonElement json = new JsonParser().parse(new FileReader(config));

			JsonArray currentRanks = json.getAsJsonObject().getAsJsonArray("ranks");

			for (JsonElement element : currentRanks) {
				JsonObject obj = element.getAsJsonObject();

				if (obj.getAsJsonPrimitive("name").getAsString().equals(rank)) {
					obj.getAsJsonArray("players").add(new JsonPrimitive(playerName));
					changed = true;
				}
			}

			FileWriter writer = new FileWriter(config);
			writer.write(JsonMaker.serialize(json));
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return changed;
	}

	public void removePlayerRank(String playerName) {
		try {
			JsonElement json = new JsonParser().parse(new FileReader(config));

			boolean removeOld = false;
			String oldRank = null;
			JsonArray oldArray = null;

			if (json.isJsonObject() && json.getAsJsonObject().has("ranks") && json.getAsJsonObject().get("ranks").isJsonArray()) {
				primary:
				for (JsonElement element : json.getAsJsonObject().getAsJsonArray("ranks"))
					if (element.isJsonObject()) {
						JsonObject jsonRank = element.getAsJsonObject();
						if (jsonRank.has("players") && jsonRank.get("players").isJsonArray()) {
							JsonArray players = jsonRank.getAsJsonArray("players");
							for (JsonElement player1 : players)
								if (player1.isJsonPrimitive() && player1.getAsJsonPrimitive().isString())
									if (player1.isJsonPrimitive() && player1.getAsJsonPrimitive().isString()) {
										oldRank = jsonRank.getAsJsonPrimitive("name").getAsString();
										oldArray = players;
										removeOld = true;
										break primary;
									}
						}
					}
			} else return;

			if (removeOld && oldRank != null) {
				JsonArray newArray = new JsonArray();
				for (JsonElement element : oldArray) {
					String name = element.getAsJsonPrimitive().getAsString();
					if (name.equals(playerName)) continue;
					newArray.add(new JsonPrimitive(name));
				}

				JsonArray currentRanks = json.getAsJsonObject().getAsJsonArray("ranks");

				JsonObject newObj = new JsonObject();
				JsonArray newRanks = new JsonArray();
				for (JsonElement element : currentRanks) {
					JsonObject obj = element.getAsJsonObject();

					String name = obj.getAsJsonPrimitive("name").getAsString();
					int color = obj.getAsJsonPrimitive("color").getAsInt();
					boolean gm1 = obj.getAsJsonPrimitive("gm1").getAsBoolean();
					int claimablePlots = obj.getAsJsonPrimitive("claimable_plots").getAsInt();
					boolean manageOthers = obj.getAsJsonPrimitive("manage_others").getAsBoolean();

					if (obj.getAsJsonPrimitive("name").getAsString().equals(oldRank)) {

						JsonObject newRank = new JsonObject();
						newRank.addProperty("name", name);
						newRank.addProperty("color", color);
						newRank.addProperty("gm1", gm1);
						newRank.addProperty("claimable_plots", claimablePlots);
						newRank.addProperty("manage_others", manageOthers);
						newRank.add("players", newArray);

						newRanks.add(newRank);

					} else newRanks.add(element.getAsJsonObject());
				}

				newObj.add("ranks", newRanks);
				FileWriter writer = new FileWriter(config);
				writer.write(JsonMaker.serialize(newObj));
				writer.flush();
				writer.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}

package me.lordsaad.modeoff.api;

import com.google.gson.*;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

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
									if (playerName.equals(player1.getAsString())) {
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

	public void setPlayerRank(String playerName, String rank) {
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
									if (playerName.equals(player1.getAsString()))
										if (rank.equals(jsonRank.getAsJsonPrimitive("name").getAsString())) return;
										else {
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

					} else if (rank.equals(name)) {

						JsonObject newRank = new JsonObject();
						newRank.addProperty("name", name);
						newRank.addProperty("color", color);
						newRank.addProperty("gm1", gm1);
						newRank.addProperty("claimable_plots", claimablePlots);
						newRank.addProperty("manage_others", manageOthers);

						JsonArray newPlayers = obj.getAsJsonArray("players");
						newPlayers.add(new JsonPrimitive(playerName));
						newRank.add("players", newPlayers);

						newRanks.add(newRank);

					} else newRanks.add(element.getAsJsonObject());
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}
}

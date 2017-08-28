package info.modoff.modeoff.api

import com.google.gson.*
import com.teamwizardry.librarianlib.features.kotlin.serialize

import java.awt.*
import java.io.*

/**
 * Created by LordSaad.
 */
object RankManager {
    var directory: File? = null
    var config: File? = null

    fun getPlayerRank(playerName: String): Rank? {
        try {
            val json = JsonParser().parse(FileReader(config!!))
            if (json is JsonObject) {
                val ranks = json["ranks"]
                if (ranks is JsonArray) {
                    ranks.filterIsInstance<JsonObject>().forEach { rank ->
                        val players = rank["players"]
                        if (players is JsonArray) {
                            players.filterIsInstance<JsonPrimitive>().forEach { name ->
                                if (name.asString == playerName) {
                                    // FOUND RANK
                                    return Rank(
                                        rank.getAsJsonPrimitive("name").asString,
                                        Color(rank.getAsJsonPrimitive("color").asInt),
                                        rank.getAsJsonPrimitive("gm1").asBoolean,
                                        rank.getAsJsonPrimitive("claimable_plots").asInt,
                                        rank.getAsJsonPrimitive("manage_others").asBoolean
                                    )
                                }
                            }
                        }
                    }
                }
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }

        return null
    }

    fun setPlayerRank(playerName: String, rank: String): Boolean {
        var changed = false
        removePlayerRank(playerName)
        try {
            val json = JsonParser().parse(FileReader(config!!))

            val currentRanks = json.asJsonObject.getAsJsonArray("ranks")

            for (element in currentRanks) {
                val obj = element.asJsonObject

                if (obj.getAsJsonPrimitive("name").asString == rank) {
                    obj.getAsJsonArray("players").add(JsonPrimitive(playerName))
                    changed = true
                }
            }

            val writer = FileWriter(config!!)
            writer.write(json.serialize())
            writer.flush()
            writer.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return changed
    }

    fun removePlayerRank(playerName: String) {
        try {
            val json = JsonParser().parse(FileReader(config!!))

            var removeOld = false
            var oldRank: String? = null
            var oldArray: JsonArray? = null

            if (json.isJsonObject && json.asJsonObject.has("ranks") && json.asJsonObject.get("ranks").isJsonArray) {
                primary@ for (element in json.asJsonObject.getAsJsonArray("ranks"))
                    if (element.isJsonObject) {
                        val jsonRank = element.asJsonObject
                        if (jsonRank.has("players") && jsonRank.get("players").isJsonArray) {
                            val players = jsonRank.getAsJsonArray("players")
                            for (player1 in players)
                                if (player1.isJsonPrimitive && player1.asJsonPrimitive.isString)
                                    if (player1.isJsonPrimitive && player1.asJsonPrimitive.isString) {
                                        oldRank = jsonRank.getAsJsonPrimitive("name").asString
                                        oldArray = players
                                        removeOld = true
                                        break@primary
                                    }
                        }
                    }
            } else
                return

            if (removeOld && oldRank != null) {
                val newArray = JsonArray()
                for (element in oldArray!!) {
                    val name = element.asJsonPrimitive.asString
                    if (name == playerName) continue
                    newArray.add(JsonPrimitive(name))
                }

                val currentRanks = json.asJsonObject.getAsJsonArray("ranks")

                val newObj = JsonObject()
                val newRanks = JsonArray()
                for (element in currentRanks) {
                    val obj = element.asJsonObject

                    val name = obj.getAsJsonPrimitive("name").asString
                    val color = obj.getAsJsonPrimitive("color").asInt
                    val gm1 = obj.getAsJsonPrimitive("gm1").asBoolean
                    val claimablePlots = obj.getAsJsonPrimitive("claimable_plots").asInt
                    val manageOthers = obj.getAsJsonPrimitive("manage_others").asBoolean

                    if (obj.getAsJsonPrimitive("name").asString == oldRank) {

                        val newRank = JsonObject()
                        newRank.addProperty("name", name)
                        newRank.addProperty("color", color)
                        newRank.addProperty("gm1", gm1)
                        newRank.addProperty("claimable_plots", claimablePlots)
                        newRank.addProperty("manage_others", manageOthers)
                        newRank.add("players", newArray)

                        newRanks.add(newRank)

                    } else
                        newRanks.add(element.asJsonObject)
                }

                newObj.add("ranks", newRanks)
                val writer = FileWriter(config!!)
                writer.write(newObj.serialize())
                writer.flush()
                writer.close()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }
}

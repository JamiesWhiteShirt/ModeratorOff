package info.modoff.modeoff.common.plot

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.teamwizardry.librarianlib.features.kotlin.serialize
import info.modoff.modeoff.Modeoff
import info.modoff.modeoff.api.Plot
import info.modoff.modeoff.common.network.message.MessagePlotLayout
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.server.MinecraftServer
import java.io.*
import java.util.*

class PlotManagerServer(server: MinecraftServer, layout: PlotLayout) : PlotManager(layout) {
    fun sendToPlayer(player: EntityPlayerMP) {
        Modeoff.messageHandler.sendTo(MessagePlotLayout(layout), player)
    }

    private val plotFile = server.activeAnvilConverter.getFile(server.folderName, "registered_plots.json")

    init {
        try {
            if (!plotFile.exists()) {
                Modeoff.logger.info(plotFile.name + " file not found. Creating file...")
                if (!plotFile.createNewFile()) {
                    Modeoff.logger.fatal("SOMETHING WENT WRONG! Could not create config file " + plotFile.name)
                }

                val obj = JsonObject()
                obj.add("plots", JsonArray())

                val writer = FileWriter(plotFile)
                writer.write(obj.serialize())
                writer.flush()
                writer.close()
                Modeoff.logger.info(plotFile.name + " file has been created successfully!")
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun isUUIDRegistered(uuid: UUID): Boolean {
        try {
            val json = JsonParser().parse(FileReader(plotFile))
            if (json.isJsonObject && json.asJsonObject.has("plots") && json.asJsonObject.get("plots").isJsonArray) {
                val array = json.asJsonObject.getAsJsonArray("plots")
                array
                    .filter { it.isJsonObject && it.asJsonObject.has("uuid") }
                    .map { UUID.fromString(it.asJsonObject.getAsJsonPrimitive("uuid").asString) }
                    .filter { uuid == it }
                    .forEach { return true }
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }

        return false
    }

    val nextAvailableID: Int
        get() {
            val json: JsonElement
            try {
                json = JsonParser().parse(FileReader(plotFile))
                if (json.isJsonObject && json.asJsonObject.has("plots") && json.asJsonObject.get("plots").isJsonArray) {
                    val array = json.asJsonObject.getAsJsonArray("plots")
                    if (array.size() == 0) return 0
                    return array.size()
                }
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }

            return -1
        }

    fun saveUUIDToPlot(uuid: UUID, plotID: Int): Boolean {
        if (isUUIDRegistered(uuid)) return false

        val json: JsonElement
        try {
            json = JsonParser().parse(FileReader(plotFile))
            if (json.isJsonObject && json.asJsonObject.has("plots") && json.asJsonObject.get("plots").isJsonArray) {
                val array = json.asJsonObject.getAsJsonArray("plots")

                val plots = ArrayList<JsonObject>()
                for (element in array)
                    if (element.isJsonObject) plots.add(element.asJsonObject)

                val newPlot = JsonObject()
                newPlot.addProperty("uuid", uuid.toString())
                newPlot.addProperty("id", plotID)

                val newArray = JsonArray()
                for (element in plots) {
                    newArray.add(element)
                }
                newArray.add(newPlot)

                val obj = JsonObject()
                obj.add("plots", newArray)

                try {
                    val writer = FileWriter(plotFile)
                    writer.write(obj.serialize())
                    writer.flush()
                    writer.close()
                    Modeoff.logger.info("registered plot " + plotID + " to uuid '" + uuid.toString() + "' successfully!")

                    return true
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }

        return false
    }

    override fun getPlotByAssignedUUID(playerUuid: UUID): Plot? {
        try {
            val json = JsonParser().parse(FileReader(plotFile))
            (json as? JsonObject)?.let {
                (it["plots"] as? JsonArray)
                    ?.filterIsInstance<JsonObject>()
                    ?.forEach {
                        val id = it["id"]
                        val uuid = it["uuid"]
                        if (id != null && uuid != null) {
                            if (UUID.fromString(uuid.asString) == playerUuid) {
                                return Plot(id.asInt)
                            }
                        }
                    }
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }

        return null
    }

    fun getUUIDForPlot(plotId: Int): UUID? {
        try {
            val json = JsonParser().parse(FileReader(plotFile))
            (json as? JsonObject)?.let {
                (it["plots"] as? JsonArray)
                    ?.filterIsInstance<JsonObject>()
                    ?.forEach {
                        val id = it["id"]
                        val uuid = it["uuid"]
                        if (id != null && uuid != null) {
                            if (id.asInt == plotId) {
                                return UUID.fromString(uuid.asString)
                            }
                        }
                    }
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }

        return null
    }
}

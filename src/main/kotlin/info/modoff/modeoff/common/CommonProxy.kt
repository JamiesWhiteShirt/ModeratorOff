package info.modoff.modeoff.common

import com.google.gson.*
import com.teamwizardry.librarianlib.features.config.EasyConfigHandler
import com.teamwizardry.librarianlib.features.kotlin.serialize
import com.teamwizardry.librarianlib.features.network.PacketHandler
import info.modoff.modeoff.Modeoff
import info.modoff.modeoff.api.ConfigValues
import info.modoff.modeoff.api.RankManager
import info.modoff.modeoff.common.gui.GuiHandler
import info.modoff.modeoff.common.network.PacketManagerGui
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.common.network.NetworkRegistry
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper
import net.minecraftforge.fml.relauncher.Side

import java.awt.*
import java.io.*
import java.net.*
import java.util.UUID

private fun readUUIDSetFromURL(urlString: String): Set<UUID> {
    try {
        val url = URL(urlString)
        val request = url.openConnection()
        request.connect()
        val parser = JsonParser()
        val root = parser.parse(InputStreamReader(request.content as InputStream))
        return root.takeIf { it.isJsonObject }?.let { root ->
            root.asJsonObject.get("list")?.takeIf { it.isJsonArray }?.let { list ->
                list.asJsonArray
                    .filter { it.isJsonObject }
                    .mapNotNull { it.asJsonObject.get("uuid") }
                    .map { UUID.fromString(it.asString) }
                    .toSet()
            }
        } ?: throw Exception("Malformed JSON")
    } catch (e: IOException) {
        e.printStackTrace()
    } catch (e: JsonParseException) {
        e.printStackTrace()
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return emptySet()
}

/**
 * Created by LordSaad.
 */
abstract class CommonProxy {
    lateinit var directory: File
    lateinit var teamMembers: Set<UUID>
    lateinit var contestants: MutableSet<UUID>

    open fun preInit(event: FMLPreInitializationEvent) {
        EasyConfigHandler.init()

        val configFolder = event.modConfigurationDirectory

        if (!configFolder.exists()) {
            Modeoff.logger.info(configFolder.name + " not found. Creating directory...")
            if (!configFolder.mkdirs()) {
                Modeoff.logger.error("SOMETHING WENT WRONG! Could not create config directory " + configFolder.name)
                return
            }
            Modeoff.logger.info(configFolder.name + " has been created successfully!")
        }

        directory = File(configFolder, Modeoff.MOD_ID)

        MinecraftForge.EVENT_BUS.register(EventHandler())

        PacketHandler.register(PacketManagerGui::class.java, Side.CLIENT)
    }

    open fun init(event: FMLInitializationEvent) {
        NetworkRegistry.INSTANCE.registerGuiHandler(Modeoff, GuiHandler())
    }

    open fun postInit(event: FMLPostInitializationEvent) {
        Modeoff.logger.info("Downloading list of team members and contestants...")
        contestants = readUUIDSetFromURL(ConfigValues.urlContestants).toHashSet()
        teamMembers = readUUIDSetFromURL(ConfigValues.urlTeam).toHashSet()
        Modeoff.logger.info("Finished downloading lists!")

        initRanks(directory)
    }

    private fun initRanks(directory: File) {
        val ranksFolder = File(directory, "ranks")
        if (!ranksFolder.exists()) {
            Modeoff.logger.info(ranksFolder.name + " not found. Creating directory...")
            if (!ranksFolder.mkdirs()) {
                Modeoff.logger.error("SOMETHING WENT WRONG! Could not create config directory " + ranksFolder.name)
                return
            }
            Modeoff.logger.info(ranksFolder.name + " has been created successfully!")
        }
        RankManager.directory = ranksFolder

        val rankConfig = File(ranksFolder, "ranks_config")
        try {
            if (!rankConfig.exists()) {
                Modeoff.logger.info(rankConfig.name + " file not found. Creating file...")
                if (!rankConfig.createNewFile()) {
                    Modeoff.logger.fatal("SOMETHING WENT WRONG! Could not create config file " + rankConfig.name)
                    return
                }

                val base = JsonObject()
                val array = JsonArray()

                val team = JsonObject()
                team.addProperty("name", "Organizer")
                team.addProperty("color", Color.YELLOW.rgb)
                team.addProperty("gm1", true)
                team.addProperty("claimable_plots", -1)
                team.addProperty("manage_others", true)

                val teamArray = JsonArray()
                teamArray.add(JsonPrimitive("LordSaad"))
                teamArray.add(JsonPrimitive("Eladkay"))
                teamArray.add(JsonPrimitive("escapee"))
                teamArray.add(JsonPrimitive("prospector"))
                team.add("players", teamArray)

                array.add(team)

                val participant = JsonObject()
                participant.addProperty("name", "Participant")
                participant.addProperty("color", Color.CYAN.rgb)
                participant.addProperty("gm1", false)
                participant.addProperty("claimable_plots", 1)
                participant.addProperty("manage_others", false)
                array.add(participant)

                base.add("ranks", array)

                val writer = FileWriter(rankConfig)
                writer.write(base.serialize())
                writer.flush()
                writer.close()
                Modeoff.logger.info(rankConfig.name + " file has been created successfully!")
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        RankManager.config = rankConfig
    }

    abstract fun registerMessages(messageHandler: SimpleNetworkWrapper)
}

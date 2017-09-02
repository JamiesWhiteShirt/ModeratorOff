package info.modoff.modeoff.common.plot

import info.modoff.modeoff.Modeoff
import info.modoff.modeoff.common.network.message.MessageAllPlots
import info.modoff.modeoff.common.plot.serialization.readPlotsFromFile
import info.modoff.modeoff.common.plot.serialization.writePlotsToFile
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.server.MinecraftServer
import net.minecraft.util.math.BlockPos
import java.io.*
import java.util.*

class PlotManagerServer(server: MinecraftServer) : PlotManager() {
    private val plotFile = server.activeAnvilConverter.getFile(server.folderName, "modeoff.dat")

    private val plotDatabase = PlotDatabase(try {
        // Null means the file does not exist, this is not an error
        readPlotsFromFile(plotFile) ?: listOf(
            Plot(
                UUID(0, 0),
                BlockPos(0, 0, 0),
                BlockPos(16, 16, 16)
            )
        )
    } catch (e: IOException) {
        // IO error
        Modeoff.logger.error("There was an error reading the modeoff.dat file, plots will be reset")
        e.printStackTrace()
        emptyList<Plot>()
    })

    override val allPlots get() = plotDatabase.all

    fun save() {
        writePlotsToFile(plotFile, plotDatabase.all)
    }

    fun sendToPlayer(player: EntityPlayerMP) {
        Modeoff.messageHandler.sendTo(MessageAllPlots(plotDatabase.all), player)
    }
}

package info.modoff.modeoff

import info.modoff.modeoff.api.ConfigValues
import info.modoff.modeoff.common.CommonProxy
import info.modoff.modeoff.common.PlotLayout
import info.modoff.modeoff.common.command.CommandAssign
import info.modoff.modeoff.common.command.CommandManager
import info.modoff.modeoff.common.command.CommandRank
import info.modoff.modeoff.common.command.CommandTpPlot
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.SidedProxy
import net.minecraftforge.fml.common.event.*
import net.minecraftforge.fml.common.network.NetworkRegistry
import org.apache.logging.log4j.Logger

@Mod(
    modid = Modeoff.MOD_ID,
    name = Modeoff.MOD_NAME,
    version = Modeoff.VERSION,
    dependencies = "required-after:librarianlib",
    modLanguageAdapter = "net.shadowfacts.forgelin.KotlinAdapter"
)
object Modeoff {
    const val MOD_ID = "modeoff"
    const val MOD_NAME = "Modeoff"
    const val VERSION = "1.0"

    const val CLIENT = "info.modoff.modeoff.client.ClientProxy"
    const val SERVER = "info.modoff.modeoff.server.ServerProxy"

    lateinit var logger: Logger private set

    @SidedProxy(clientSide = CLIENT, serverSide = SERVER)
    lateinit var proxy: CommonProxy private set

    val messageHandler = NetworkRegistry.INSTANCE.newSimpleChannel("modeoff")

    @Mod.EventHandler
    fun preInit(event: FMLPreInitializationEvent) {
        logger = event.modLog
        proxy.preInit(event)
        proxy.registerMessages(messageHandler)
    }

    @Mod.EventHandler
    fun init(e: FMLInitializationEvent) {
        proxy.init(e)
    }

    @Mod.EventHandler
    fun postInit(e: FMLPostInitializationEvent) {
        proxy.postInit(e)
    }

    var serverPlotLayout: PlotLayout? = null

    @Mod.EventHandler
    fun onServerStarting(event: FMLServerStartingEvent) {
        serverPlotLayout = PlotLayout(
            ConfigValues.firstPlotX,
            ConfigValues.firstPlotY,
            ConfigValues.firstPlotZ,
            ConfigValues.directionOfRows,
            ConfigValues.directionOfColumns,
            ConfigValues.plotGridRows,
            ConfigValues.plotGridColumns,
            ConfigValues.plotSize,
            ConfigValues.plotMarginWidth,
            ConfigValues.plotWorldDimensionID
        )

        event.registerServerCommand(CommandAssign())
        event.registerServerCommand(CommandTpPlot())
        event.registerServerCommand(CommandManager())
        event.registerServerCommand(CommandRank())
    }

    fun serverClose(event: FMLServerStoppedEvent) {
        serverPlotLayout = null
    }
}

package info.modoff.modeoff

import info.modoff.modeoff.common.CommonProxy
import info.modoff.modeoff.common.command.CommandAssign
import info.modoff.modeoff.common.command.CommandManager
import info.modoff.modeoff.common.command.CommandRank
import info.modoff.modeoff.common.command.CommandTpPlot
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.SidedProxy
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.common.event.FMLServerStartingEvent
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

    const val CLIENT = "ClientProxy"
    const val SERVER = "ServerProxy"

    lateinit var logger: Logger private set

    @SidedProxy(clientSide = CLIENT, serverSide = SERVER)
    lateinit var proxy: CommonProxy private set

    @Mod.EventHandler
    fun preInit(event: FMLPreInitializationEvent) {
        logger = event.modLog
        proxy.preInit(event)
    }

    @Mod.EventHandler
    fun init(e: FMLInitializationEvent) {
        proxy.init(e)
    }

    @Mod.EventHandler
    fun postInit(e: FMLPostInitializationEvent) {
        proxy.postInit(e)
    }

    @Mod.EventHandler
    fun serverLoad(event: FMLServerStartingEvent) {
        event.registerServerCommand(CommandAssign())
        event.registerServerCommand(CommandTpPlot())
        event.registerServerCommand(CommandManager())
        event.registerServerCommand(CommandRank())
    }
}

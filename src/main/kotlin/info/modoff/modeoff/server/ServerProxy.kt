package info.modoff.modeoff.server

import info.modoff.modeoff.Modeoff
import info.modoff.modeoff.common.CommonProxy
import info.modoff.modeoff.common.network.handler.DummyMessageHandler
import info.modoff.modeoff.common.network.message.MessageAllPlots
import info.modoff.modeoff.common.plot.PlotManager
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper
import net.minecraftforge.fml.relauncher.Side

/**
 * Created by LordSaad.
 */
class ServerProxy : CommonProxy() {
    override fun preInit(event: FMLPreInitializationEvent) {
        super.preInit(event)
    }

    override fun init(event: FMLInitializationEvent) {
        super.init(event)
    }

    override fun postInit(event: FMLPostInitializationEvent) {
        super.postInit(event)
    }

    override fun registerMessages(messageHandler: SimpleNetworkWrapper) {
        messageHandler.registerMessage(DummyMessageHandler, MessageAllPlots::class.java, MessageAllPlots.DISCRIMINATOR, Side.CLIENT)
    }

    override fun getPlotManager(side: Side): PlotManager? {
        return when (side) {
            Side.SERVER -> Modeoff.plotManagerServer
            else -> null
        }
    }
}

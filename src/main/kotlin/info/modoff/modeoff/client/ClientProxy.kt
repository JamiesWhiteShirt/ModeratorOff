package info.modoff.modeoff.client

import info.modoff.modeoff.client.network.handler.MessagePlotLayoutHandler
import info.modoff.modeoff.common.CommonProxy
import info.modoff.modeoff.common.PlotLayout
import info.modoff.modeoff.common.network.message.MessagePlotLayout
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.world.WorldEvent
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper
import net.minecraftforge.fml.relauncher.Side

/**
 * Created by LordSaad.
 */
class ClientProxy : CommonProxy() {
    override fun preInit(event: FMLPreInitializationEvent) {
        super.preInit(event)
        MinecraftForge.EVENT_BUS.register(this)
    }

    override fun init(event: FMLInitializationEvent) {
        super.init(event)
    }

    override fun postInit(event: FMLPostInitializationEvent) {
        super.postInit(event)
    }

    override fun registerMessages(messageHandler: SimpleNetworkWrapper) {
        messageHandler.registerMessage(MessagePlotLayoutHandler(this), MessagePlotLayout::class.java, MessagePlotLayout.DISCRIMINATOR, Side.CLIENT)
    }

    var clientPlotLayout: PlotLayout?
    get() = boundClientPlotLayout!!.value
    set(value) {
        boundClientPlotLayout!!.value = value
    }

    private var boundClientPlotLayout: PlayerControllerBound<PlotLayout?>? = null

    @SubscribeEvent
    fun onWorldLoad(event: WorldEvent.Load) {
        if (event.world.isRemote) {
            if (!(boundClientPlotLayout?.isFresh ?: false)) {
                boundClientPlotLayout = PlayerControllerBound.create(null)
            }
        }
    }
}

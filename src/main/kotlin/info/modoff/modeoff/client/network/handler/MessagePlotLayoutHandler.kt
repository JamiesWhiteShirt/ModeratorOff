package info.modoff.modeoff.client.network.handler

import info.modoff.modeoff.client.ClientProxy
import info.modoff.modeoff.client.plot.PlotManagerClient
import info.modoff.modeoff.common.network.message.MessageAllPlots
import net.minecraftforge.fml.common.FMLCommonHandler
import net.minecraftforge.fml.common.network.simpleimpl.IMessage
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext

class MessagePlotLayoutHandler(val proxy: ClientProxy) : IMessageHandler<MessageAllPlots, IMessage> {
    override fun onMessage(message: MessageAllPlots, ctx: MessageContext): IMessage? {
        val thread = FMLCommonHandler.instance().getWorldThread(ctx.netHandler)
        if (thread.isCallingFromMinecraftThread) {
            process(message)
        } else {
            thread.addScheduledTask { process(message) }
        }
        return null
    }

    private fun process(message: MessageAllPlots) {
        proxy.plotManagerClient = PlotManagerClient(message.plots!!)
    }
}

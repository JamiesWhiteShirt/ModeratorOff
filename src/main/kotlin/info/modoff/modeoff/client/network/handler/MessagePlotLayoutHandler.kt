package info.modoff.modeoff.client.network.handler

import info.modoff.modeoff.client.ClientProxy
import info.modoff.modeoff.client.plot.PlotManagerClient
import info.modoff.modeoff.common.network.message.MessagePlotLayout
import net.minecraftforge.fml.common.FMLCommonHandler
import net.minecraftforge.fml.common.network.simpleimpl.IMessage
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext

class MessagePlotLayoutHandler(val proxy: ClientProxy) : IMessageHandler<MessagePlotLayout, IMessage> {
    override fun onMessage(message: MessagePlotLayout, ctx: MessageContext): IMessage? {
        val thread = FMLCommonHandler.instance().getWorldThread(ctx.netHandler)
        if (thread.isCallingFromMinecraftThread) {
            process(message)
        } else {
            thread.addScheduledTask { process(message) }
        }
        return null
    }

    private fun process(message: MessagePlotLayout) {
        proxy.plotManagerClient = PlotManagerClient(message.plotLayout!!)
    }
}

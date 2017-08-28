package info.modoff.modeoff.common.network.message

import info.modoff.modeoff.common.plot.PlotLayout
import io.netty.buffer.ByteBuf
import net.minecraftforge.fml.common.network.simpleimpl.IMessage

class MessagePlotLayout(var plotLayout: PlotLayout?) : IMessage {
    companion object {
        const val DISCRIMINATOR = 1
    }

    constructor() : this(null)

    override fun fromBytes(buf: ByteBuf) {
        plotLayout = PlotLayout.fromBytes(buf)
    }

    override fun toBytes(buf: ByteBuf) {
        plotLayout!!.toBytes(buf)
    }
}

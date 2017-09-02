package info.modoff.modeoff.common.network.message

import com.teamwizardry.librarianlib.features.kotlin.readVarInt
import com.teamwizardry.librarianlib.features.kotlin.writeVarInt
import info.modoff.modeoff.common.plot.Plot
import info.modoff.modeoff.common.plot.serialization.readPlotFromByteBuf
import info.modoff.modeoff.common.plot.serialization.writePlotToByteBuf
import io.netty.buffer.ByteBuf
import net.minecraftforge.fml.common.network.simpleimpl.IMessage

class MessageAllPlots(var plots: List<Plot>?) : IMessage {
    companion object {
        const val DISCRIMINATOR = 1
    }

    constructor() : this(null)

    override fun fromBytes(buf: ByteBuf) {
        plots = (0 until buf.readVarInt()).map {
            readPlotFromByteBuf(buf)
        }
    }

    override fun toBytes(buf: ByteBuf) {
        buf.writeVarInt(plots!!.size)
        plots!!.forEach {
            writePlotToByteBuf(buf, it)
        }
    }
}

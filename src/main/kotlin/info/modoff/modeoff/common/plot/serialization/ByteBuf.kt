package info.modoff.modeoff.common.plot.serialization

import info.modoff.modeoff.common.plot.Plot
import io.netty.buffer.ByteBuf
import net.minecraft.util.math.BlockPos
import java.util.*

fun readPlotFromByteBuf(buf: ByteBuf): Plot {
    return Plot(
        UUID(
            buf.readLong(),
            buf.readLong()
        ),
        BlockPos.fromLong(buf.readLong()),
        BlockPos.fromLong(buf.readLong())
    )
}

fun writePlotToByteBuf(buf: ByteBuf, plot: Plot) {
    buf.apply {
        writeLong(plot.uuid.mostSignificantBits)
        writeLong(plot.uuid.leastSignificantBits)
        writeLong(plot.min.toLong())
        writeLong(plot.max.toLong())
    }
}

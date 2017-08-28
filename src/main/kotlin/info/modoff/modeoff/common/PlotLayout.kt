package info.modoff.modeoff.common

import com.teamwizardry.librarianlib.features.kotlin.readString
import com.teamwizardry.librarianlib.features.kotlin.readVarInt
import com.teamwizardry.librarianlib.features.kotlin.writeString
import com.teamwizardry.librarianlib.features.kotlin.writeVarInt
import io.netty.buffer.ByteBuf

data class PlotLayout(
    val firstX: Int,
    val firstY: Int,
    val firstZ: Int,
    val rowDirection: String,
    val columnDirection: String,
    val gridRows: Int,
    val gridColumns: Int,
    val plotSize: Int,
    val marginWidth: Int,
    val worldDimensionID: Int
) {
    companion object {
        fun fromBytes(buf: ByteBuf): PlotLayout {
            return buf.run {
                PlotLayout(
                    readVarInt(),
                    readVarInt(),
                    readVarInt(),
                    readString(),
                    readString(),
                    readVarInt(),
                    readVarInt(),
                    readVarInt(),
                    readVarInt(),
                    readVarInt()
                )
            }
        }
    }

    fun toBytes(buf: ByteBuf) {
        buf.apply {
            writeVarInt(firstX)
            writeVarInt(firstY)
            writeVarInt(firstZ)
            writeString(rowDirection)
            writeString(columnDirection)
            writeVarInt(gridRows)
            writeVarInt(gridColumns)
            writeVarInt(plotSize)
            writeVarInt(marginWidth)
            writeVarInt(worldDimensionID)
        }
    }
}

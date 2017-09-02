package info.modoff.modeoff.common.plot.serialization

import info.modoff.modeoff.common.plot.Plot
import net.minecraft.nbt.CompressedStreamTools
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.nbt.NBTTagList
import net.minecraft.util.math.BlockPos
import net.minecraftforge.common.util.Constants
import java.io.File

fun readPlotFromNBT(compound: NBTTagCompound): Plot? {
    val uuid = compound.getUniqueId("uuid")
    return if (uuid != null) {
        val cornerA = compound.getCompoundTag("min")
        val cornerB = compound.getCompoundTag("max")
        Plot(uuid, readBlockPosFromNBT(cornerA), readBlockPosFromNBT(cornerB))
    } else {
        null
    }
}

fun writePlotToNBT(plot: Plot): NBTTagCompound = NBTTagCompound().apply {
    setUniqueId("uuid", plot.uuid)
    setTag("min", writeBlockPosToNBT(plot.min))
    setTag("max", writeBlockPosToNBT(plot.max))
}

fun readBlockPosFromNBT(compound: NBTTagCompound) = BlockPos(
    compound.getInteger("x"),
    compound.getInteger("y"),
    compound.getInteger("z")
)

fun writeBlockPosToNBT(pos: BlockPos) = NBTTagCompound().apply {
    setInteger("x", pos.x)
    setInteger("y", pos.y)
    setInteger("z", pos.z)
}

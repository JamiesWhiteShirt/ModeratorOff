package info.modoff.modeoff.common.plot.serialization

import info.modoff.modeoff.common.plot.Plot
import net.minecraft.nbt.CompressedStreamTools
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.nbt.NBTTagList
import net.minecraftforge.common.util.Constants
import java.io.File

fun readPlotsFromFile(file: File): List<Plot>? {
    val nbtData = CompressedStreamTools.read(file)
    val plotsTag = nbtData?.getTagList("plots", Constants.NBT.TAG_COMPOUND)
    return plotsTag
        ?.filterIsInstance<NBTTagCompound>()
        ?.mapNotNull(::readPlotFromNBT)
}

fun writePlotsToFile(file: File, plots: List<Plot>) {
    val compound = NBTTagCompound()
    compound.setTag("plots", NBTTagList().apply {
        plots.map(::writePlotToNBT).forEach(this::appendTag)
    })
    CompressedStreamTools.write(compound, file)
}

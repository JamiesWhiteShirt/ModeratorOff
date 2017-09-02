package info.modoff.modeoff.common.plot

import net.minecraft.util.math.BlockPos

abstract class PlotManager {
    abstract val allPlots: List<Plot>

    fun getPlotsForPosition(pos: BlockPos): List<Plot> {
        return allPlots.filter { plot ->
            (pos.x >= plot.min.x && pos.x < plot.max.x)
            && (pos.y >= plot.min.y && pos.y < plot.max.y)
            && (pos.z >= plot.min.z && pos.z < plot.max.z)
        }
    }
}

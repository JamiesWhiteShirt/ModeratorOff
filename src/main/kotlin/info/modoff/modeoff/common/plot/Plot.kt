package info.modoff.modeoff.common.plot

import info.modoff.modeoff.api.ConfigValues
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.BlockPos

/**
 * Created by LordSaad.
 */
class Plot(plotId: Int) {
    companion object {
        private fun getPlotPos(plotID: Int): BlockPos {
            val pos = BlockPos.MutableBlockPos(ConfigValues.firstPlotX, ConfigValues.firstPlotY, ConfigValues.firstPlotZ)
            pos.add(ConfigValues.plotSize / 2, ConfigValues.plotSize / 2, ConfigValues.plotSize / 2)

            val row = plotID / ConfigValues.plotGridRows
            val column = plotID % ConfigValues.plotGridColumns

            pos.move(EnumFacing.valueOf(ConfigValues.directionOfRows), row * ConfigValues.plotSize + row * ConfigValues.plotMarginWidth + ConfigValues.plotSize / 2)
            pos.move(EnumFacing.valueOf(ConfigValues.directionOfColumns), column * ConfigValues.plotSize + column * ConfigValues.plotMarginWidth + ConfigValues.plotSize / 2)
            return BlockPos(pos)
        }
    }


    val plotID: Int = plotId
    val corner1: BlockPos
    val corner2: BlockPos

    init {
        val pos = BlockPos.MutableBlockPos(ConfigValues.firstPlotX, ConfigValues.firstPlotY, ConfigValues.firstPlotZ)
        pos.add(ConfigValues.plotSize / 2, ConfigValues.plotSize / 2, ConfigValues.plotSize / 2)
        val row = plotID / ConfigValues.plotGridRows
        val column = plotID % ConfigValues.plotGridColumns
        pos.move(EnumFacing.valueOf(ConfigValues.directionOfRows), row * ConfigValues.plotSize + row * ConfigValues.plotMarginWidth)
        pos.move(EnumFacing.valueOf(ConfigValues.directionOfColumns), column * ConfigValues.plotSize + column * ConfigValues.plotMarginWidth)
        corner1 = BlockPos(pos.x - ConfigValues.plotSize / 2, pos.y, pos.z - ConfigValues.plotSize / 2)
        corner2 = BlockPos(pos.x + ConfigValues.plotSize / 2, pos.y, pos.z + ConfigValues.plotSize / 2)
    }

    fun teleportPlayerToCenter(player: EntityPlayer) {
        if (player.world.provider.dimension != ConfigValues.plotWorldDimensionID)
            player.changeDimension(ConfigValues.plotWorldDimensionID)

        val pos = getPlotPos(plotID)
        player.setPositionAndUpdate(pos.x + 0.5, (pos.y + 2).toDouble(), pos.z + 0.5)
    }
}

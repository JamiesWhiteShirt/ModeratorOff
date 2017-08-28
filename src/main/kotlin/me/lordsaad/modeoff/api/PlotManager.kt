package me.lordsaad.modeoff.api

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.BlockPos

import java.util.UUID

/**
 * Created by LordSaad.
 */
class PlotManager {
    companion object {
        fun teleportToPlot(player: EntityPlayer?, plotID: Int) {
            if (plotID < 0) return
            if (player == null) return

            if (player.world.provider.dimension != ConfigValues.plotWorldDimensionID)
                player.changeDimension(ConfigValues.plotWorldDimensionID)

            val pos = getPlotPos(plotID) ?: return

            player.setPositionAndUpdate(pos.x + 0.5, (pos.y + 2).toDouble(), pos.z + 0.5)
        }

        fun getPlotPos(plotID: Int): BlockPos? {
            if (plotID < 0) return null

            val pos = BlockPos.MutableBlockPos(ConfigValues.firstPlotX, ConfigValues.firstPlotY, ConfigValues.firstPlotZ)
            pos.add(ConfigValues.plotSize / 2, ConfigValues.plotSize / 2, ConfigValues.plotSize / 2)

            val row = plotID / ConfigValues.plotGridRows
            val column = plotID % ConfigValues.plotGridColumns

            pos.move(EnumFacing.valueOf(ConfigValues.directionOfRows), row * ConfigValues.plotSize + row * ConfigValues.plotMarginWidth + ConfigValues.plotSize / 2)
            pos.move(EnumFacing.valueOf(ConfigValues.directionOfColumns), column * ConfigValues.plotSize + column * ConfigValues.plotMarginWidth + ConfigValues.plotSize / 2)
            return BlockPos(pos)
        }
    }


    val uuid: UUID
    var plotID = -1
    val corner1: BlockPos
    val corner2: BlockPos

    constructor(uuid: UUID, plotId: Int) {
        this.uuid = uuid
        this.plotID = plotId

        val pos = BlockPos.MutableBlockPos(ConfigValues.firstPlotX, ConfigValues.firstPlotY, ConfigValues.firstPlotZ)
        pos.add(ConfigValues.plotSize / 2, ConfigValues.plotSize / 2, ConfigValues.plotSize / 2)

        val row = plotID / ConfigValues.plotGridRows
        val column = plotID % ConfigValues.plotGridColumns

        pos.move(EnumFacing.valueOf(ConfigValues.directionOfRows), row * ConfigValues.plotSize + row * ConfigValues.plotMarginWidth)
        pos.move(EnumFacing.valueOf(ConfigValues.directionOfColumns), column * ConfigValues.plotSize + column * ConfigValues.plotMarginWidth)
        corner1 = BlockPos(pos.x - ConfigValues.plotSize / 2, pos.y, pos.z - ConfigValues.plotSize / 2)
        corner2 = BlockPos(pos.x + ConfigValues.plotSize / 2, pos.y, pos.z + ConfigValues.plotSize / 2)
    }

    constructor(player: EntityPlayer) : this(player.uniqueID, PlotAssigningManager.getPlotForUUID(player.uniqueID))

    fun teleportPlayerToCenter(player: EntityPlayer) {
        teleportToPlot(player, plotID)
    }
}

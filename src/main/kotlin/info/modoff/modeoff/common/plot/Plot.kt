package info.modoff.modeoff.common.plot

import com.teamwizardry.librarianlib.features.kotlin.div
import com.teamwizardry.librarianlib.features.kotlin.plus
import info.modoff.modeoff.api.ConfigValues
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.math.BlockPos
import java.util.*

/**
 * Created by LordSaad.
 */
class Plot(val uuid: UUID, val min: BlockPos, val max: BlockPos) {
    fun teleportPlayerToCenter(player: EntityPlayer) {
        if (player.world.provider.dimension != ConfigValues.plotWorldDimensionID)
            player.changeDimension(ConfigValues.plotWorldDimensionID)

        val pos = (min + max) / 2
        player.setPositionAndUpdate(pos.x + 0.5, pos.y.toDouble(), pos.z + 0.5)
    }
}

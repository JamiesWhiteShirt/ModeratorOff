package info.modoff.modeoff.client

import com.google.common.collect.HashMultimap
import net.minecraft.block.state.IBlockState
import net.minecraft.init.Blocks
import net.minecraft.util.BlockRenderLayer
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3i
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World

import java.util.EnumMap

/**
 * Will cache the area from the crane selected.

 * @param world  The world object.
 * @param origin A block in the crane.
 * @param width  The width of the crane.
 * @param height The height of the crane.
 */
class AreaCacher(world: IBlockAccess, origin: BlockPos, width: Int, height: Int) {
    val blocks = EnumMap<BlockRenderLayer, HashMultimap<IBlockState, BlockPos>>(BlockRenderLayer::class.java)

    init {
        val air = Blocks.AIR.defaultState
        val fullWidth = width * 2

        /* val iterable = BlockPos.getAllInBoxMutable(origin.add(-width, 0, -width), origin.add(width, height, width))

        val newCache = iterable.map {
            if (world.isBlockLoaded(it)) {
                world.getBlockState(it)
            } else {
                air
            }
        }.toTypedArray()

        val solidsCache = newCache.map {
            it.isFullBlock && it.isOpaqueCube && it.isBlockNormalCube && it.isNormalCube && !it.isTranslucent
            && !it.material.isLiquid && it.material.isSolid
        }.toTypedArray() */

        val cache = Array(fullWidth) { xIndex ->
            val x = origin.x + xIndex - width
            Array(height) { yIndex ->
                val y = origin.y + yIndex
                Array(fullWidth) { zIndex ->
                    val z = origin.z + zIndex - width
                    val pos = BlockPos(x, y, z)

                    world.getBlockState(pos)
                }
            }
        }

        fun getCachedState(x: Int, y: Int, z: Int): IBlockState {
            return if (x < 0 || y < 0 || z < 0 || x >= fullWidth || y >= height || z >= fullWidth) {
                air
            } else {
                cache[x][y][z]
            }
        }

        // SECOND ITERATION
        // Check surrounding iterations
        for (x in 0..fullWidth - 1) {
            for (y in 0..height - 1) {
                for (z in 0..fullWidth - 1) {
                    val state = cache[x][y][z]

                    val surrounded = EnumFacing.VALUES.all { facing ->
                        getCachedState(x + facing.frontOffsetX, y + facing.frontOffsetY, z + facing.frontOffsetZ).run {
                            isFullBlock && isOpaqueCube && isBlockNormalCube && isNormalCube && !isTranslucent
                            && !material.isLiquid && material.isSolid
                        }
                    }

                    if (!surrounded) {
                        val pos = BlockPos(x, y, z).subtract(Vec3i(width, height, width)).add(origin)
                        val layer = state.block.blockLayer
                        blocks.getOrPut(layer, { HashMultimap.create() }).put(state, pos)
                    }
                }
            }
        }

        blocks.values.forEach {
            it.removeAll(air)
        }
    }
}

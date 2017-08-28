package info.modoff.modeoff.client.plot

import net.minecraft.block.state.IBlockState
import net.minecraft.init.Blocks
import net.minecraft.util.math.BlockPos
import net.minecraft.world.ChunkCache
import net.minecraft.world.World

/**
 * Created by Gegy
 */
class PlotChunkCache(world: World, from: BlockPos, to: BlockPos, subIn: Int) : ChunkCache(world, from, to, subIn) {
    private val min: BlockPos = BlockPos(Math.min(from.x, to.x), Math.min(from.y, to.y), Math.min(from.z, to.z))
    private val max: BlockPos = BlockPos(Math.max(from.x, to.x), Math.max(from.y, to.y), Math.max(from.z, to.z))

    override fun getBlockState(pos: BlockPos): IBlockState {
        if (pos.x >= min.x && pos.y >= min.y && pos.z >= min.z && pos.x < max.x && pos.y < max.y && pos.z < max.z) {
            return super.getBlockState(pos)
        }
        return Blocks.AIR.defaultState
    }
}

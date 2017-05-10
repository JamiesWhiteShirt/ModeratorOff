package me.lordsaad.modeoff.api;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.World;

/**
 * Created by Gegy
 */
public class PlotChunkCache extends ChunkCache {
	private final BlockPos min;
	private final BlockPos max;

	public PlotChunkCache(World world, BlockPos from, BlockPos to, int subIn) {
		super(world, from, to, subIn);
		min = new BlockPos(Math.min(from.getX(), to.getX()), Math.min(from.getY(), to.getY()), Math.min(from.getZ(), to.getZ()));
		max = new BlockPos(Math.max(from.getX(), to.getX()), Math.max(from.getY(), to.getY()), Math.max(from.getZ(), to.getZ()));
	}

	@Override
	public IBlockState getBlockState(BlockPos pos) {
		if (pos.getX() >= min.getX() && pos.getY() >= min.getY() && pos.getZ() >= min.getZ() && pos.getX() < max.getX() && pos.getY() < max.getY() && pos.getZ() < max.getZ()) {
			return super.getBlockState(pos);
		}
		return Blocks.AIR.getDefaultState();
	}
}

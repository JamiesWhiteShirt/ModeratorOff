package info.modoff.modeoff.util

import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.Side

val World.side: Side get() = if (isRemote) Side.CLIENT else Side.SERVER

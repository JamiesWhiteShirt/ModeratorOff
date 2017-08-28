package info.modoff.modeoff.common

import info.modoff.modeoff.Modeoff
import info.modoff.modeoff.api.PlotManager
import net.minecraft.util.math.BlockPos
import net.minecraftforge.event.entity.player.PlayerEvent
import net.minecraftforge.event.entity.player.PlayerInteractEvent
import net.minecraftforge.event.world.BlockEvent
import net.minecraftforge.fml.common.eventhandler.Event
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

/**
 * Created by LordSaad.
 */
class EventHandler {

    @SubscribeEvent
    fun leftClickBlock(event: PlayerInteractEvent.LeftClickBlock) {
        if (Modeoff.proxy.teamMembers.contains(event.entityPlayer.uniqueID)) return
        if (!Modeoff.proxy.contestants.contains(event.entityPlayer.uniqueID)) {
            event.useItem = Event.Result.DENY
            event.useBlock = Event.Result.DENY
            event.isCanceled = true
            return
        }
        val manager = PlotManager(event.entityPlayer)
        if (manager.plotID < 0) {
            event.useItem = Event.Result.DENY
            event.useBlock = Event.Result.DENY
            event.isCanceled = true
            return
        }
        if (manager.corner1 == null || manager.corner2 == null) {
            event.useItem = Event.Result.DENY
            event.useBlock = Event.Result.DENY
            event.isCanceled = true
            return
        }

        if (isWithinBounds(manager.corner1, manager.corner2, event.pos)) event.isCanceled = true
    }

    @SubscribeEvent
    fun onBreakBlock(event: BlockEvent.BreakEvent) {
        if (Modeoff.proxy.teamMembers.contains(event.player.uniqueID)) return
        if (!Modeoff.proxy.contestants.contains(event.player.uniqueID)) {
            event.isCanceled = true
            return
        }

        val manager = PlotManager(event.player)
        if (manager.plotID < 0) {
            event.isCanceled = true
            return
        }
        if (manager.corner1 == null || manager.corner2 == null) {
            event.isCanceled = true
            return
        }

        if (isWithinBounds(manager.corner1, manager.corner2, event.pos)) event.isCanceled = true
    }

    @SubscribeEvent
    fun breakSpeed(event: PlayerEvent.BreakSpeed) {
        if (Modeoff.proxy.teamMembers.contains(event.entityPlayer.uniqueID)) return
        if (!Modeoff.proxy.contestants.contains(event.entityPlayer.uniqueID)) {
            event.isCanceled = true
            return
        }
        val manager = PlotManager(event.entityPlayer)
        if (manager.plotID < 0) {
            event.isCanceled = true
            return
        }
        if (manager.corner1 == null || manager.corner2 == null) {
            event.isCanceled = true
            return
        }

        if (isWithinBounds(manager.corner1, manager.corner2, event.pos)) event.isCanceled = true
    }

    @SubscribeEvent
    fun place(event: BlockEvent.PlaceEvent) {
        if (Modeoff.proxy.teamMembers.contains(event.player.uniqueID)) return
        if (!Modeoff.proxy.contestants.contains(event.player.uniqueID)) {
            event.isCanceled = true
            return
        }

        val manager = PlotManager(event.player)
        if (manager.plotID < 0) {
            event.isCanceled = true
            return
        }
        if (manager.corner1 == null || manager.corner2 == null) {
            event.isCanceled = true
            return
        }

        if (isWithinBounds(manager.corner1, manager.corner2, event.pos)) event.isCanceled = true
    }

    private fun isWithinBounds(corner1: BlockPos, corner2: BlockPos, pos: BlockPos): Boolean {
        val xMin = Math.min(corner1.x, corner2.x)
        val xMax = Math.max(corner1.x, corner2.x)
        val zMin = Math.min(corner1.z, corner2.z)
        val zMax = Math.max(corner1.z, corner2.z)

        return pos.x < xMin || pos.x > xMax || pos.z < zMin || pos.z > zMax
    }
}

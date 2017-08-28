package info.modoff.modeoff.common

import info.modoff.modeoff.Modeoff
import info.modoff.modeoff.util.side
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.util.math.BlockPos
import net.minecraftforge.event.entity.player.PlayerEvent
import net.minecraftforge.event.entity.player.PlayerInteractEvent
import net.minecraftforge.event.world.BlockEvent
import net.minecraftforge.fml.common.eventhandler.Event
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.PlayerEvent as FMLPlayerEvent

/**
 * Created by LordSaad.
 */
class EventHandler {
    @SubscribeEvent
    fun leftClickBlock(event: PlayerInteractEvent.LeftClickBlock) {
        val plotManager = Modeoff.proxy.getPlotManager(event.side)
        if (plotManager != null) {
            val uuid = event.entityPlayer.uniqueID
            if (Modeoff.proxy.teamMembers.contains(uuid)) return
            if (!Modeoff.proxy.contestants.contains(uuid)) {
                event.useItem = Event.Result.DENY
                event.useBlock = Event.Result.DENY
                event.isCanceled = true
                return
            }
            val plot = plotManager.getPlotByAssignedUUID(uuid)
            if (plot != null) {
                event.useItem = Event.Result.DENY
                event.useBlock = Event.Result.DENY
                event.isCanceled = true
                return
            }
        }
    }

    @SubscribeEvent
    fun onBreakBlock(event: BlockEvent.BreakEvent) {
        val plotManager = Modeoff.proxy.getPlotManager(event.world.side)
        if (plotManager != null) {
            val uuid = event.player.uniqueID
            if (Modeoff.proxy.teamMembers.contains(uuid)) return
            if (!Modeoff.proxy.contestants.contains(uuid)) {
                event.isCanceled = true
                return
            }
            val plot = plotManager.getPlotByAssignedUUID(uuid)
            if (plot != null) {
                event.isCanceled = true
                return
            }
        }
    }

    @SubscribeEvent
    fun breakSpeed(event: PlayerEvent.BreakSpeed) {
        val plotManager = Modeoff.proxy.getPlotManager(event.entity.world.side)
        if (plotManager != null) {
            val uuid = event.entityPlayer.uniqueID
            if (Modeoff.proxy.teamMembers.contains(uuid)) return
            if (!Modeoff.proxy.contestants.contains(uuid)) {
                event.isCanceled = true
                return
            }
            val plot = plotManager.getPlotByAssignedUUID(uuid)
            if (plot != null) {
                event.isCanceled = true
                return
            }
        }
    }

    @SubscribeEvent
    fun place(event: BlockEvent.PlaceEvent) {
        val plotManager = Modeoff.proxy.getPlotManager(event.player.world.side)
        if (plotManager != null) {
            val uuid = event.player.uniqueID
            if (Modeoff.proxy.teamMembers.contains(uuid)) return
            if (!Modeoff.proxy.contestants.contains(uuid)) {
                event.isCanceled = true
                return
            }
            val plot = plotManager.getPlotByAssignedUUID(uuid)
            if (plot != null) {
                event.isCanceled = true
                return
            }
        }
    }

    private fun isWithinBounds(corner1: BlockPos, corner2: BlockPos, pos: BlockPos): Boolean {
        val xMin = Math.min(corner1.x, corner2.x)
        val xMax = Math.max(corner1.x, corner2.x)
        val zMin = Math.min(corner1.z, corner2.z)
        val zMax = Math.max(corner1.z, corner2.z)

        return pos.x < xMin || pos.x > xMax || pos.z < zMin || pos.z > zMax
    }

    @SubscribeEvent
    fun onPlayerLoggedIn(event: FMLPlayerEvent.PlayerLoggedInEvent) {
        Modeoff.plotManagerServer!!.sendToPlayer(event.player as EntityPlayerMP)
    }
}

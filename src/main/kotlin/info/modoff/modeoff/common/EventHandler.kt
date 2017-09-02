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
            val plots = plotManager.getPlotsForPosition(event.pos)
            if (plots.isNotEmpty()) {
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
            val plots = plotManager.getPlotsForPosition(event.pos)
            if (plots.isNotEmpty()) {
                event.isCanceled = true
                return
            }
        }
    }

    @SubscribeEvent
    fun breakSpeed(event: PlayerEvent.BreakSpeed) {
        val plotManager = Modeoff.proxy.getPlotManager(event.entity.world.side)
        if (plotManager != null) {
            val plots = plotManager.getPlotsForPosition(event.pos)
            if (plots.isNotEmpty()) {
                event.isCanceled = true
                return
            }
        }
    }

    @SubscribeEvent
    fun place(event: BlockEvent.PlaceEvent) {
        val plotManager = Modeoff.proxy.getPlotManager(event.player.world.side)
        if (plotManager != null) {
            val plots = plotManager.getPlotsForPosition(event.pos)
            if (plots.isNotEmpty()) {
                event.isCanceled = true
                return
            }
        }
    }

    @SubscribeEvent
    fun onPlayerLoggedIn(event: FMLPlayerEvent.PlayerLoggedInEvent) {
        Modeoff.plotManagerServer!!.sendToPlayer(event.player as EntityPlayerMP)
    }
}

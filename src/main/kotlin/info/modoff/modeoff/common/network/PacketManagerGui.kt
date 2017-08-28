package info.modoff.modeoff.common.network

import com.teamwizardry.librarianlib.features.network.PacketBase
import info.modoff.modeoff.Modeoff
import info.modoff.modeoff.api.PlotAssigningManager
import info.modoff.modeoff.api.PlotManager
import net.minecraft.client.Minecraft
import net.minecraft.util.math.BlockPos
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext

/**
 * Created by LordSaad.
 */
class PacketManagerGui : PacketBase() {
    override fun handle(ctx: MessageContext) {
        val pos = PlotManager.getPlotPos(PlotAssigningManager.getPlotForUUID(Minecraft.getMinecraft().player.uniqueID)) ?: return
        Minecraft.getMinecraft().player.openGui(Modeoff, 0, Minecraft.getMinecraft().player.world, pos.x, pos.y, pos.z)
    }
}

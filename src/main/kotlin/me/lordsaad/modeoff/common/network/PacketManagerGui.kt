package me.lordsaad.modeoff.common.network

import com.teamwizardry.librarianlib.features.network.PacketBase
import me.lordsaad.modeoff.Modeoff
import me.lordsaad.modeoff.api.PlotAssigningManager
import me.lordsaad.modeoff.api.PlotManager
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

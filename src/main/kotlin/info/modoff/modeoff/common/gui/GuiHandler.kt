package info.modoff.modeoff.common.gui

import info.modoff.modeoff.client.gui.GuiManagePlot
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.fml.common.network.IGuiHandler

/**
 * Created by LordSaad.
 */
class GuiHandler : IGuiHandler {
    override fun getServerGuiElement(ID: Int, player: EntityPlayer, world: World, x: Int, y: Int, z: Int): Any? {
        return when (ID) {
            ContainerManagePlot.ID -> ContainerManagePlot()
            else -> null
        }
    }

    override fun getClientGuiElement(ID: Int, player: EntityPlayer, world: World, x: Int, y: Int, z: Int): Any? {
        return when (ID) {
            ContainerManagePlot.ID -> GuiManagePlot(BlockPos(x, y, z))
            else -> null
        }
    }
}

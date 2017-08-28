package info.modoff.modeoff.common.gui

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.Container

class ContainerManagePlot : Container() {
    companion object {
        const val ID = 0
    }

    override fun canInteractWith(playerIn: EntityPlayer): Boolean {
        return true
    }
}

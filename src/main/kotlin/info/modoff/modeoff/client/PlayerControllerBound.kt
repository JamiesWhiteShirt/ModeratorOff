package info.modoff.modeoff.client

import net.minecraft.client.Minecraft
import net.minecraft.client.multiplayer.PlayerControllerMP

class PlayerControllerBound<T> private constructor(private val playerController: PlayerControllerMP, var value: T) {
    companion object {
        fun <T> create(data: T): PlayerControllerBound<T>? {
            return Minecraft.getMinecraft().playerController?.let {
                PlayerControllerBound(it, data)
            }
        }
    }

    /**
     * The value is bound by the player controller. If the player controller has changed, the bound server value is no
     * longer fresh.
     */
    val isFresh: Boolean get() = playerController == Minecraft.getMinecraft().playerController
}

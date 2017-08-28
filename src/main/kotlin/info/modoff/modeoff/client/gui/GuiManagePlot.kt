package info.modoff.modeoff.client.gui

import com.google.common.collect.HashMultimap
import com.teamwizardry.librarianlib.features.gui.GuiBase
import com.teamwizardry.librarianlib.features.gui.GuiComponent
import com.teamwizardry.librarianlib.features.gui.components.ComponentSprite
import com.teamwizardry.librarianlib.features.gui.components.ComponentVoid
import com.teamwizardry.librarianlib.features.kotlin.createCacheArrayAndReset
import com.teamwizardry.librarianlib.features.sprite.Sprite
import com.teamwizardry.librarianlib.features.sprite.Texture
import info.modoff.modeoff.Modeoff
import info.modoff.modeoff.api.AreaCacher
import info.modoff.modeoff.api.ConfigValues
import info.modoff.modeoff.api.PlotChunkCache
import net.minecraft.block.state.IBlockState
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.BlockRendererDispatcher
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.BufferBuilder
import net.minecraft.client.renderer.texture.TextureMap
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.util.BlockRenderLayer
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MathHelper
import net.minecraft.world.ChunkCache
import net.minecraft.world.IBlockAccess
import org.lwjgl.opengl.GL11

import java.util.EnumMap

/**
 * Created by LordSaad.
 */
class GuiManagePlot(pos: BlockPos) : GuiBase(512, 512) {
    companion object {
        private val textureBackground = Texture(ResourceLocation(Modeoff.MOD_ID, "textures/gui/plot_gui.png"))
        private val spriteBackground = textureBackground.getSprite("bg", 166, 256)
    }

    private val blocks = EnumMap<BlockRenderLayer, HashMultimap<IBlockState, BlockPos>>(BlockRenderLayer::class.java)
    private val vboCaches = EnumMap<BlockRenderLayer, IntArray>(BlockRenderLayer::class.java)

    private var tick = 0.0

    private var animSideTickPassed = 0f
    private val animTickMax = 180f
    private var animSideRotation: Float = 0.toFloat()

    private var areaCacher: AreaCacher? = null

    init {

        val width = ConfigValues.plotSize + ConfigValues.plotMarginWidth
        val height = 64

        val blockAccess = PlotChunkCache(Minecraft.getMinecraft().world, pos.add(-width, -height, -width), pos.add(width, height, width), 0)

        cache(blockAccess, pos, width, height)

        val compBackground = ComponentSprite(spriteBackground, 512 / 2 - 332 / 2, 0, 332, 512)
        mainComponents.add(compBackground)

        val boxing2 = ComponentVoid(0, 0, 512, 256)
        val sideView = ComponentVoid(0, 0, 512, 256)

        boxing2.add(sideView)
        //ScissorMixin.INSTANCE.scissor(sideView);

        val tileSideSize = (width / 10).toDouble()

        sideView.BUS.hook(GuiComponent.ComponentTickEvent::class.java) { event ->
            //if (animSideRotation % 60 <= 0.1) cache(blockAccess, pos, width, height);

            if (animSideRotation >= 360) animSideRotation = 0f

            if (animSideTickPassed < animTickMax) {
                if (animSideTickPassed <= animTickMax - 50) animSideTickPassed++

                val p = 0.03f
                val x = animSideTickPassed / animTickMax
                if (x < p) {
                    animSideRotation += (MathHelper.sin((x * Math.PI / p - Math.PI / 2).toFloat()) + 1) / 2.0f
                } else {
                    animSideRotation += (MathHelper.sin(((x - p) * Math.PI / (1 - p) + Math.PI / 2).toFloat()) + 1) / 2.0f
                }
            }
        }

        sideView.BUS.hook(GuiComponent.PostDrawEvent::class.java) { event ->
            if (tick >= 360 * 2) {
                tick = 0.0
            } else {
                tick++
            }

            val horizontalAngle = 40
            val verticalAngle = 45

            GlStateManager.matrixMode(GL11.GL_MODELVIEW)
            GlStateManager.shadeModel(GL11.GL_SMOOTH)

            GlStateManager.pushMatrix()
            GlStateManager.enableCull()

            GlStateManager.translate((256 - width / 2).toFloat(), (128 - height / 2).toFloat(), 5000f)
            GlStateManager.rotate(horizontalAngle * (animSideTickPassed / animTickMax), -1f, 0f, 0f)
            GlStateManager.rotate(animSideRotation * 10, 0f, 1f, 0f)
            //GlStateManager.rotate((float) ((tick + event.getPartialTicks()) / 2), 0, 1, 0);
            GlStateManager.translate(tileSideSize * (animSideTickPassed / animTickMax), -tileSideSize * (animSideTickPassed / animTickMax), tileSideSize * (animSideTickPassed / animTickMax))
            GlStateManager.scale(tileSideSize * (animSideTickPassed / animTickMax), -tileSideSize * (animSideTickPassed / animTickMax), tileSideSize * (animSideTickPassed / animTickMax))

            GlStateManager.translate(-0.5, -0.5, -0.5)

            mc.renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE)
            for (layer in blocks.keys) {
                val tes = Tessellator.getInstance()
                val buffer = tes.buffer

                buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK)
                buffer.addVertexData(vboCaches[layer])
                tes.draw()
            }

            GlStateManager.popMatrix()
        }

        mainComponents.add(boxing2)
    }

    override fun doesGuiPauseGame(): Boolean {
        return false
    }

    fun cache(blockAccess: IBlockAccess, pos: BlockPos, width: Int, height: Int) {
        blocks.clear()
        areaCacher = AreaCacher(blockAccess, pos, width, height)
        blocks.putAll(areaCacher!!.blocks)
        vboCaches.clear()

        for (layer in blocks.keys) {
            val tes = Tessellator.getInstance()
            val buffer = tes.buffer
            val dispatcher = Minecraft.getMinecraft().blockRendererDispatcher

            if (vboCaches[layer] == null) {
                buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK)
                buffer.setTranslation((-pos.x).toDouble(), (-pos.y).toDouble(), (-pos.z).toDouble())

                for (state2 in blocks[layer]!!.keySet()) {
                    for (pos2 in blocks[layer]!!.get(state2)) {
                        dispatcher.renderBlock(state2, pos2, blockAccess, buffer)
                    }
                }

                vboCaches.put(layer, buffer.createCacheArrayAndReset())
            }
        }
    }
}

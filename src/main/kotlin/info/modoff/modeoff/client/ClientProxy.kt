package info.modoff.modeoff.client

import info.modoff.modeoff.Modeoff
import info.modoff.modeoff.client.network.handler.MessagePlotLayoutHandler
import info.modoff.modeoff.client.plot.PlotManagerClient
import info.modoff.modeoff.common.CommonProxy
import info.modoff.modeoff.common.plot.PlotLayout
import info.modoff.modeoff.common.network.message.MessagePlotLayout
import info.modoff.modeoff.common.plot.Plot
import info.modoff.modeoff.common.plot.PlotManager
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.world.WorldEvent
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper
import net.minecraftforge.fml.relauncher.Side

/**
 * Created by LordSaad.
 */
class ClientProxy : CommonProxy() {
    override fun preInit(event: FMLPreInitializationEvent) {
        super.preInit(event)
        MinecraftForge.EVENT_BUS.register(this)
    }

    override fun init(event: FMLInitializationEvent) {
        super.init(event)
    }

    override fun postInit(event: FMLPostInitializationEvent) {
        super.postInit(event)
    }

    override fun registerMessages(messageHandler: SimpleNetworkWrapper) {
        messageHandler.registerMessage(MessagePlotLayoutHandler(this), MessagePlotLayout::class.java, MessagePlotLayout.DISCRIMINATOR, Side.CLIENT)
    }

    override fun getPlotManager(side: Side): PlotManager? {
        return when (side) {
            Side.CLIENT -> plotManagerClient
            Side.SERVER -> Modeoff.plotManagerServer
        }
    }

    var plotManagerClient: PlotManagerClient?
    get() = boundClientPlotManager!!.value
    set(value) {
        boundClientPlotManager!!.value = value
    }

    private var boundClientPlotManager: PlayerControllerBound<PlotManagerClient?>? = null

    @SubscribeEvent
    fun onWorldLoad(event: WorldEvent.Load) {
        if (event.world.isRemote) {
            if (!(boundClientPlotManager?.isFresh ?: false)) {
                boundClientPlotManager = PlayerControllerBound.create(null)
            }
        }
    }

    val FORCEFIELD_TEXTURES = ResourceLocation("textures/misc/forcefield.png")

    @SubscribeEvent
    fun onRenderWorldLast(event: RenderWorldLastEvent) {
        val plotManager = plotManagerClient
        if (plotManager != null) {
            val tessellator = Tessellator.getInstance()
            val bufferbuilder = tessellator.buffer
            val player = Minecraft.getMinecraft().player!!
            val partialTicks = event.partialTicks

            val x = player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTicks
            val y = player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTicks
            val z = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTicks
            GlStateManager.enableBlend()
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO)
            Minecraft.getMinecraft().renderEngine.bindTexture(FORCEFIELD_TEXTURES)
            GlStateManager.depthMask(false)
            GlStateManager.pushMatrix()
            GlStateManager.color(0.5f, 0.5f, 0.5f, 0.25f)
            GlStateManager.enablePolygonOffset()
            GlStateManager.alphaFunc(516, 0.1f)
            GlStateManager.enableAlpha()
            GlStateManager.disableCull()
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX)

            val layout = plotManager.layout
            val plotSize = layout.plotSize.toDouble()
            for (plotId in 0 until (layout.gridColumns * layout.gridRows)) {
                val pos = Plot.getPlotPos(plotId)
                bufferbuilder.setTranslation(-x + pos.x, -y + pos.y, -z + pos.z)

                bufferbuilder.pos(0.0, 256.0, 0.0).tex(0.0, 256.0).endVertex()
                bufferbuilder.pos(0.0, 256.0, plotSize).tex(plotSize, 256.0).endVertex()
                bufferbuilder.pos(0.0, 0.0, plotSize).tex(plotSize, 0.0).endVertex()
                bufferbuilder.pos(0.0, 0.0, 0.0).tex(0.0, 0.0).endVertex()

                bufferbuilder.pos(plotSize, 256.0, 0.0).tex(plotSize, 256.0).endVertex()
                bufferbuilder.pos(plotSize, 256.0, plotSize).tex(0.0, 256.0).endVertex()
                bufferbuilder.pos(plotSize, 0.0, plotSize).tex(0.0, 0.0).endVertex()
                bufferbuilder.pos(plotSize, 0.0, 0.0).tex(plotSize, 0.0).endVertex()

                bufferbuilder.pos(0.0, 256.0, 0.0).tex(plotSize, 256.0).endVertex()
                bufferbuilder.pos(plotSize, 256.0, 0.0).tex(0.0, 256.0).endVertex()
                bufferbuilder.pos(plotSize, 0.0, 0.0).tex(0.0, 0.0).endVertex()
                bufferbuilder.pos(0.0, 0.0, 0.0).tex(plotSize, 0.0).endVertex()

                bufferbuilder.pos(0.0, 256.0, plotSize).tex(0.0, 256.0).endVertex()
                bufferbuilder.pos(plotSize, 256.0, plotSize).tex(plotSize, 256.0).endVertex()
                bufferbuilder.pos(plotSize, 0.0, plotSize).tex(plotSize, 0.0).endVertex()
                bufferbuilder.pos(0.0, 0.0, plotSize).tex(0.0, 0.0).endVertex()
            }

            tessellator.draw()
            bufferbuilder.setTranslation(0.0, 0.0, 0.0)
            GlStateManager.enableCull()
            GlStateManager.disableAlpha()
            GlStateManager.disablePolygonOffset()
            GlStateManager.enableAlpha()
            GlStateManager.disableBlend()
            GlStateManager.popMatrix()
            GlStateManager.depthMask(true)
        }
    }
}

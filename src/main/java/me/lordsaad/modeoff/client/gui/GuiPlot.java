package me.lordsaad.modeoff.client.gui;

import com.google.common.collect.HashMultimap;
import com.teamwizardry.librarianlib.features.gui.GuiBase;
import com.teamwizardry.librarianlib.features.gui.GuiComponent;
import com.teamwizardry.librarianlib.features.gui.components.ComponentSprite;
import com.teamwizardry.librarianlib.features.gui.components.ComponentVoid;
import com.teamwizardry.librarianlib.features.kotlin.ClientUtilMethods;
import com.teamwizardry.librarianlib.features.sprite.Sprite;
import com.teamwizardry.librarianlib.features.sprite.Texture;
import me.lordsaad.modeoff.Modeoff;
import me.lordsaad.modeoff.api.AreaCacher;
import me.lordsaad.modeoff.api.ConfigValues;
import me.lordsaad.modeoff.api.PlotChunkCache;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;

import java.util.EnumMap;

/**
 * Created by LordSaad.
 */
public class GuiPlot extends GuiBase {

	private static final Texture textureBackground = new Texture(new ResourceLocation(Modeoff.MOD_ID, "textures/gui/plot_gui.png"));
	private static final Sprite spriteBackground = textureBackground.getSprite("bg", 166, 256);

	private static Minecraft mc = Minecraft.getMinecraft();

	private EnumMap<BlockRenderLayer, HashMultimap<IBlockState, BlockPos>> blocks = new EnumMap<>(BlockRenderLayer.class);
	private EnumMap<BlockRenderLayer, int[]> vboCaches = new EnumMap<>(BlockRenderLayer.class);

	private double tick = 0;

	private float animSideTickPassed = 0;
	private float animTickMax = 180;
	private float animSideRotation;

	private AreaCacher areaCacher;

	public GuiPlot(BlockPos pos) {
		super(512, 512);

		int width = ConfigValues.plotSize + ConfigValues.plotMarginWidth;
		int height = 64;

		ChunkCache blockAccess = new PlotChunkCache(mc.world, pos.add(-width, -height, -width), pos.add(width, height, width), 0);

		cache(blockAccess, pos, width, height);

		ComponentSprite compBackground = new ComponentSprite(spriteBackground, (512 / 2) - (332 / 2), 0, 332, 512);
		getMainComponents().add(compBackground);

		ComponentVoid boxing2 = new ComponentVoid(0, 0, 512, 256);
		ComponentVoid sideView = new ComponentVoid(0, 0, 512, 256);

		boxing2.add(sideView);
		//ScissorMixin.INSTANCE.scissor(sideView);

		double tileSideSize = width / 10;

		sideView.BUS.hook(GuiComponent.ComponentTickEvent.class, (event) -> {
			//if (animSideRotation % 60 <= 0.1) cache(blockAccess, pos, width, height);

			if (animSideRotation >= 360) animSideRotation = 0;

			if (animSideTickPassed < animTickMax) {
				if (animSideTickPassed <= animTickMax - 50) animSideTickPassed++;

				float p = 0.03f;
				float x = animSideTickPassed / animTickMax;
				if (x < p) {
					animSideRotation += (MathHelper.sin((float) (((x * Math.PI) / p) - Math.PI / 2)) + 1) / 2.0f;
				} else {
					animSideRotation += (MathHelper.sin((float) ((((x - p) * Math.PI) / (1 - p)) + Math.PI / 2)) + 1) / 2.0f;
				}
			}
		});

		sideView.BUS.hook(GuiComponent.PostDrawEvent.class, (event) -> {
			if (tick >= 360 * 2) {
				tick = 0;
			} else {
				tick++;
			}

			int horizontalAngle = 40;
			int verticalAngle = 45;

			GlStateManager.matrixMode(GL11.GL_MODELVIEW);
			GlStateManager.shadeModel(GL11.GL_SMOOTH);

			GlStateManager.pushMatrix();
			GlStateManager.enableCull();

			GlStateManager.translate(256 - (width / 2), 128 - (height / 2), 5000);
			GlStateManager.rotate(horizontalAngle * (animSideTickPassed / animTickMax), -1, 0, 0);
			GlStateManager.rotate(animSideRotation * 10, 0, 1, 0);
			//GlStateManager.rotate((float) ((tick + event.getPartialTicks()) / 2), 0, 1, 0);
			GlStateManager.translate(tileSideSize * (animSideTickPassed / animTickMax), -tileSideSize * (animSideTickPassed / animTickMax), tileSideSize * (animSideTickPassed / animTickMax));
			GlStateManager.scale(tileSideSize * (animSideTickPassed / animTickMax), -tileSideSize * (animSideTickPassed / animTickMax), tileSideSize * (animSideTickPassed / animTickMax));

			GlStateManager.translate(-0.5, -0.5, -0.5);

			mc.renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
			for (BlockRenderLayer layer : blocks.keySet()) {
				Tessellator tes = Tessellator.getInstance();
				VertexBuffer buffer = tes.getBuffer();

				buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
				buffer.addVertexData(vboCaches.get(layer));
				tes.draw();
			}

			GlStateManager.popMatrix();
		});

		getMainComponents().add(boxing2);
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	public void cache(IBlockAccess blockAccess, BlockPos pos, int width, int height) {
		blocks.clear();
		areaCacher = new AreaCacher(mc.world, pos, width, height);
		blocks.putAll(areaCacher.blocks);
		vboCaches.clear();

		for (BlockRenderLayer layer : blocks.keySet()) {
			Tessellator tes = Tessellator.getInstance();
			VertexBuffer buffer = tes.getBuffer();
			BlockRendererDispatcher dispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();

			if (vboCaches.get(layer) == null) {
				buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
				buffer.setTranslation(-pos.getX(), -pos.getY(), -pos.getZ());

				for (IBlockState state2 : blocks.get(layer).keySet()) {
					for (BlockPos pos2 : blocks.get(layer).get(state2)) {
						dispatcher.renderBlock(state2, pos2, blockAccess, buffer);
					}
				}

				vboCaches.put(layer, ClientUtilMethods.createCacheArrayAndReset(buffer));
			}
		}
	}
}

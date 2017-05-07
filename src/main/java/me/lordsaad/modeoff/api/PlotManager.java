package me.lordsaad.modeoff.api;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * Created by LordSaad.
 */
public class PlotManager {

	@NotNull
	private UUID uuid;
	@NotNull
	private EntityPlayer player;
	private int plotID = -1;

	public PlotManager(@NotNull EntityPlayer player) {
		this.uuid = player.getUniqueID();
		this.player = player;

		PlotAssigningManager manager = PlotAssigningManager.INSTANCE;
		plotID = manager.getPlotForUUID(uuid);
	}

	public void teleportToCenter() {
		if (plotID < 0) return;

		if (player.world.provider.getDimension() != ConfigValues.plotWorldDimensionID)
			player.changeDimension(ConfigValues.plotWorldDimensionID);

		BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(ConfigValues.x, ConfigValues.y, ConfigValues.z);
		pos.add(ConfigValues.plotSize / 2, ConfigValues.plotSize / 2, ConfigValues.plotSize / 2);

		int row = plotID / ConfigValues.gridRows;
		int column = plotID % ConfigValues.gridColumns;

		pos.move(EnumFacing.valueOf(ConfigValues.directionOfRows), row * ConfigValues.plotSize + row * ConfigValues.plotMarginWidth);
		pos.move(EnumFacing.valueOf(ConfigValues.directionOfColumns), column * ConfigValues.plotSize + column * ConfigValues.plotMarginWidth);
		player.setPositionAndUpdate(pos.getX() + 0.5, pos.getY() + 2, pos.getZ() + 0.5);
	}
}

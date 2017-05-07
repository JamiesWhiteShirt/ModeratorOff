package me.lordsaad.modeoff.api;

import net.minecraft.entity.player.EntityPlayer;
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
		if (player.world.provider.getDimension() != ConfigValues.plotWorldDimensionID)
			player.changeDimension(ConfigValues.plotWorldDimensionID);

		BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(ConfigValues.x, ConfigValues.y, ConfigValues.z);
		//pos.move()
		//player.setPositionAndUpdate();
	}
}

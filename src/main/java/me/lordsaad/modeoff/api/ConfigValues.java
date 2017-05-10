package me.lordsaad.modeoff.api;

import com.teamwizardry.librarianlib.features.config.ConfigPropertyInt;
import com.teamwizardry.librarianlib.features.config.ConfigPropertyString;
import me.lordsaad.modeoff.Modeoff;

/**
 * Created by LordSaad.
 */
public final class ConfigValues {

	@ConfigPropertyInt(modid = Modeoff.MOD_ID, category = "general", id = "first_plot_x", comment = "The corner of the first plot and will expand to the direction you set below", defaultValue = 0)
	public static int x;
	@ConfigPropertyInt(modid = Modeoff.MOD_ID, category = "general", id = "first_plot_y", comment = "The corner of the first plot and will expand to the direction you set below", defaultValue = 0)
	public static int y;
	@ConfigPropertyInt(modid = Modeoff.MOD_ID, category = "general", id = "first_plot_z", comment = "The corner of the first plot and will expand to the direction you set below", defaultValue = 0)
	public static int z;
	@ConfigPropertyString(modid = Modeoff.MOD_ID, category = "general", id = "direction_of_rows", comment = "What direction the rows are assigned in", defaultValue = "EAST")
	public static String directionOfRows;
	@ConfigPropertyString(modid = Modeoff.MOD_ID, category = "general", id = "direction_of_columns", comment = "What direction the columns are assigned in", defaultValue = "SOUTH")
	public static String directionOfColumns;
	@ConfigPropertyInt(modid = Modeoff.MOD_ID, category = "general", id = "plot_grid_rows", comment = "The amount of grid rows that exist on the X axis of the plot grid", defaultValue = 4)
	public static int gridRows;
	@ConfigPropertyInt(modid = Modeoff.MOD_ID, category = "general", id = "plot_grid_columns", comment = "The amount of grid columns that exist on the Y axis of the plot grid", defaultValue = 4)
	public static int gridColumns;
	@ConfigPropertyInt(modid = Modeoff.MOD_ID, category = "general", id = "plot_size", comment = "The size of each plot", defaultValue = 32)
	public static int plotSize;
	@ConfigPropertyInt(modid = Modeoff.MOD_ID, category = "general", id = "plot_margin_width", comment = "The width of the space between plots", defaultValue = 4)
	public static int plotMarginWidth;
	@ConfigPropertyInt(modid = Modeoff.MOD_ID, category = "general", id = "plot_world_dimension_id", comment = "The ID of the dimension the world the plots are in", defaultValue = 0)
	public static int plotWorldDimensionID;
	@ConfigPropertyString(modid = Modeoff.MOD_ID, category = "network", id = "contestants_url", comment = "A url that leads to a raw json of the contestants", defaultValue = "http://modoff.info/")
	public static String urlContestants;
	@ConfigPropertyString(modid = Modeoff.MOD_ID, category = "network", id = "team_url", comment = "A url that leads to a raw json of the team members", defaultValue = "http://modoff.info/")
	public static String urlTeam;
}

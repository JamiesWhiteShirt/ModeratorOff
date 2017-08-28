package info.modoff.modeoff.api

import com.teamwizardry.librarianlib.features.config.ConfigProperty

/**
 * Created by LordSaad.
 */
object ConfigValues {
    @ConfigProperty(category = "general", comment = "The corner of the first plot and will expand to the direction you set below")
    var firstPlotX: Int = 0
    @ConfigProperty(category = "general", comment = "The corner of the first plot and will expand to the direction you set below")
    var firstPlotY: Int = 0
    @ConfigProperty(category = "general", comment = "The corner of the first plot and will expand to the direction you set below")
    var firstPlotZ: Int = 0
    @ConfigProperty(category = "general", comment = "What direction the rows are assigned in")
    var directionOfRows: String = "EAST"
    @ConfigProperty(category = "general", comment = "What direction the columns are assigned in")
    var directionOfColumns: String = "SOUTH"
    @ConfigProperty(category = "general", comment = "The amount of grid rows that exist on the X axis of the plot grid")
    var plotGridRows: Int = 4
    @ConfigProperty(category = "general", comment = "The amount of grid columns that exist on the Y axis of the plot grid")
    var plotGridColumns: Int = 4
    @ConfigProperty(category = "general", comment = "The size of each plot")
    var plotSize: Int = 32
    @ConfigProperty(category = "general", comment = "The width of the space between plots")
    var plotMarginWidth: Int = 4
    @ConfigProperty(category = "general", comment = "The ID of the dimension the world the plots are in")
    var plotWorldDimensionID: Int = 0
    @ConfigProperty(category = "network", comment = "A url that leads to a raw json of the contestants")
    var urlContestants: String = "http://modoff.info/"
    @ConfigProperty(category = "network", comment = "A url that leads to a raw json of the team members")
    var urlTeam: String = "http://modoff.info/"
}

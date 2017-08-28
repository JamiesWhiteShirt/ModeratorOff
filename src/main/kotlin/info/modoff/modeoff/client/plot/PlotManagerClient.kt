package info.modoff.modeoff.client.plot

import info.modoff.modeoff.api.Plot
import info.modoff.modeoff.common.plot.PlotLayout
import info.modoff.modeoff.common.plot.PlotManager
import java.util.*

class PlotManagerClient(layout: PlotLayout) : PlotManager(layout) {
    override fun getPlotByAssignedUUID(uuid: UUID): Plot? {
        return null
    }
}

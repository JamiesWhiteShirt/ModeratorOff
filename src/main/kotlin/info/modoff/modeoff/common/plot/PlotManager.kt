package info.modoff.modeoff.common.plot

import java.util.*

abstract class PlotManager(val layout: PlotLayout) {
    abstract fun getPlotByAssignedUUID(uuid: UUID): Plot?
}

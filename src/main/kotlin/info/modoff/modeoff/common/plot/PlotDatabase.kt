package info.modoff.modeoff.common.plot

class PlotDatabase(list: List<Plot>) {
    private val byUuid = list.associate { it.uuid to it }.toMutableMap()

    val all get() = byUuid.values.toList()
}

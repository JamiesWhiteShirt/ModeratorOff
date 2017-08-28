package info.modoff.modeoff.common.command

import info.modoff.modeoff.api.Plot
import info.modoff.modeoff.common.plot.PlotManagerServer
import net.minecraft.command.*
import net.minecraft.server.MinecraftServer
import net.minecraft.util.math.BlockPos

/**
 * Created by LordSaad.
 */
class CommandTpPlot(val plotManager: PlotManagerServer) : CommandBase() {

    override fun getName() = "plot_tp"

    override fun getUsage(sender: ICommandSender) = "commands.modeoff.plot_tp.usage"

    @Throws(CommandException::class)
    override fun execute(server: MinecraftServer, sender: ICommandSender, args: Array<String>) {
        //if (!(sender instanceof EntityPlayer)) {
        //	sender.sendMessage(new TextComponentString(TextFormatting.RED + "You do not have permission to use this command."));
        //	return;
        //}
        //
        //if (!CommonProxy.teamMembers.contains(((EntityPlayer) sender).getUniqueID()) || !CommonProxy.contestants.contains(((EntityPlayer) sender).getUniqueID())) {
        //	sender.sendMessage(new TextComponentString(TextFormatting.RED + "You do not have permission to use this command."));
        //	return;
        //}

        when (args.size) {
            0 -> {
                val player = getCommandSenderAsPlayer(sender)
                plotManager.getPlotByAssignedUUID(player.uniqueID)?.teleportPlayerToCenter(player)
            }
            1 -> {
                val (plotIdOrPlayer) = args
                getPlotByIdOrAssignedPlayer(server, sender, plotIdOrPlayer)?.teleportPlayerToCenter(getCommandSenderAsPlayer(sender))
            }
            2 -> {
                val (playerName, plotIdOrPlayer) = args
                val player = getPlayer(server, sender, playerName)

                getPlotByIdOrAssignedPlayer(server, sender, plotIdOrPlayer)?.teleportPlayerToCenter(player)
            }
            else -> throw WrongUsageException(getUsage(sender))
        }
    }

    private fun getPlotByIdOrAssignedPlayer(server: MinecraftServer, sender: ICommandSender, idOrPlayer: String): Plot? {
        return try {
            val plotID = Integer.parseInt(idOrPlayer)
            Plot(plotID)
        } catch (e: NumberFormatException) {
            val player = getPlayer(server, sender, idOrPlayer)
            plotManager.getPlotByAssignedUUID(player.uniqueID)
        }
    }

    override fun getTabCompletions(server: MinecraftServer, sender: ICommandSender, args: Array<String>, targetPos: BlockPos?): List<String> {
        return if (args.isNotEmpty()) getListOfStringsMatchingLastWord(args, *server.onlinePlayerNames) else emptyList<String>()
    }
}

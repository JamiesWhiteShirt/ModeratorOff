package info.modoff.modeoff.common.command

import info.modoff.modeoff.api.PlotAssigningManager
import info.modoff.modeoff.api.PlotManager
import net.minecraft.command.*
import net.minecraft.server.MinecraftServer
import net.minecraft.util.math.BlockPos

/**
 * Created by LordSaad.
 */
class CommandTpPlot : CommandBase() {

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
                PlotManager(player).teleportPlayerToCenter(player)
            }
            1 -> {
                val (plot) = args
                val plotManager = getPlotManagerByPlotIdOrPlayer(server, sender, plot)
                PlotManager.teleportToPlot(getCommandSenderAsPlayer(sender), plotManager.plotID)
            }
            2 -> {
                val (player, plot) = args
                val targetPlayer = getPlayer(server, sender, player)
                val plotManager = getPlotManagerByPlotIdOrPlayer(server, sender, plot)

                PlotManager.teleportToPlot(targetPlayer, plotManager.plotID)
            }
            else -> throw WrongUsageException(getUsage(sender))
        }
    }

    private fun getPlotManagerByPlotIdOrPlayer(server: MinecraftServer, sender: ICommandSender, plotIdOrPlayer: String): PlotManager {
        return try {
            val plotID = Integer.parseInt(plotIdOrPlayer)
            PlotManager(PlotAssigningManager.getUUIDForPlot(plotID)!!, plotID)
        } catch (e: NumberFormatException) {
            PlotManager(getPlayer(server, sender, plotIdOrPlayer))
        }
    }

    override fun getTabCompletions(server: MinecraftServer, sender: ICommandSender, args: Array<String>, targetPos: BlockPos?): List<String> {
        return if (args.isNotEmpty()) getListOfStringsMatchingLastWord(args, *server.onlinePlayerNames) else emptyList<String>()
    }
}

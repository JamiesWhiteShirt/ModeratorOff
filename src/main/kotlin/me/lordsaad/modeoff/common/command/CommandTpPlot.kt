package me.lordsaad.modeoff.common.command

import me.lordsaad.modeoff.api.PlotManager
import net.minecraft.command.*
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.server.MinecraftServer
import net.minecraft.util.math.BlockPos

/**
 * Created by LordSaad.
 */
class CommandTpPlot : CommandBase() {

    override fun getName() = "plot_tp"

    override fun getUsage(sender: ICommandSender) = "/plot_tp [username/plotID] [username/plotID]"

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
                PlotManager(CommandBase.getCommandSenderAsPlayer(sender)).teleportToCenter()
            }
            1 -> {
                val (plot) = args
                val plotManager = getPlotManagerByPlotIdOrPlayer(server, sender, plot)
                PlotManager.teleportToPlot(CommandBase.getCommandSenderAsPlayer(sender), plotManager.plotID)
            }
            2 -> {
                val (player, plot) = args
                val targetPlayer = getPlayerByPlotIdOrPlayer(server,sender, player)
                val plotManager = getPlotManagerByPlotIdOrPlayer(server, sender, plot)

                PlotManager.teleportToPlot(targetPlayer, plotManager.plotID)
            }
            else -> throw WrongUsageException(getUsage(sender))
        }
    }

    private fun getPlotManagerByPlotIdOrPlayer(server: MinecraftServer, sender: ICommandSender, plotIdOrPlayer: String): PlotManager {
        return try {
            val plotID = Integer.parseInt(plotIdOrPlayer)
            PlotManager(sender.entityWorld, plotID)
        } catch (e: NumberFormatException) {
            PlotManager(CommandBase.getPlayer(server, sender, plotIdOrPlayer))
        }
    }

    private fun getPlayerByPlotIdOrPlayer(server: MinecraftServer, sender: ICommandSender, plotIdOrPlayer: String): EntityPlayer {
        return try {
            val plotID = Integer.parseInt(plotIdOrPlayer)
            val manager = PlotManager(sender.entityWorld, plotID)
            manager.player ?: throw PlayerNotFoundException("Could not find player of plot id " + plotID, sender)
        } catch (e: NumberFormatException) {
            CommandBase.getPlayer(server, sender, plotIdOrPlayer)
        }
    }

    override fun getTabCompletions(server: MinecraftServer, sender: ICommandSender, args: Array<String>, targetPos: BlockPos?): List<String> {
        return if (args.isNotEmpty()) CommandBase.getListOfStringsMatchingLastWord(args, *server.onlinePlayerNames) else emptyList<String>()
    }
}

package info.modoff.modeoff.common.command

import info.modoff.modeoff.Modeoff
import info.modoff.modeoff.common.plot.Plot
import info.modoff.modeoff.common.plot.PlotManagerServer
import net.minecraft.command.CommandBase
import net.minecraft.command.CommandException
import net.minecraft.command.ICommandSender
import net.minecraft.command.WrongUsageException
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.server.MinecraftServer
import net.minecraft.util.math.BlockPos
import net.minecraft.util.text.TextComponentTranslation

/**
 * Created by LordSaad.
 */
class CommandAssign(val plotManager: PlotManagerServer) : CommandBase() {
    override fun getName() = "plot_assign"

    override fun getUsage(sender: ICommandSender) = "commands.modeoff.plot_assign.usage"

    @Throws(CommandException::class)
    override fun execute(server: MinecraftServer, sender: ICommandSender, args: Array<String>) {
        //if (sender instanceof EntityPlayer)
        //	if (!CommonProxy.teamMembers.contains(((EntityPlayer) sender).getUniqueID()) || !CommonProxy.contestants.contains(((EntityPlayer) sender).getUniqueID())) {
        //		sender.sendMessage(new TextComponentString(TextFormatting.RED + "You do not have permission to use this command."));
        //		return;
        //	}
        /* if (sender is EntityPlayer) {
            val player = when (args.size) {
                0 -> {
                    getCommandSenderAsPlayer(sender)
                }
                1 -> {
                    val (player) = args
                    getPlayer(server, sender, player)
                }
                else -> throw WrongUsageException(getUsage(sender))
            }

            if (plotManager.isUUIDRegistered(player.uniqueID)) {
                // TODO: Why does this modify the contestants set?
                Modeoff.proxy.contestants.add(player.uniqueID)
                sender.sendMessage(TextComponentTranslation("commands.modeoff.plot_assign.alreadyAssigned", player.name))
            } else {
                plotManager.saveUUIDToPlot(player.uniqueID, plotManager.nextAvailableID)
                val plot = plotManager.getPlotByAssignedUUID(player.uniqueID)!!
                sender.sendMessage(TextComponentTranslation("commands.modeoff.plot_assign.success", player.name, plot.plotID))

                plot.teleportPlayerToCenter(player)
                player.sendMessage(TextComponentTranslation("chat.type.modeoff.assignedPlot", plot.plotID))
            }
        } */
    }


    override fun getTabCompletions(server: MinecraftServer, sender: ICommandSender, args: Array<String>, targetPos: BlockPos?): List<String> {
        return if (args.size == 1) getListOfStringsMatchingLastWord(args, *server.onlinePlayerNames) else emptyList<String>()
    }
}

package me.lordsaad.modeoff.common.command

import me.lordsaad.modeoff.Modeoff
import me.lordsaad.modeoff.api.PlotAssigningManager
import me.lordsaad.modeoff.api.PlotManager
import net.minecraft.command.CommandBase
import net.minecraft.command.CommandException
import net.minecraft.command.ICommandSender
import net.minecraft.command.WrongUsageException
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.server.MinecraftServer
import net.minecraft.util.math.BlockPos
import net.minecraft.util.text.TextComponentString
import net.minecraft.util.text.TextFormatting

/**
 * Created by LordSaad.
 */
class CommandAssign : CommandBase() {
    override fun getName() = "plot_assign"

    override fun getUsage(sender: ICommandSender) = "/plot_assign [username]"

    @Throws(CommandException::class)
    override fun execute(server: MinecraftServer, sender: ICommandSender, args: Array<String>) {
        //if (sender instanceof EntityPlayer)
        //	if (!CommonProxy.teamMembers.contains(((EntityPlayer) sender).getUniqueID()) || !CommonProxy.contestants.contains(((EntityPlayer) sender).getUniqueID())) {
        //		sender.sendMessage(new TextComponentString(TextFormatting.RED + "You do not have permission to use this command."));
        //		return;
        //	}
        if (sender is EntityPlayer) {
            val player = if (args.size == 1) {
                val (player) = args
                if (Modeoff.proxy.teamMembers.contains(sender.uniqueID)) {
                    CommandBase.getPlayer(server, sender, player)
                } else {
                    throw WrongUsageException(getUsage(sender))
                }
            } else {
                CommandBase.getCommandSenderAsPlayer(sender)
            }

            val manager = PlotAssigningManager.INSTANCE
            if (manager.isUUIDRegistered(player.uniqueID)) {
                // TODO: Why does this modify the contestants set?
                Modeoff.proxy.contestants.add(player.uniqueID)
                sender.sendMessage(TextComponentString(TextFormatting.RED.toString() + "The plot for '" + TextFormatting.GOLD + player.name + TextFormatting.RED + "' has already been registered. Do /plot_tp to teleport to it."))
            } else {
                manager.saveUUIDToPlot(player.uniqueID, manager.nextAvailableID)
                val plotManager = PlotManager(player)
                plotManager.teleportToCenter()
                sender.sendMessage(TextComponentString(TextFormatting.GREEN.toString() + "A plot has been assigned for '" + TextFormatting.GOLD + player.name + TextFormatting.GREEN + "' successfully! Plot ID: " + manager.getPlotForUUID(player.uniqueID)))
            }
        }
    }


    override fun getTabCompletions(server: MinecraftServer, sender: ICommandSender, args: Array<String>, targetPos: BlockPos?): List<String> {
        return if (args.size == 1) CommandBase.getListOfStringsMatchingLastWord(args, *server.onlinePlayerNames) else emptyList<String>()
    }
}

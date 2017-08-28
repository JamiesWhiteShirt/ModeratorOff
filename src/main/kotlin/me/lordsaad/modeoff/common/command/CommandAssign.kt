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
import net.minecraft.util.text.TextComponentTranslation

/**
 * Created by LordSaad.
 */
class CommandAssign : CommandBase() {
    override fun getName() = "plot_assign"

    override fun getUsage(sender: ICommandSender) = "commands.modeoff.plot_assign.usage"

    @Throws(CommandException::class)
    override fun execute(server: MinecraftServer, sender: ICommandSender, args: Array<String>) {
        //if (sender instanceof EntityPlayer)
        //	if (!CommonProxy.teamMembers.contains(((EntityPlayer) sender).getUniqueID()) || !CommonProxy.contestants.contains(((EntityPlayer) sender).getUniqueID())) {
        //		sender.sendMessage(new TextComponentString(TextFormatting.RED + "You do not have permission to use this command."));
        //		return;
        //	}
        if (sender is EntityPlayer) {
            val player = when (args.size) {
                0 -> {
                    CommandBase.getCommandSenderAsPlayer(sender)
                }
                1 -> {
                    val (player) = args
                    CommandBase.getPlayer(server, sender, player)
                }
                else -> throw WrongUsageException(getUsage(sender))
            }

            if (PlotAssigningManager.isUUIDRegistered(player.uniqueID)) {
                // TODO: Why does this modify the contestants set?
                Modeoff.proxy.contestants.add(player.uniqueID)
                sender.sendMessage(TextComponentTranslation("commands.modeoff.plot_assign.alreadyAssigned", player.name))
            } else {
                PlotAssigningManager.saveUUIDToPlot(player.uniqueID, PlotAssigningManager.nextAvailableID)
                val plotManager = PlotManager(player)
                sender.sendMessage(TextComponentTranslation("commands.modeoff.plot_assign.success", player.name, plotManager.plotID))

                plotManager.teleportPlayerToCenter(player)
                player.sendMessage(TextComponentTranslation("chat.type.modeoff.assignedPlot", plotManager.plotID))
            }
        }
    }


    override fun getTabCompletions(server: MinecraftServer, sender: ICommandSender, args: Array<String>, targetPos: BlockPos?): List<String> {
        return if (args.size == 1) CommandBase.getListOfStringsMatchingLastWord(args, *server.onlinePlayerNames) else emptyList<String>()
    }
}

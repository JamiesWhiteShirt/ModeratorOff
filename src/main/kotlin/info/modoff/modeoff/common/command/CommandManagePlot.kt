package info.modoff.modeoff.common.command

import info.modoff.modeoff.Modeoff
import info.modoff.modeoff.common.gui.ContainerManagePlot
import net.minecraft.command.CommandBase
import net.minecraft.command.CommandException
import net.minecraft.command.ICommandSender
import net.minecraft.command.WrongUsageException
import net.minecraft.server.MinecraftServer

/**
 * Created by LordSaad.
 */
class CommandManagePlot : CommandBase() {
    override fun getName() = "plot_manager"

    override fun getUsage(sender: ICommandSender) = "commands.modeoff.plot_manager.usage"

    @Throws(CommandException::class)
    override fun execute(server: MinecraftServer, sender: ICommandSender, args: Array<String>) {
        //if (sender instanceof EntityPlayer)
        //	if (!CommonProxy.teamMembers.contains(((EntityPlayer) sender).getUniqueID()) || !CommonProxy.contestants.contains(((EntityPlayer) sender).getUniqueID())) {
        //		sender.sendMessage(new TextComponentString(TextFormatting.RED + "You do not have permission to use this command."));
        //		return;
        //	}
        if (args.isNotEmpty()) throw WrongUsageException(getUsage(sender))

        val player = getCommandSenderAsPlayer(sender)
        val pos = player.position
        player.openGui(Modeoff, ContainerManagePlot.ID, player.world, pos.x, pos.y, pos.z)
    }
}

package me.lordsaad.modeoff.common.command

import com.teamwizardry.librarianlib.features.network.PacketHandler
import me.lordsaad.modeoff.common.network.PacketManagerGui
import net.minecraft.command.CommandBase
import net.minecraft.command.CommandException
import net.minecraft.command.ICommandSender
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.server.MinecraftServer
import net.minecraft.util.math.BlockPos

/**
 * Created by LordSaad.
 */
class CommandManager : CommandBase() {
    override fun getName() = "plot_manager"

    override fun getUsage(sender: ICommandSender) = "/plot_manager [username]"

    @Throws(CommandException::class)
    override fun execute(server: MinecraftServer, sender: ICommandSender, args: Array<String>) {
        //if (sender instanceof EntityPlayer)
        //	if (!CommonProxy.teamMembers.contains(((EntityPlayer) sender).getUniqueID()) || !CommonProxy.contestants.contains(((EntityPlayer) sender).getUniqueID())) {
        //		sender.sendMessage(new TextComponentString(TextFormatting.RED + "You do not have permission to use this command."));
        //		return;
        //	}
        val player = CommandBase.getCommandSenderAsPlayer(sender)

        PacketHandler.NETWORK.sendTo(PacketManagerGui(), player as EntityPlayerMP)
    }


    override fun getTabCompletions(server: MinecraftServer, sender: ICommandSender, args: Array<String>, targetPos: BlockPos?): List<String> {
        return if (args.size == 1) CommandBase.getListOfStringsMatchingLastWord(args, *server.onlinePlayerNames) else emptyList<String>()
    }
}

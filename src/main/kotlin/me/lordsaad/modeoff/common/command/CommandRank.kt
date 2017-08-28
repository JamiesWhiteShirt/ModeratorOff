package me.lordsaad.modeoff.common.command

import me.lordsaad.modeoff.api.RankManager
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
class CommandRank : CommandBase() {
    override fun getName() = "rank"

    override fun getUsage(sender: ICommandSender) = "/rank <set/remove> <username> <rank>"

    @Throws(CommandException::class)
    override fun execute(server: MinecraftServer, sender: ICommandSender, args: Array<String>) {
        if (sender !is EntityPlayer || RankManager.getPlayerRank(sender.name)?.editOthers ?: false) {
            if (args.size < 2) throw WrongUsageException(getUsage(sender))

            val (operation, player) = args

            when (operation) {
                "set" -> {
                    if (args.size != 3) throw WrongUsageException(getUsage(sender))
                    val (_, _, rank) = args
                    RankManager.setPlayerRank(player, rank)
                    sender.sendMessage(TextComponentString(TextFormatting.GREEN.toString() + "Rank of player '" + TextFormatting.GOLD + player + TextFormatting.GREEN + "' was set to '" + TextFormatting.GOLD + rank + TextFormatting.GREEN + "' successfully!"))
                }
                "remove" -> {
                    if (args.size != 2) throw WrongUsageException(getUsage(sender))
                    RankManager.removePlayerRank(player)
                    sender.sendMessage(TextComponentString(TextFormatting.GREEN.toString() + "Rank of player '" + TextFormatting.GOLD + args[1] + TextFormatting.GREEN + "' was removed successfully!"))
                }
                else -> throw WrongUsageException(getUsage(sender))
            }
        } else {
            sender.sendMessage(TextComponentString(TextFormatting.RED.toString() + "You do not have permission to use this command."))
        }
    }


    override fun getTabCompletions(server: MinecraftServer, sender: ICommandSender, args: Array<String>, targetPos: BlockPos?): List<String> {
        return if (args.size >= 2) CommandBase.getListOfStringsMatchingLastWord(args, *server.onlinePlayerNames) else emptyList<String>()
    }
}

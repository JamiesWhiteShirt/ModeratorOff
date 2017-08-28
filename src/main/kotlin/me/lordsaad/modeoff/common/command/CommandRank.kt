package me.lordsaad.modeoff.common.command

import me.lordsaad.modeoff.api.RankManager
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
class CommandRank : CommandBase() {
    override fun getName() = "rank"

    override fun getUsage(sender: ICommandSender) = "commands.modeoff.rank.usage"

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
                    sender.sendMessage(TextComponentTranslation("commands.modeoff.rank.set.success", player, rank))
                }
                "remove" -> {
                    if (args.size != 2) throw WrongUsageException(getUsage(sender))
                    RankManager.removePlayerRank(player)
                    sender.sendMessage(TextComponentTranslation("commands.modeoff.rank.remove.success", player))
                }
                else -> throw WrongUsageException(getUsage(sender))
            }
        } else {
            throw CommandException("commands.generic.permission")
        }
    }


    override fun getTabCompletions(server: MinecraftServer, sender: ICommandSender, args: Array<String>, targetPos: BlockPos?): List<String> {
        return if (args.size >= 2) CommandBase.getListOfStringsMatchingLastWord(args, *server.onlinePlayerNames) else emptyList<String>()
    }
}

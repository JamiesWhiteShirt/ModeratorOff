package me.lordsaad.modeoff.common.command;

import me.lordsaad.modeoff.api.Rank;
import me.lordsaad.modeoff.api.RankManager;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

/**
 * Created by LordSaad.
 */
public class CommandRank extends CommandBase {

	@NotNull
	@Override
	public String getName() {
		return "rank";
	}

	@NotNull
	@Override
	public String getUsage(@NotNull ICommandSender sender) {
		return "/rank <set/remove> <username> <rank>";
	}

	@Override
	public void execute(@NotNull MinecraftServer server, @NotNull ICommandSender sender, @NotNull String[] args) throws CommandException {
		boolean perm = false;
		if (sender instanceof EntityPlayer) {
			Rank rank = RankManager.INSTANCE.getPlayerRank(getCommandSenderAsPlayer(sender).getName());
			if (rank != null && rank.editOthers) perm = true;
		} else perm = true;

		if (!perm) {
			sender.sendMessage(new TextComponentString(TextFormatting.RED + "You do not have permission to use this command."));
			return;
		}

		if (args.length < 2) throw new WrongUsageException(getUsage(sender));

		if (args[0].equals("set")) {
			RankManager.INSTANCE.setPlayerRank(args[1], args[0]);
			EntityPlayer player = getPlayer(server, sender, args[1]);
			if (!player.getName().equals(sender.getName()))
				sender.sendMessage(new TextComponentString(TextFormatting.GREEN + "Rank of player '" + TextFormatting.GOLD + args[1] + TextFormatting.GREEN + "' was set to '" + TextFormatting.GOLD + args[0] + TextFormatting.GREEN + "' successfully!"));
			player.sendMessage(new TextComponentString(TextFormatting.GREEN + "Your rank was set to '" + TextFormatting.GOLD + args[0] + TextFormatting.GREEN + "' successfully!"));
		} else if (args[0].equals("remove")) {
			RankManager.INSTANCE.removePlayerRank(args[1]);
			EntityPlayer player = getPlayer(server, sender, args[1]);
			if (!player.getName().equals(sender.getName()))
				sender.sendMessage(new TextComponentString(TextFormatting.GREEN + "Rank of player '" + TextFormatting.GOLD + args[1] + TextFormatting.GREEN + "' was removed successfully!"));
			player.sendMessage(new TextComponentString(TextFormatting.GREEN + "Your rank was removed successfully!"));
		}
	}


	@NotNull
	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
		return args.length >= 2 ? getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames()) : Collections.emptyList();
	}
}

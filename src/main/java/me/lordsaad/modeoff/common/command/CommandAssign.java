package me.lordsaad.modeoff.common.command;

import me.lordsaad.modeoff.api.PlotAssigningManager;
import me.lordsaad.modeoff.api.PlotManager;
import me.lordsaad.modeoff.common.CommonProxy;
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
public class CommandAssign extends CommandBase {

	@NotNull
	@Override
	public String getName() {
		return "plot_assign";
	}

	@NotNull
	@Override
	public String getUsage(@NotNull ICommandSender sender) {
		return "/plot_assign [username]";
	}

	@Override
	public void execute(@NotNull MinecraftServer server, @NotNull ICommandSender sender, @NotNull String[] args) throws CommandException {
		//if (sender instanceof EntityPlayer)
		//	if (!CommonProxy.teamMembers.contains(((EntityPlayer) sender).getUniqueID()) || !CommonProxy.contestants.contains(((EntityPlayer) sender).getUniqueID())) {
		//		sender.sendMessage(new TextComponentString(TextFormatting.RED + "You do not have permission to use this command."));
		//		return;
		//	}
		EntityPlayer player = null;
		if (args.length >= 1) {
			if ((sender instanceof EntityPlayer) && CommonProxy.teamMembers.contains(((EntityPlayer) sender).getUniqueID()))
				player = getPlayer(server, sender, args[0]);
		} else if (sender instanceof EntityPlayer) player = getCommandSenderAsPlayer(sender);
		else throw new WrongUsageException(getUsage(sender));

		if (player == null) throw new WrongUsageException(getUsage(sender));

		PlotAssigningManager manager = PlotAssigningManager.INSTANCE;
		if (manager.isUUIDRegistered(player.getUniqueID())) {
			CommonProxy.contestants.add(player.getUniqueID());
			sender.sendMessage(new TextComponentString(TextFormatting.RED + "The plot for '" + TextFormatting.GOLD + player.getName() + TextFormatting.RED + "' has already been registered. Do /plot_tp to teleport to it."));
			return;
		}

		manager.saveUUIDToPlot(player.getUniqueID(), manager.getNextAvailableID());
		PlotManager plotManager = new PlotManager(player);
		plotManager.teleportToCenter();
		sender.sendMessage(new TextComponentString(TextFormatting.GREEN + "A plot has been assigned for '" + TextFormatting.GOLD + player.getName() + TextFormatting.GREEN + "' successfully! Plot ID: " + manager.getPlotForUUID(player.getUniqueID())));
		if (!sender.getName().equals(player.getName()))
			player.sendMessage(new TextComponentString(TextFormatting.GREEN + "A plot has been assigned for '" + TextFormatting.GOLD + player.getName() + TextFormatting.GREEN + "' successfully! Plot ID: " + manager.getPlotForUUID(player.getUniqueID())));
	}


	@NotNull
	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
		return args.length == 1 ? getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames()) : Collections.emptyList();
	}
}

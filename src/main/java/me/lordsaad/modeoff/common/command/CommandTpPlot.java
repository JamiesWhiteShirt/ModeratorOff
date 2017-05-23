package me.lordsaad.modeoff.common.command;

import me.lordsaad.modeoff.api.PlotManager;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

/**
 * Created by LordSaad.
 */
public class CommandTpPlot extends CommandBase {

	@NotNull
	@Override
	public String getName() {
		return "plot_tp";
	}

	@NotNull
	@Override
	public String getUsage(@NotNull ICommandSender sender) {
		return "/plot_tp [username/plotID] [username/plotID]";
	}

	@Override
	public void execute(@NotNull MinecraftServer server, @NotNull ICommandSender sender, @NotNull String[] args) throws CommandException {
		//if (!(sender instanceof EntityPlayer)) {
		//	sender.sendMessage(new TextComponentString(TextFormatting.RED + "You do not have permission to use this command."));
		//	return;
		//}
//
		//if (!CommonProxy.teamMembers.contains(((EntityPlayer) sender).getUniqueID()) || !CommonProxy.contestants.contains(((EntityPlayer) sender).getUniqueID())) {
		//	sender.sendMessage(new TextComponentString(TextFormatting.RED + "You do not have permission to use this command."));
		//	return;
		//}

		if (args.length == 0) {
			new PlotManager(getCommandSenderAsPlayer(sender)).teleportToCenter();

		} else if (args.length == 1) {
			try {
				int plotID = Integer.parseInt(args[0]);
				PlotManager.teleportToPlot(getCommandSenderAsPlayer(sender), new PlotManager(sender.getEntityWorld(), plotID).plotID);
			} catch (NumberFormatException e) {
				PlotManager.teleportToPlot(getCommandSenderAsPlayer(sender), new PlotManager(getPlayer(server, sender, args[0])).plotID);
			}
		} else {
			EntityPlayer player1;
			int id;
			try {
				int plotID = Integer.parseInt(args[0]);
				PlotManager manager = new PlotManager(sender.getEntityWorld(), plotID);
				if (manager.player == null)
					throw new PlayerNotFoundException("Could not find player of plot id " + plotID, sender);
				player1 = manager.player;
			} catch (NumberFormatException e) {
				player1 = getPlayer(server, sender, args[0]);
			}

			PlotManager manager = new PlotManager(getPlayer(server, sender, args[1]));
			if (manager.plotID < 0)
				throw new PlayerNotFoundException("Could not find plot id for player" + args[1], sender);
			id = manager.plotID;

			PlotManager.teleportToPlot(player1, id);
		}
	}

	@NotNull
	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
		return args.length >= 1 ? getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames()) : Collections.emptyList();
	}
}

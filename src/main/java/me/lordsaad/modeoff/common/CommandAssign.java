package me.lordsaad.modeoff.common;

import me.lordsaad.modeoff.api.PlotAssigningManager;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.FMLCommonHandler;
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
		return "assign_plot";
	}

	@NotNull
	@Override
	public String getUsage(@NotNull ICommandSender sender) {
		return "/assign_plot [username]";
	}

	@Override
	public void execute(@NotNull MinecraftServer server, @NotNull ICommandSender sender, @NotNull String[] args) throws CommandException {
		EntityPlayer player;
		if (!(sender instanceof EntityPlayer)) {
			if (args.length < 1) throw new WrongUsageException(getUsage(sender));
			else player = getCommandSenderAsPlayer(sender);
		} else {
			if (args.length >= 1) player = getPlayer(server, sender, args[0]);
			else player = (EntityPlayer) sender;
		}

			PlotAssigningManager manager = PlotAssigningManager.INSTANCE;
			if (manager.isUUIDRegistered(player.getUniqueID())) {
				sender.sendMessage(new TextComponentString(TextFormatting.RED + "You're plot has already been registered. Do /plot_tp to teleport to it."));
				return;
			}
			manager.saveUUIDToPlot(player.getUniqueID(), manager.getNextAvailableID());
			sender.sendMessage(new TextComponentString(TextFormatting.GREEN + "A plot has been assigned to you successfully! Plot ID: " + manager.getPlotForUUID(player.getUniqueID())));
	}


	@NotNull
	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
		return args.length == 1 ? getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames()) : Collections.<String>emptyList();
	}
}

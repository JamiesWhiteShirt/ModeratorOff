package me.lordsaad.modeoff;

import me.lordsaad.modeoff.common.CommandAssign;
import me.lordsaad.modeoff.common.CommandManager;
import me.lordsaad.modeoff.common.CommandTpPlot;
import me.lordsaad.modeoff.common.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import org.apache.logging.log4j.Logger;

@Mod(
		modid = Modeoff.MOD_ID,
		name = Modeoff.MOD_NAME,
		version = Modeoff.VERSION,
		dependencies = "required-after:librarianlib"
)
public class Modeoff {

	public static final String MOD_ID = "modeoff";
	public static final String MOD_NAME = "Modeoff";
	public static final String VERSION = "1.0";

	public static final String CLIENT = "me.lordsaad.modeoff.client.ClientProxy";
	public static final String SERVER = "me.lordsaad.modeoff.server.ServerProxy";

	public static Logger logger;

	@SidedProxy(clientSide = CLIENT, serverSide = SERVER)
	public static CommonProxy proxy;

	@Mod.Instance
	public static Modeoff instance;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		proxy.preInit(event);
		logger = event.getModLog();
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent e) {
		proxy.init(e);
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent e) {
		proxy.postInit(e);
	}

	@Mod.EventHandler
	public void serverLoad(FMLServerStartingEvent event) {
		event.registerServerCommand(new CommandAssign());
		event.registerServerCommand(new CommandTpPlot());
		event.registerServerCommand(new CommandManager());
	}
}

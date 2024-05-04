package mdk.mop;

import mdk.comands.SkinCommand;
import mdk.config.ModConfigs;
import mdk.debug.GUI;
import mdk.mop.network.ModNetworkHandler;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.common.MinecraftForge;

import javax.swing.*;
import java.io.File;

@Mod(modid = Mop.MODID, version = Mop.VERSION)
public class Mop {
	public Configuration cfg;
	public File file;
	public static final String MODID = "armskinloader";
	public static final String VERSION = "1.0.0";
	@Mod.Instance(MODID)
	public static Mop instance;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event){
		cfg = new Configuration(event.getSuggestedConfigurationFile());
		cfg.load();


		cfg.save();
		MinecraftForge.EVENT_BUS.register(this);

		SwingUtilities.invokeLater(() -> {
			GUI window = new GUI();
			window.setVisible(true);

			GUI.out.println("Debuging: "+ ModConfigs.debug);
		});
		ModNetworkHandler.init();
		if (FMLCommonHandler.instance().getSide().isServer()) {
			file = new File("skinker");
			file.mkdir();
			File file2 = new File(file, "skins");
			file2.mkdir();
		}
	}


	@Mod.EventHandler
	public void init(FMLServerStartingEvent event)
	{
		event.registerServerCommand(new SkinCommand());
	}
}

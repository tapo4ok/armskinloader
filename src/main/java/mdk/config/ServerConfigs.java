package mdk.config;

import mdk.mop.Mop;
import net.minecraftforge.common.config.Config;

@Config(modid = Mop.MODID, name = "skin_server")
public class ServerConfigs {
    @Config.Name("file_data_format")
    @Config.RangeInt(min = 0, max = 1)
    @Config.Comment({
            "0. .txt config file and .png skin",
            "1. .bin data file (skin and config)"
    })
    public static int file_format = 0;

    @Config.Name("eneble_file_server")
    @Config.Comment({
            "in development",
            "Enable installation of skin data on the server"
    })
    public static boolean file_isserver = false;

    @Config.Name("file_server_url")
    @Config.Comment({
            "in development",
            "installation server"
    })
    public static String url = "null";
}

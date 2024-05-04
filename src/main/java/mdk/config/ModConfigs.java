package mdk.config;

import mdk.mop.Mop;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Name;
import net.minecraftforge.common.config.Config.Comment;


@Config(modid = Mop.MODID, name = "skin_maneger")
public class ModConfigs {
    @Name("debug")
    @Comment("Eneble debug mode")
    public static boolean debug = false;

    @Name("systems")
    @Comment("Eneble Skin Systems")
    public static boolean skin_systems = true;

    @Name("elyby")
    @Comment("Elyby api")
    public static boolean elyby = true;
    @Name("elyby_url")
    public static String elyby_url = "http://skinsystem.ely.by/textures/";
    @Name("tlauncher")
    @Comment("Tlauncher api")
    public static boolean tlauncher = true;
    @Name("tlauncher_url")
    public static String tlauncher_url = "http://auth.tlauncher.org/skin/profile/texture/login/";
}
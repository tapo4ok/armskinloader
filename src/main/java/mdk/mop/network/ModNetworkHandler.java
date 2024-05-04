package mdk.mop.network;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

public class ModNetworkHandler {
    public static final SimpleNetworkWrapper NETWORK = new SimpleNetworkWrapper("skin_system");
    public static final SimpleNetworkWrapper NETWORK_DEBUG = new SimpleNetworkWrapper("debug_228");

    public static void init() {
        NETWORK.registerMessage(new Skin.ClientHandler(), Skin.class, 0, Side.CLIENT);
        NETWORK.registerMessage(new Skin.ServerHandler(), Skin.class, 1, Side.SERVER);

        NETWORK_DEBUG.registerMessage(new DebugPkg.ServerHandler(), DebugPkg.class, 0, Side.SERVER);
    }
}

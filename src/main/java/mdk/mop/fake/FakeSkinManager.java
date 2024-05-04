package mdk.mop.fake;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import mdk.config.ModConfigs;
import mdk.debug.GUI;
import mdk.mop.api.ElyByApi;
import mdk.mop.api.ISkinApi;
import mdk.mop.api.Tlauncher;
import mdk.mop.network.ModNetworkHandler;
import mdk.mop.network.Skin;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.ImageBufferDownload;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.SkinManager;
import net.minecraft.client.resources.SkinManager.SkinAvailableCallback;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


@SideOnly(Side.CLIENT)
public class FakeSkinManager {
    private static final Logger LOGGER = LogManager.getLogger();
    public List<ISkinApi> iSkinApis;
    private static final ExecutorService THREAD_POOL = new ThreadPoolExecutor(0, 2, 1L, TimeUnit.MINUTES, new LinkedBlockingQueue());
    public final TextureManager textureManager;
    public final MinecraftSessionService sessionService;
    public final File c;
    public static Map<String, SkinManager.SkinAvailableCallback> SACMap;
    public static FakeSkinManager inst;

    public FakeSkinManager(TextureManager textureManagerInstance, File skinCacheDirectory, MinecraftSessionService sessionService)
    {
        this.textureManager = textureManagerInstance;
        this.sessionService = sessionService;
        c = skinCacheDirectory;
        iSkinApis = new ArrayList<>();
        if (ModConfigs.skin_systems) {
            if (ModConfigs.elyby) {
                iSkinApis.add(new ElyByApi());
            }

            if (ModConfigs.tlauncher) {
                iSkinApis.add(new Tlauncher());
            }
        }
        SACMap = new HashMap<>();
        inst = this;
    }

    public ResourceLocation loadSkin(final MinecraftProfileTexture profileTexture, final MinecraftProfileTexture.Type textureType, @Nullable final SkinAvailableCallback skinAvailableCallback)
    {
        ResourceLocation resourcelocation = new ResourceLocation("skins/" + profileTexture.getHash());
        File file1 = new File(this.c, profileTexture.getHash());
        File file2 = new File(file1, profileTexture.getHash());
        IImageBuffer iimagebuffer = textureType == MinecraftProfileTexture.Type.SKIN ? new ImageBufferDownload() : null;

        ThreadDownloadImageData threaddownloadimagedata = new ThreadDownloadImageData(file2, profileTexture.getUrl(), DefaultPlayerSkin.getDefaultSkinLegacy(), new IImageBuffer()
        {
            public BufferedImage parseUserSkin(BufferedImage image)
            {
                return iimagebuffer.parseUserSkin(image);
            }
            public void skinAvailable()
            {
                iimagebuffer.skinAvailable();

                if (skinAvailableCallback != null)
                {
                    skinAvailableCallback.skinAvailable(MinecraftProfileTexture.Type.SKIN, resourcelocation, profileTexture);
                }
            }
        });

        this.textureManager.loadTexture(resourcelocation, threaddownloadimagedata);
        return resourcelocation;
    }

    public void loadProfileTextures(final GameProfile profile, final SkinManager.SkinAvailableCallback skinAvailableCallback, final boolean requireSecure)
    {
        THREAD_POOL.submit(new Runnable()
        {
            public void run()
            {
                Minecraft.getMinecraft().addScheduledTask(new Runnable()
                {
                    public void run()
                    {
                        Minecraft minecraft = Minecraft.getMinecraft();

                        if (!minecraft.world.isRemote) {
                            Skin skin = new Skin();
                            skin.username=profile.getName();
                            SACMap.put(profile.getName(), skinAvailableCallback);
                            ModNetworkHandler.NETWORK.sendToServer(skin);
                        }

                        MinecraftProfileTexture t = null;
                        for (ISkinApi h: iSkinApis) {
                            MinecraftProfileTexture g = h.getUserData(profile);
                            if (g != null) {
                                t = g;
                                break;
                            }
                        }
                        if (t != null) {
                            loadSkin(t, MinecraftProfileTexture.Type.SKIN, skinAvailableCallback);
                        }
                    }
                });
            }
        });
    }

    public void load(Skin message, MessageContext ctx) {
        Minecraft.getMinecraft().addScheduledTask(() -> {
            GUI.out.println("Get Skin for:"+message.username);
            Map<String, SkinManager.SkinAvailableCallback> map = FakeSkinManager.SACMap;
            if (map.containsKey(message.username)) {
                Map<String, String> str = new HashMap<>();
                str.put("model", message.model);
                GUI.out.println("model: " + message.model);
                MinecraftProfileTexture txt = new MinecraftProfileTexture("https://s.namemc.com/i/"+message.username, str);

                final ResourceLocation resourcelocation = new ResourceLocation("skins/" + txt.getHash());
                SkinManager.SkinAvailableCallback skinAvailableCallback = map.get(message.username);

                this.textureManager.loadTexture(resourcelocation, new AbstractTexture() {
                    @Override
                    public void loadTexture(IResourceManager resourceManager) throws IOException {
                        this.deleteGlTexture();

                        TextureUtil.uploadTextureImage(this.getGlTextureId(), message.skin);
                    }
                });

                skinAvailableCallback.skinAvailable(MinecraftProfileTexture.Type.SKIN, resourcelocation, txt);
            }
        });

    }

    public Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> loadSkinFromCache(GameProfile profile)
    {
        return new HashMap<>();
    }
}

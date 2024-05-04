package mdk.mop.api;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;

public interface ISkinApi {
    MinecraftProfileTexture getUserData(GameProfile gameProfile);
    MinecraftProfileTexture getUserData(String username);

}

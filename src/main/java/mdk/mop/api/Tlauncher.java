package mdk.mop.api;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import mdk.config.ModConfigs;
import mdk.mop.Init.hewfqwr;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Tlauncher implements ISkinApi {
    @Override
    public MinecraftProfileTexture getUserData(GameProfile gameProfile) {
        return getUserData(gameProfile.getName());
    }

    @Override
    public MinecraftProfileTexture getUserData(String username) {
        try {
            URL url = new URL(ModConfigs.tlauncher_url+username);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                JsonElement userElement = hewfqwr.GSON.fromJson(response.toString(), JsonElement.class);
                if (userElement != null) {
                    JsonObject obj = userElement.getAsJsonObject().getAsJsonObject("SKIN");
                    if (obj != null) {
                        Map<String, String> i = new HashMap<>();
                        try {
                            i.put("model", obj.getAsJsonObject("metadata").get("model").getAsString());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return new MinecraftProfileTexture(obj.get("url").getAsString(), i);
                    }
                }
            }
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

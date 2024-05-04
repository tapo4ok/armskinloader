package mdk.mop.network;

import mdk.debug.GUI;
import mdk.mop.fake.FakeSkinManager;
import mdk.skind.ImageFac;
import mdk.skind.IskinObject;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.relauncher.Side;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Properties;

public class Skin implements IMessage {

    public boolean eneble;
    public String model;
    public String username;
    public BufferedImage skin;

    @Override
    public void toBytes(ByteBuf buf) {
        PacketBuffer buffer = new PacketBuffer(buf);
        buffer.writeString(username);

        if (FMLCommonHandler.instance().getSide().isServer())  {
            buffer.writeBoolean(eneble);
            if (eneble) {
                buffer.writeString(model);
                try {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    ImageIO.write(skin, "png", baos);
                    baos.flush();
                    buf.writeBytes(baos.toByteArray());
                    baos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        PacketBuffer buffer = new PacketBuffer(buf);
        username = buffer.readString(256);

        if (FMLCommonHandler.instance().getSide().isClient()) {
            eneble = buffer.readBoolean();
            if (eneble) {
                model = buffer.readString(256);
                try {
                    byte[] byteArray = new byte[buf.readableBytes()];
                    buf.readBytes(byteArray);
                    ByteArrayInputStream bais = new ByteArrayInputStream(byteArray);
                    skin = ImageIO.read(bais);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static class ClientHandler implements IMessageHandler<Skin, IMessage> {

        @Override
        public IMessage onMessage(Skin message, MessageContext ctx) {
            FakeSkinManager.inst.load(message, ctx);
            return null;
        }
    }


    public static class ServerHandler implements IMessageHandler<Skin, IMessage> {

        @Override
        public IMessage onMessage(Skin message, MessageContext ctx) {
            GUI.out.println("Get Skin for:"+message.username);
            File file = new File("skinker");
            if (file.exists()) {
                {
                    try {
                        IskinObject so = ImageFac.get(message.username, "null");
                        message.model=so.getModel();
                        message.eneble=true;
                        message.skin=so.getSkin();
                        if (so.loadet()) {
                            GUI.out.println("Send Skin for:"+message.username);
                            ModNetworkHandler.NETWORK.sendTo(message, ctx.getServerHandler().player);
                        }

                    } catch (Exception e) {
                        return null;
                    }
                }
            }
            return null;
        }
    }
}

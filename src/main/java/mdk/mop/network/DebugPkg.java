package mdk.mop.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;


public class DebugPkg implements IMessage {
    public String action;
    @Override
    public void toBytes(ByteBuf buf) {
        PacketBuffer buffer = new PacketBuffer(buf);
        buffer.writeString(action);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        PacketBuffer buffer = new PacketBuffer(buf);
        action = buffer.readString(256*2);
    }

    public static class ServerHandler implements IMessageHandler<DebugPkg, IMessage> {
        @Override
        public IMessage onMessage(DebugPkg message, MessageContext ctx) {
            return null;
        }
    }
}

package mdk.comands;

import mdk.mop.Mop;
import mdk.skind.ImageFac;
import mdk.skind.IskinObject;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

import java.io.*;
import java.net.URL;
import java.util.Properties;

public class SkinCommand extends CommandBase {
    public SkinCommand() {

    }

    @Override
    public String getName() {
        return "skin";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "skin.usage.comamnd";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        IskinObject obj = ImageFac.get(args[0], args[1]);
        obj.setModel(args[2]);
        obj.save();
    }
}

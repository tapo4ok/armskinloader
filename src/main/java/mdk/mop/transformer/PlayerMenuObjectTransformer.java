package mdk.mop.transformer;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.ListIterator;
import mdk.mop.TransformerManager;

@TransformerManager.TransformTarget(className="net.minecraft.client.gui.spectator.PlayerMenuObject",
        methodNames={"<init>"},
        desc="(Lcom/mojang/authlib/GameProfile;)V")
public class PlayerMenuObjectTransformer implements TransformerManager.IMethodTransformer{

    @Override
    public void transform(ClassNode cn, MethodNode mn) {
        InsnList il=mn.instructions;
        ListIterator<AbstractInsnNode> li=il.iterator();

        boolean flag=false;
        while(li.hasNext()) {
            AbstractInsnNode ain=li.next();
            if(ain.getOpcode()!= Opcodes.INVOKESTATIC)
                continue;

            MethodInsnNode min = (MethodInsnNode)ain;
            if(min.owner.equals("net/minecraft/client/entity/AbstractClientPlayer"))
                continue;

            if(!flag) {
                il.set(ain, new MethodInsnNode(Opcodes.INVOKESTATIC,"mdk/mop/fake/FakeClientPlayer","getLocationSkin",
                        "(Ljava/lang/String;)Lnet/minecraft/util/ResourceLocation;",false));
                flag=true;
            }else {
                il.set(ain, new MethodInsnNode(Opcodes.INVOKESTATIC,"mdk/mop/fake/FakeClientPlayer","getDownloadImageSkin",
                        "(Lnet/minecraft/util/ResourceLocation;Ljava/lang/String;)Lnet/minecraft/client/renderer/ThreadDownloadImageData;",false));
                break;
            }
        }
    }

}
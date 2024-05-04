package mdk.mop.transformer;

import mdk.mop.TransformerManager;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

public class SkinManagerTransformer {
    @TransformerManager.TransformTarget(className="net.minecraft.client.resources.SkinManager",
            methodNames={"<init>"},
            desc="(Lnet/minecraft/client/renderer/texture/TextureManager;Ljava/io/File;Lcom/mojang/authlib/minecraft/MinecraftSessionService;)V")
    public static class InitTransformer implements TransformerManager.IMethodTransformer{
        @Override
        public void transform(ClassNode cn,MethodNode mn) {
            cn.fields.add(new FieldNode(Opcodes.ACC_PRIVATE, "fakeManager", "Lmdk/mop/fake/FakeSkinManager;", null, null));
            mn.instructions.clear();
            mn.instructions.add(new VarInsnNode(Opcodes.ALOAD,0));
            mn.instructions.add(new MethodInsnNode(Opcodes.INVOKESPECIAL,"java/lang/Object","<init>","()V", false));
            mn.instructions.add(new VarInsnNode(Opcodes.ALOAD,0));
            mn.instructions.add(new TypeInsnNode(Opcodes.NEW, "mdk/mop/fake/FakeSkinManager"));
            mn.instructions.add(new InsnNode(Opcodes.DUP));
            mn.instructions.add(new VarInsnNode(Opcodes.ALOAD,1));
            mn.instructions.add(new VarInsnNode(Opcodes.ALOAD,2));
            mn.instructions.add(new VarInsnNode(Opcodes.ALOAD,3));
            mn.instructions.add(new MethodInsnNode(Opcodes.INVOKESPECIAL,"mdk/mop/fake/FakeSkinManager","<init>",
                    "(Lnet/minecraft/client/renderer/texture/TextureManager;Ljava/io/File;Lcom/mojang/authlib/minecraft/MinecraftSessionService;)V", false));
            mn.instructions.add(new FieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/client/resources/SkinManager", "fakeManager", "Lmdk/mop/fake/FakeSkinManager;"));
            mn.instructions.add(new InsnNode(Opcodes.RETURN));
        }
    }
    @TransformerManager.TransformTarget(className="net.minecraft.client.resources.SkinManager",
            methodNames={"func_152789_a","loadSkin"},
            desc="(Lcom/mojang/authlib/minecraft/MinecraftProfileTexture;Lcom/mojang/authlib/minecraft/MinecraftProfileTexture$Type;Lnet/minecraft/client/resources/SkinManager$SkinAvailableCallback;)Lnet/minecraft/util/ResourceLocation;")
    public static class LoadSkinTransformer implements TransformerManager.IMethodTransformer {
        @Override
        public void transform(ClassNode cn, MethodNode mn) {
            mn.instructions.clear();
            mn.instructions.add(new VarInsnNode(Opcodes.ALOAD,0));
            mn.instructions.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/client/resources/SkinManager", "fakeManager", "Lmdk/mop/fake/FakeSkinManager;"));
            mn.instructions.add(new VarInsnNode(Opcodes.ALOAD,1));
            mn.instructions.add(new VarInsnNode(Opcodes.ALOAD,2));
            mn.instructions.add(new VarInsnNode(Opcodes.ALOAD,3));
            mn.instructions.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL,"mdk/mop/fake/FakeSkinManager","loadSkin",
                    "(Lcom/mojang/authlib/minecraft/MinecraftProfileTexture;Lcom/mojang/authlib/minecraft/MinecraftProfileTexture$Type;Lnet/minecraft/client/resources/SkinManager$SkinAvailableCallback;)Lnet/minecraft/util/ResourceLocation;", false));
            mn.instructions.add(new InsnNode(Opcodes.ARETURN));
        }
    }

    @TransformerManager.TransformTarget(className="net.minecraft.client.resources.SkinManager",
            methodNames={"func_152790_a","loadProfileTextures"},
            desc="(Lcom/mojang/authlib/GameProfile;Lnet/minecraft/client/resources/SkinManager$SkinAvailableCallback;Z)V")
    public static class LoadProfileTexturesTransformer implements TransformerManager.IMethodTransformer{
        @Override
        public void transform(ClassNode cn,MethodNode mn) {
            mn.instructions.clear();
            mn.instructions.add(new VarInsnNode(Opcodes.ALOAD,0));
            mn.instructions.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/client/resources/SkinManager", "fakeManager", "Lmdk/mop/fake/FakeSkinManager;"));
            mn.instructions.add(new VarInsnNode(Opcodes.ALOAD,1));
            mn.instructions.add(new VarInsnNode(Opcodes.ALOAD,2));
            mn.instructions.add(new VarInsnNode(Opcodes.ILOAD,3));
            mn.instructions.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL,"mdk/mop/fake/FakeSkinManager","loadProfileTextures",
                    "(Lcom/mojang/authlib/GameProfile;Lnet/minecraft/client/resources/SkinManager$SkinAvailableCallback;Z)V", false));
            mn.instructions.add(new InsnNode(Opcodes.RETURN));
        }
    }
    @TransformerManager.TransformTarget(className="net.minecraft.client.resources.SkinManager",
            methodNames={"func_152788_a","loadSkinFromCache"},
            desc="(Lcom/mojang/authlib/GameProfile;)Ljava/util/Map;")
    public static class LoadSkinFromCacheTransformer implements TransformerManager.IMethodTransformer{
        @Override
        public void transform(ClassNode cn,MethodNode mn) {
            mn.instructions.clear();
            mn.instructions.add(new VarInsnNode(Opcodes.ALOAD,0));
            mn.instructions.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/client/resources/SkinManager", "fakeManager", "Lmdk/mop/fake/FakeSkinManager;"));
            mn.instructions.add(new VarInsnNode(Opcodes.ALOAD,1));
            mn.instructions.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL,"mdk/mop/fake/FakeSkinManager","loadSkinFromCache",
                    "(Lcom/mojang/authlib/GameProfile;)Ljava/util/Map;", false));
            mn.instructions.add(new InsnNode(Opcodes.ARETURN));
        }
    }
}

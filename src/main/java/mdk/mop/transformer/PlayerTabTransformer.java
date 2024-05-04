package mdk.mop.transformer;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import mdk.mop.TransformerManager;

import java.util.ListIterator;

public class PlayerTabTransformer {
    @TransformerManager.TransformTarget(className="net.minecraft.client.gui.GuiPlayerTabOverlay",
            methodNames={"func_175249_a","renderPlayerlist"},
            desc="(ILnet/minecraft/scoreboard/Scoreboard;Lnet/minecraft/scoreboard/ScoreObjective;)V")
    public static class ScoreObjectiveTransformer implements TransformerManager.IMethodTransformer{
        //From: http://git.oschina.net/AsteriskTeam/TabIconHackForge/blob/master/src/main/java/kengxxiao/tabiconhack/coremod/TabIconHackForgeClassTransformer.java#L30-L43
        @Override
        public void transform(ClassNode cn, MethodNode mn) {
            ListIterator<AbstractInsnNode> iterator = mn.instructions.iterator();
            while (iterator.hasNext())
            {
                AbstractInsnNode node = iterator.next();
                if (node instanceof VarInsnNode)
                {
                    VarInsnNode varNode = (VarInsnNode) node;
                    if (varNode.getOpcode() == Opcodes.ISTORE && varNode.var == 11)
                        mn.instructions.set(varNode.getPrevious(), new InsnNode(Opcodes.ICONST_1));
                }
            }
        }
    }
}

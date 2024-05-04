package mdk.mop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mdk.mop.transformer.*;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraftforge.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;

public class TransformerManager implements IClassTransformer {
    public static List<IMethodTransformer> TRANFORMERS;
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface TransformTarget{
        String className();
        String[] methodNames();
        String desc();
    }
    public interface IMethodTransformer {
        void transform(ClassNode cn,MethodNode mn);
    }
    private Map<String, Map<String, IMethodTransformer>> map;
    public TransformerManager(){
        TRANFORMERS = new ArrayList<>();
        TRANFORMERS.add(new SkinManagerTransformer.LoadSkinTransformer());
        TRANFORMERS.add(new SkinManagerTransformer.InitTransformer());
        TRANFORMERS.add(new SkinManagerTransformer.LoadProfileTexturesTransformer());
        TRANFORMERS.add(new SkinManagerTransformer.LoadSkinFromCacheTransformer());
        TRANFORMERS.add(new PlayerTabTransformer.ScoreObjectiveTransformer());
        TRANFORMERS.add(new PlayerMenuObjectTransformer());
        map = new HashMap<String, Map<String, IMethodTransformer>>();
        for(IMethodTransformer t:TRANFORMERS){
            if(!t.getClass().isAnnotationPresent(TransformTarget.class)){
                continue;
            }
            addMethodTransformer(t.getClass().getAnnotation(TransformTarget.class),t);
        }
    }
    private void addMethodTransformer(TransformTarget target, IMethodTransformer transformer) {
        if (!map.containsKey(target.className()))
            map.put(target.className(), new HashMap<String, IMethodTransformer>());
        for(String methodName:target.methodNames()){
            map.get(target.className()).put(methodName + target.desc(), transformer);
        }
    }

    @Override
    public byte[] transform(String obfClassName, String className, byte[] bytes) {
        if (!map.containsKey(className)) return bytes;
        Map<String, IMethodTransformer> transMap = map.get(className);
        System.out.println(className);

        ClassReader cr = new ClassReader(bytes);
        ClassNode cn = new ClassNode();
        cr.accept(cn, 0);

        List<MethodNode> ml = new ArrayList<MethodNode>(cn.methods);
        for (MethodNode mn : ml) {
            String methodName = FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(obfClassName, mn.name, mn.desc);
            String methodDesc = FMLDeobfuscatingRemapper.INSTANCE.mapMethodDesc(mn.desc);
            if (transMap.containsKey(methodName + methodDesc)) {
                try {
                    transMap.get(methodName + methodDesc).transform(cn,mn);
                } catch (Exception e) {
                    e.printStackTrace();
                    return bytes;
                }
            }
        }

        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
        cn.accept(cw);
        return cw.toByteArray();
    }
}
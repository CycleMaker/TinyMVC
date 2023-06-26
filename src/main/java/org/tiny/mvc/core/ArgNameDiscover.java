package org.tiny.mvc.core;

import com.sun.org.apache.bcel.internal.generic.Type;
import org.objectweb.asm.*;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: wuzihan (wuzihan@youzan.com)
 * @create: 2023-04-27 16 :23
 * @description
 */
public class ArgNameDiscover {
    private Map<Method, List<String>> argNameMap = new ConcurrentHashMap<>();
    public String getArg(Method m, int i) {
        List<String> args = argNameMap.get(m);
        if (Objects.nonNull(args)) {
            return args.get(i);
        } else {
            args = getMethodParamNames(m);
            argNameMap.put(m, args);
            return args.get(i);
        }
    }

    public List<String> discover(Method m){
        List<String> args = argNameMap.get(m);
        if (Objects.nonNull(args)) {
            return args;
        }
        args = getMethodParamNames(m);
        argNameMap.put(m, args);
        return args;
    }

    public static List<String> getMethodParamNames(final Method method) {
        final String methodName = method.getName();
        final Class<?>[] methodParameterTypes = method.getParameterTypes();
        final int methodParameterCount = methodParameterTypes.length;
        final String className = method.getDeclaringClass().getName();
        final boolean isStatic = Modifier.isStatic(method.getModifiers());
        List<String> methodParametersNames = new ArrayList<>(methodParameterCount);
        ClassReader cr;
        try {
            cr = new ClassReader(className);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        cr.accept(new ClassAdapter(cw) {
            public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {

                MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);

                final Type[] argTypes = Type.getArgumentTypes(desc);

                //参数类型不一致
                if (!methodName.equals(name) || !matchTypes(argTypes, methodParameterTypes)) {
                    return mv;
                }
                return new MethodAdapter(mv) {
                    public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
                        //如果是静态方法，第一个参数就是方法参数，非静态方法，则第一个参数是 this ,然后才是方法的参数
                        int methodParameterIndex = isStatic ? index : index - 1;
                        if (0 <= methodParameterIndex && methodParameterIndex < methodParameterCount) {
                            methodParametersNames.add(methodParameterIndex,name);
                        }
                        super.visitLocalVariable(name, desc, signature, start, end, index);
                    }
                };
            }
        }, 0);
        return methodParametersNames;
    }

    /**
     * 比较参数是否一致
     */
    private static boolean matchTypes(Type[] types, Class<?>[] parameterTypes) {
        if (types.length != parameterTypes.length) {
            return false;
        }
        for (int i = 0; i < types.length; i++) {
            if (!Type.getType(parameterTypes[i]).equals(types[i])) {
                return false;
            }
        }
        return true;
    }


    }

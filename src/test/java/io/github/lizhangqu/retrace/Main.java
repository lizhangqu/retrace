package io.github.lizhangqu.retrace;

import java.io.*;
import java.util.Collection;

public class Main {
    public static void main(String[] args) throws IOException {
        Main main = new Main();
        main.readMappingByOriginalName();
        main.readMappingByObfuscatedName();
    }

    void readMappingByOriginalName() {
        System.out.println("**********************************************************************");
        File file = new File(Main.class.getClassLoader().getResource("mapping.txt").getFile());
        Retrace retrace = Retrace.createRetrace(file, true);
        if (retrace == null) {
            System.out.println("retrace is null");
            return;
        }

        ClassMapping retraceClass = retrace.getClassMapping("com.google.common.base.CharMatcher$1");
        System.out.println(retraceClass.getOriginalName() + "=>" + retraceClass.getObfuscatedName());

        String originalName = "com.github.artyomcool.retrace.StackTraceAnalyzer";
        String obfuscatedName = retrace.getObfuscatedName("com.github.artyomcool.retrace.StackTraceAnalyzer");
        System.out.println(originalName + "=>" + obfuscatedName);


        retraceClass = retrace.getClassMapping("com.google.common.base.Joiner$MapJoiner");
        System.out.println(retraceClass.getOriginalName() + "=>" + retraceClass.getObfuscatedName());


        Collection<MethodMapping> methods = retraceClass.getMethods("join");
        System.out.println("count:" + methods.size());
        for (MethodMapping methodMapping : methods) {
            System.out.println("##################################################################");
            System.out.println(methodMapping.getOriginalName() + "=>" + methodMapping.getObfuscatedName());
            System.out.println("return:" + methodMapping.getReturn());
            System.out.println("args:" + methodMapping.getArgs());
            System.out.println("range:" + methodMapping.getRange());
            System.out.println(methodMapping.toString());
        }
    }

    void readMappingByObfuscatedName() throws IOException {
        System.out.println("**********************************************************************");
        File file = new File(Main.class.getClassLoader().getResource("mapping.txt").getFile());
        Retrace retrace = Retrace.createRetrace(file, false);
        if (retrace == null) {
            System.out.println("retrace is null");
            return;
        }

        ClassMapping retraceClass = retrace.getClassMapping("a.a.a.b.n");
        System.out.println(retraceClass.getObfuscatedName() + "=>" + retraceClass.getOriginalName());

        String obfuscatedName = "b.a.a.d";
        String originalName = retrace.getOriginalName("b.a.a.d");
        System.out.println(obfuscatedName + "=>" + originalName);


        retraceClass = retrace.getClassMapping("a.a.a.b.az");
        System.out.println(retraceClass.getObfuscatedName() + "=>" + retraceClass.getOriginalName());

        Collection<MethodMapping> methods = retraceClass.getMethods("a");
        System.out.println("count:" + methods.size());

        for (MethodMapping methodMapping : methods) {
            System.out.println("##################################################################");
            System.out.println(methodMapping.getObfuscatedName() + "=>" + methodMapping.getOriginalName());
            System.out.println("return:" + methodMapping.getReturn());
            System.out.println("args:" + methodMapping.getArgs());
            System.out.println("range:" + methodMapping.getRange());
            System.out.println(methodMapping.toString());
        }

    }
}

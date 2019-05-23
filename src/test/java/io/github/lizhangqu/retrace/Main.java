package io.github.lizhangqu.retrace;

import java.io.*;
import java.util.Collection;

public class Main {
    public static void main(String[] args) {
        Main main = new Main();
        main.readMappingByOriginalName();
        main.readMappingByObfuscatedName();
        main.stacktrace();
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

    void readMappingByObfuscatedName() {
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

    void stacktrace() {
        System.out.println("**********************************************************************");
        File file = new File(Main.class.getClassLoader().getResource("mapping.txt").getFile());
        Retrace retrace = Retrace.createRetrace(file, false);
        if (retrace == null) {
            System.out.println("retrace is null");
            return;
        }

        String originalStacktrace = retrace.stackTrace(
                "java.lang.RuntimeException: some text\n" +
                        "\tat a.a.a.b.av.b(SourceFile:103)\n" +
                        "\tat b.a.a.e.a(SourceFile:62)\n" +
                        "\tat b.a.a.d.a(SourceFile:105)\n" +
                        "Caused by: java.lang.IllegalArgumentException: some text 2\n" +
                        "\tat b.a.a.c.a(SourceFile:40)\n" +
                        "\tat some.unknown.method(SourceFile:76)\n" +
                        "\tat some.unknown.method2(UnknownSource)\n" +
                        "\tat b.a.a.a.a(UnknownSource)\n" +
                        "Caused by: java.lang.NullPointerException\n" +
                        "\tat b.a.a.b.toString(SourceFile:45)\n" +
                        "\t... 2 more");

        System.out.println(originalStacktrace);
    }
}

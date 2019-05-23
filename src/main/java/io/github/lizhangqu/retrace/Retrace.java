package io.github.lizhangqu.retrace;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Retrace {
    private final Map<String, ClassMapping> classes = new HashMap<String, ClassMapping>();
    StackTraceAnalyzer stackTraceAnalyzer;

    /**
     * useOriginalNameAsKey为true的情况下是根据原始类名映射,否则是混淆后的类名
     */
    public static Retrace createRetrace(String filePath, boolean useOriginalNameAsKey) {
        File file = new File(filePath);
        return createRetrace(file, useOriginalNameAsKey);
    }

    /**
     * useOriginalNameAsKey为true的情况下是根据原始类名映射,否则是混淆后的类名
     */
    public static Retrace createRetrace(File file, boolean useOriginalNameAsKey) {
        try {
            if (file == null || !file.exists()) {
                return null;
            }
            BufferedReader reader = readResource(file.getAbsolutePath());
            if (reader == null) {
                return null;
            }
            return new Retrace(reader, useOriginalNameAsKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static BufferedReader readResource(String resource) {
        try {
            InputStream resourceAsStream = new FileInputStream(resource);
            return new BufferedReader(new InputStreamReader(resourceAsStream));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 构造函数读mapping文件
     */
    private Retrace(BufferedReader mappings, boolean useOriginalNameAsKey) throws IOException {
        String line = mappings.readLine();
        ClassMapping currentClass = null;
        while (line != null) {
            boolean isNewClass = currentClass == null || !line.startsWith(" ");
            if (isNewClass) {
                if (currentClass != null) {
                    classes.put(useOriginalNameAsKey ? currentClass.getOriginalName() : currentClass.getObfuscatedName(), currentClass);
                }
                currentClass = new ClassMapping(line);
            } else {
                currentClass.addLine(line, useOriginalNameAsKey);
            }
            line = mappings.readLine();
        }
        if (currentClass != null) {
            classes.put(useOriginalNameAsKey ? currentClass.getOriginalName() : currentClass.getObfuscatedName(), currentClass);
        }
        stackTraceAnalyzer = new StackTraceAnalyzer(classes);
    }

    /**
     * 根据key获得对应的ClassMapping
     * 如果useOriginalNameAsKey是true,则key是原始类名
     * 否则,key是混淆后的类名
     */
    public ClassMapping getClassMapping(String className) {
        return classes.get(className);
    }

    /**
     * 根据key获得混淆后的类名
     */
    public String getObfuscatedName(String name) {
        ClassMapping mapping = classes.get(name);
        if (mapping != null) {
            name = mapping.getObfuscatedName();
            return name;
        }
        return null;
    }

    /**
     * 根据key获得原始类名
     */
    public String getOriginalName(String name) {
        ClassMapping mapping = classes.get(name);
        if (mapping != null) {
            name = mapping.getOriginalName();
            return name;
        }
        return null;
    }


    /**
     * 堆栈还原
     */
    public String stackTrace(String obfuscatedStacktrace) {
        try {
            BufferedReader reader = new BufferedReader(new StringReader(obfuscatedStacktrace));
            StringBuilder builder = new StringBuilder();
            for (; ; ) {
                String line = reader.readLine();
                if (line == null) {
                    return builder.toString();
                }
                stackTraceAnalyzer.appendLine(builder, line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
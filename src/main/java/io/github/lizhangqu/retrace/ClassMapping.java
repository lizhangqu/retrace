package io.github.lizhangqu.retrace;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;

import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClassMapping {

    private static Pattern className = Pattern.compile("(?<name>.*)\\s->\\s(?<obf>.*):");
    private static Pattern methodName = Pattern.compile("\\s+((?<from>\\d+):(?<to>\\d+):)?(?<ret>[^:]+)\\s(?<name>[^:]+)\\((?<args>.*)\\)\\s->\\s(?<obf>[^:]+)");

    private Multimap<String, MethodMapping> methods = MultimapBuilder.hashKeys().arrayListValues().build();
    private String name;
    private String obfuscatedName;

    public ClassMapping(String line) {
        Matcher matcher = className.matcher(line);
        if (!matcher.matches()) {
            throw new IllegalArgumentException();
        }
        name = matcher.group("name");
        obfuscatedName = matcher.group("obf");
    }


    /**
     * 读一行
     */
    void addLine(String line, boolean useOriginalNameAsKey) {
        Matcher matcher = methodName.matcher(line);
        if (!matcher.matches()) {
            return;
        }
        MethodMapping methodMapping = new MethodMapping(matcher);
        methods.put(useOriginalNameAsKey ? methodMapping.getOriginalName() : methodMapping.getObfuscatedName(), methodMapping);
    }

    /**
     * 获得原始类名
     */
    public String getOriginalName() {
        return name;
    }

    /**
     * 获得混淆类名
     */
    public String getObfuscatedName() {
        return obfuscatedName;
    }

    /**
     * 类中方法映射
     */
    public Collection<MethodMapping> getMethods(String name) {
        return methods.get(name);
    }
}
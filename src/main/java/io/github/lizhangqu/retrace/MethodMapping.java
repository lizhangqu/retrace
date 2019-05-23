package io.github.lizhangqu.retrace;

import com.google.common.collect.Range;

import java.util.regex.Matcher;

public class MethodMapping {

    private String ret;
    private String args;
    private String name;
    private String obfuscatedName;
    private Range<Integer> range;

    public MethodMapping(Matcher matcher) {
        obfuscatedName = matcher.group("obf");
        name = matcher.group("name");
        args = matcher.group("args");
        ret = matcher.group("ret");

        String from = matcher.group("from");
        String to = matcher.group("to");

        if (from == null || to == null) {
            return;
        }

        range = Range.closed(Integer.valueOf(from), Integer.valueOf(to));
    }

    /**
     * 获得方法混淆名
     */
    public String getObfuscatedName() {
        return obfuscatedName;
    }

    /**
     * 获得方法原始名
     */
    public String getOriginalName() {
        return name;
    }

    /**
     * 方法行数范围
     */
    public boolean inRange(int line) {
        return range.contains(line);
    }

    /**
     * 获得参数名
     */
    public String getArgs() {
        return args;
    }

    /**
     * 获得方法行数范围
     */
    public Range<Integer> getRange() {
        return range;
    }

    /**
     * 获得访问值
     */
    public String getReturn() {
        return ret;
    }

    @Override
    public String toString() {
        return "MethodMapping{" +
                "ret='" + ret + '\'' +
                ", args='" + args + '\'' +
                ", name='" + name + '\'' +
                ", obfuscatedName='" + obfuscatedName + '\'' +
                ", range=" + range +
                '}';
    }
}
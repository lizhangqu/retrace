package io.github.lizhangqu.retrace;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StackTraceAnalyzer {

    private static final String AT_PREFIX = "\tat ";
    private static final String CAUSED_BY_PREFIX = "Caused by: ";
    private static final Pattern AT_PATTERN = Pattern.compile("\tat (?<class>.*)\\.(?<method>[^.]+)\\((?<source>[^:]+)(:(?<line>\\d+))?\\)");

    private final Map<String, ClassMapping> classes;

    public StackTraceAnalyzer(Map<String, ClassMapping> classes) {
        this.classes = classes;
    }

    public void appendLine(StringBuilder builder, String line) {
        if (line.startsWith(AT_PREFIX)) {
            appendAt(builder, line);
        } else {
            if (line.startsWith(CAUSED_BY_PREFIX)) {
                line = line.substring(CAUSED_BY_PREFIX.length());
                builder.append(CAUSED_BY_PREFIX);
            }
            appendException(builder, line);
        }
        builder.append('\n');
    }

    private void appendAt(StringBuilder builder, String line) {
        Matcher matcher = AT_PATTERN.matcher(line);
        if (!matcher.matches()) {
            builder.append(line);
            return;
        }

        String className = matcher.group("class");
        String methodName = matcher.group("method");
        String source = matcher.group("source");
        String lineNumber = matcher.group("line");
        final int lineNumberInt = lineNumber == null ? -1 : Integer.parseInt(lineNumber);

        ClassMapping mapping = classes.get(className);
        if (mapping == null) {
            builder.append(line);
            return;
        }

        builder.append(AT_PREFIX)
                .append(mapping.getOriginalName())
                .append('.');
        Collection<MethodMapping> methods = mapping.getMethods(methodName);
        Iterable<MethodMapping> filtered = lineNumberInt == -1
                ? methods
                : Iterables.filter(methods, new Predicate<MethodMapping>() {
            @Override
            public boolean apply(MethodMapping input) {
                return input.inRange(lineNumberInt);
            }
        });
        Iterator<MethodMapping> iterator = filtered.iterator();
        if (!iterator.hasNext()) {
            appendMethod(builder, methodName, source, lineNumber);
            return;
        }
        MethodMapping first = iterator.next();
        appendMethod(builder, first.getOriginalName(), source, lineNumber);
        while (iterator.hasNext()) {
            MethodMapping next = iterator.next();
            builder.append("\n\t\t\t\t").append(next.getOriginalName());
        }
    }

    private void appendMethod(StringBuilder builder, String methodName, String source, String lineNumber) {
        builder.append(methodName)
                .append('(')
                .append(source);
        if (lineNumber != null) {
            builder.append(':')
                    .append(lineNumber);
        }
        builder.append(')');
    }

    private void appendException(StringBuilder builder, String line) {
        int pos = line.indexOf(':');
        if (pos == -1) {
            builder.append(line);
            return;
        }
        String className = line.substring(0, pos);
        String tail = line.substring(pos);
        builder.append(resolveClassName(className))
                .append(tail);
    }

    private String resolveClassName(String obfuscatedName) {
        ClassMapping mapping = classes.get(obfuscatedName);
        if (mapping == null) {
            return obfuscatedName;
        }
        return mapping.getOriginalName();
    }

}

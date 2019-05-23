package io.github.lizhangqu.retrace;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;

public class Retrace {

	public static final Joiner METHODS_JOINER = Joiner.on("/");
	private final Map<String, ClassMapping> classes = new HashMap<>();
	private final StackTraceAnalyzer analyzer;

	public Retrace(BufferedReader mappings) throws IOException {
		String line = mappings.readLine();
		ClassMapping currentClass = null;
		while (line != null) {
			boolean isNewClass = currentClass == null || !line.startsWith(" ");
			if (isNewClass) {
				if (currentClass != null) {
					classes.put(currentClass.getObfuscatedName(), currentClass);
				}
				currentClass = new ClassMapping(line);
			} else {
				currentClass.addLine(line);
			}
			line = mappings.readLine();
		}
		if (currentClass != null) {
			classes.put(currentClass.getObfuscatedName(), currentClass);
		}
		analyzer = new StackTraceAnalyzer(classes);
	}

	public ClassMapping getClass(String className) {
		return classes.get(className);
	}

	public String stackTrace(BufferedReader reader) throws IOException {
		StringBuilder builder = new StringBuilder();
		for(;;) {
			String line = reader.readLine();
			if (line == null) {
				return builder.toString();
			}
			analyzer.appendLine(builder, line);
		}
	}

	public StackTrace stackTrace(StackTrace obfuscated) {
		StackTrace result = new StackTrace();
		StackTrace currentCause = result;
		for(;;) {
			currentCause.setMessage(obfuscated.getMessage());
			currentCause.setException(className(obfuscated.getException()));
			currentCause.setLines(lines(obfuscated.getLines()));

			obfuscated = obfuscated.getCausedBy();
			if (obfuscated == null) {
				return result;
			}
			StackTrace causedBy = new StackTrace();
			currentCause.setCausedBy(causedBy);
			currentCause = causedBy;
		}
	}

	public String className(String name) {
		ClassMapping mapping = classes.get(name);
		if (mapping != null) {
			name = mapping.getName();
		}
		return name;
	}

	public String methodName(String className, String methodName, final Integer lineNumber) {
		ClassMapping mapping = classes.get(className);
		if (mapping == null) {
			return methodName;
		}
		Collection<MethodMapping> methods = mapping.getMethods(methodName);
		Iterable<MethodMapping> filtered = lineNumber == null
				? methods
				: Iterables.filter(methods, new Predicate<MethodMapping>() {
			@Override
			public boolean apply(MethodMapping input) {
				return input.inRange(lineNumber);
			}
		});
		Iterator<MethodMapping> iterator = filtered.iterator();
		if (!iterator.hasNext()) {
			return methodName;
		}
		return METHODS_JOINER.join(Iterators.transform(iterator, new Function<MethodMapping, String>() {
			@Override
			public String apply(MethodMapping input) {
				return input.getName();
			}
		}));
	}

	private List<Line> lines(List<Line> lines) {
		List<Line> result = new ArrayList<>(lines.size());
		for (Line line : lines) {
			result.add(line(line));
		}
		return result;
	}

	private Line line(Line line) {
		Line result = new Line();
		result.setClassName(className(line.getClassName()));
		result.setMethodName(methodName(line.getClassName(), line.getMethodName(), line.getLineNumber()));
		result.setFileName(line.getFileName());
		result.setLineNumber(line.getLineNumber());
		return result;
	}

}

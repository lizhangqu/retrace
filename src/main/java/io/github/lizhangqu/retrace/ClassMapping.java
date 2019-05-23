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

	public String getObfuscatedName() {
		return obfuscatedName;
	}

	public void addLine(String line) {
		Matcher matcher = methodName.matcher(line);
		if (!matcher.matches()) {
			return;
		}
		MethodMapping methodMapping = new MethodMapping(matcher);
		methods.put(methodMapping.getObfuscatedName(), methodMapping);
	}

	public String getName() {
		return name;
	}

	public Collection<MethodMapping> getMethods(String obfuscatedName) {
		return methods.get(obfuscatedName);
	}
}

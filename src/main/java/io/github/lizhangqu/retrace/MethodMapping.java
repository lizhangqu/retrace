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

	public String getObfuscatedName() {
		return obfuscatedName;
	}

	public String getName() {
		return name;
	}

	public boolean inRange(int line) {
		return range.contains(line);
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

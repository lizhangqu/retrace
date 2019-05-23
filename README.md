retrace
=======

### Maven

```
<dependency>
	<groupId>io.github.lizhangqu</groupId>
	<artifactId>retrace</artifactId>
	<version>1.0.2</version>
</dependency>
```

### Gradle

```
implementation 'io.github.lizhangqu:retrace:1.0.2'
```

### Sample

根据原始class名或方法名获取混淆后的class名或方法名

```
Retrace retrace = Retrace.createRetrace(mappingFile, true);
ClassMapping retraceClass = retrace.getClassMapping("com.google.common.base.CharMatcher$1");
String originalName = retraceClass.getOriginalName();
String obfuscatedName = retraceClass.getObfuscatedName();


Collection<MethodMapping> methods = retraceClass.getMethods("join");
for (MethodMapping methodMapping : methods) {
    String originalMethodName = methodMapping.getOriginalName();
    String obfuscatedMethodName = methodMapping.getObfuscatedName();
    String methodReturn = methodMapping.getReturn();
    String methodArgs = methodMapping.getArgs();
    String methodRange = methodMapping.getRange();
}
```

根据混淆后的class名或方法名获取原始class名或方法名

```
Retrace retrace = Retrace.createRetrace(mappingFile, false);
ClassMapping retraceClass = retrace.getClassMapping("a.a.a.b.n");
String obfuscatedName = retraceClass.getObfuscatedName();
String originalName = retraceClass.getOriginalName();


Collection<MethodMapping> methods = retraceClass.getMethods("a");
for (MethodMapping methodMapping : methods) {
    String obfuscatedMethodName = methodMapping.getObfuscatedName();
    String originalMethodName = methodMapping.getOriginalName();
    String methodReturn = methodMapping.getReturn();
    String methodArgs = methodMapping.getArgs();
    String methodRange = methodMapping.getRange();
}
```

堆栈还原

```
Retrace retrace = Retrace.createRetrace(mappingFile, false);
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
```

### Thanks
 
 - [https://github.com/Artyomcool/retrace](https://github.com/Artyomcool/retrace)
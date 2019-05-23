retrace
=======

### Maven

```
<dependency>
	<groupId>io.github.lizhangqu</groupId>
	<artifactId>retrace</artifactId>
	<version>1.0.0</version>
</dependency>
```

### Gradle

```
implementation 'io.github.lizhangqu:retrace:1.0.0'
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

### Thanks
 
 - [https://github.com/Artyomcool/retrace](https://github.com/Artyomcool/retrace)
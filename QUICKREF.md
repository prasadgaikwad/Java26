# Java 26 Quick Reference

## Installation
```bash
sdk install java 26.ea.35-open
sdk use java 26.ea.35-open
java --version
```

## Run Everything
```bash
./run-all.sh
```

## Feature Summary

### ✅ Final Features (Production Ready)

1. **Primitive Types in Patterns (JEP 455)**
   - Pattern match on `int`, `long`, `double`, etc.
   - Use in `instanceof` and `switch`
   - File: `PrimitiveTypesInPatterns.java`

2. **Stream Gatherers (JEP 473)**
   - Custom stream transformations
   - `windowFixed()`, `windowSliding()`, `scan()`, `fold()`
   - File: `StreamGatherers.java`

3. **Scoped Values (JEP 481)**
   - Immutable thread-local alternative
   - Better than ThreadLocal
   - File: `ScopedValues.java`

### 🔬 Preview Features (Experimental)

4. **Module Import Declarations (JEP 476)**
   - `import module java.base;`
   - Import entire modules
   - File: `ModuleImportDeclarations.java`

5. **Flexible Constructor Bodies (JEP 482)**
   - Statements before `super()` or `this()`
   - Better validation and preparation
   - File: `FlexibleConstructorBodies.java`

## Compilation Commands

### All Features
```bash
~/.sdkman/candidates/java/26.ea.35-open/bin/javac \
  --enable-preview --source 26 \
  src/features/*.java -d build/
```

### Individual Feature
```bash
~/.sdkman/candidates/java/26.ea.35-open/bin/javac \
  --enable-preview --source 26 \
  src/features/PrimitiveTypesInPatterns.java -d build/
```

## Run Commands

### Any Feature
```bash
~/.sdkman/candidates/java/26.ea.35-open/bin/java \
  --enable-preview -cp build \
  features.<ClassName>
```

### Examples
```bash
# Primitive patterns
java --enable-preview -cp build features.PrimitiveTypesInPatterns

# Stream gatherers
java --enable-preview -cp build features.StreamGatherers

# Scoped values
java --enable-preview -cp build features.ScopedValues

# Flexible constructors
java --enable-preview -cp build features.FlexibleConstructorBodies

# Module imports
java --enable-preview -cp build features.ModuleImportDeclarations
```

## Code Examples

### Primitive Patterns
```java
if (obj instanceof int i) {
    System.out.println("Got int: " + i);
}

switch (value) {
    case int i when i < 100 -> "Small";
    case int i -> "Large";
    default -> "Not an int";
}
```

### Stream Gatherers
```java
// Fixed windows
numbers.stream()
    .gather(Gatherers.windowFixed(3))
    .forEach(System.out::println);

// Running sum
numbers.stream()
    .gather(Gatherers.scan(() -> 0, Integer::sum))
    .forEach(System.out::println);
```

### Scoped Values
```java
ScopedValue<String> USER_ID = ScopedValue.newInstance();

ScopedValue.where(USER_ID, "user-123").run(() -> {
    System.out.println(USER_ID.get());
});
```

### Flexible Constructors
```java
class Circle extends Shape {
    public Circle(double radius) {
        if (radius <= 0) throw new IllegalArgumentException();
        double area = Math.PI * radius * radius;
        super("Circle", area);  // super() no longer needs to be first!
    }
}
```

### Module Imports
```java
import module java.base;  // Instead of many individual imports
```

## What's New in Java 26?

### Developer Productivity
- ✅ Less boilerplate (module imports)
- ✅ More expressive (primitive patterns)
- ✅ Better validation (flexible constructors)

### Performance
- ✅ Stream gatherers for complex transformations
- ✅ Scoped values more efficient than ThreadLocal

### Safety
- ✅ Immutable scoped values
- ✅ Type-safe primitive patterns
- ✅ Constructor validation before super()

## Migration Tips

### From ThreadLocal to ScopedValue
```java
// OLD
ThreadLocal<String> userId = new ThreadLocal<>();
userId.set("user-123");
// ... need to clean up manually

// NEW
ScopedValue<String> userId = ScopedValue.newInstance();
ScopedValue.where(userId, "user-123").run(() -> {
    // automatic cleanup!
});
```

### Pattern Matching
```java
// OLD
if (obj instanceof Integer) {
    Integer i = (Integer) obj;
    // use i
}

// NEW
if (obj instanceof int i) {
    // use i directly!
}
```

### Stream Processing
```java
// OLD - complex custom collector
stream.collect(customWindowingCollector())

// NEW - simple gatherer
stream.gather(Gatherers.windowFixed(3))
```

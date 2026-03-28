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

1. **Primitive Types in Patterns (JEP 455/530)**
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

4. **HTTP/3 Client (JEP 517)**
   - HTTP/3 support with QUIC protocol
   - Faster connections, better mobile support
   - File: `Http3Client.java`

### 🔬 Preview Features (Experimental)

5. **Module Import Declarations (JEP 476)**
   - `import module java.base;`
   - Import entire modules
   - File: `ModuleImportDeclarations.java`

6. **Flexible Constructor Bodies (JEP 482)**
   - Statements before `super()` or `this()`
   - Better validation and preparation
   - File: `FlexibleConstructorBodies.java`

7. **PEM Encodings (JEP 524)**
   - Native PEM encoding/decoding
   - No external crypto libraries needed
   - File: `PemEncodings.java`

8. **Structured Concurrency (JEP 525)**
   - Treat concurrent tasks as single unit
   - Better error handling and cancellation
   - File: `StructuredConcurrency.java`

9. **Lazy Constants (JEP 526)**
   - Deferred initialization for constants
   - Improved startup performance
   - File: `LazyConstants.java`

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

# Lazy constants
java --enable-preview -cp build features.LazyConstants

# Structured concurrency
java --enable-preview -cp build features.StructuredConcurrency

# HTTP/3 client
java --enable-preview -cp build features.Http3Client

# PEM encodings
java --enable-preview -cp build features.PemEncodings
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

### HTTP/3
```java
HttpClient client = HttpClient.newBuilder()
    .version(HttpClient.Version.HTTP_3)
    .build();

HttpResponse<String> response = client.send(
    request, HttpResponse.BodyHandlers.ofString()
);
```

### Lazy Constants
```java
// Traditional - eager initialization
static final String EAGER = "Eager: " + expensiveInit();

// Lazy - deferred until first use
lazy static final String LAZY = "Lazy: " + expensiveInit();
```

### Structured Concurrency
```java
try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
    Future<String> user = scope.fork(() -> fetchUser());
    Future<String> orders = scope.fork(() -> fetchOrders());
    
    scope.join();
    scope.throwIfFailed();
    
    return user.resultNow() + orders.resultNow();
}
```

### PEM Encodings
```java
// Encode key to PEM
String pem = PemEncoder.encode(privateKey);

// Decode PEM to key
PrivateKey key = PemDecoder.decodePrivateKey(pem);
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

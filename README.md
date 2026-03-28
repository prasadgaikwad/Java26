# Java 26 Features Exploration

This project demonstrates the key features introduced in **Java 26** (released March 2026) with practical, runnable examples.

## Prerequisites

- SDKMAN installed
- Java 26 (installed via SDKMAN)

## Installation

```bash
# Install Java 26 using SDKMAN
sdk install java 26.ea.35-open

# Use Java 26
sdk use java 26.ea.35-open

# Verify installation
java --version
```

## Java 26 Features

### 1. 🎯 Primitive Types in Patterns (JEP 455/530) - **PREVIEW**
**File:** [`src/features/PrimitiveTypesInPatterns.java`](src/features/PrimitiveTypesInPatterns.java)

Pattern matching now supports primitive types directly in `instanceof` and `switch` expressions.

**Key Benefits:**
- ✅ Pattern match on `int`, `long`, `double`, etc.
- ✅ Type-safe primitive conversions
- ✅ More expressive switch statements
- ✅ Cleaner code without wrapper classes

**Example:**
```java
if (obj instanceof int i) {
    System.out.println("Integer: " + i);
}

switch (value) {
    case int i when i < 100 -> "Small: " + i;
    case int i -> "Large: " + i;
    case double d -> "Decimal: " + d;
}
```

**Run:**
```bash
java src/features/PrimitiveTypesInPatterns.java
```

---

### 2. 📦 Module Import Declarations (JEP 476) - **PREVIEW**
**File:** [`src/features/ModuleImportDeclarations.java`](src/features/ModuleImportDeclarations.java)

Import all public packages from a module with a single statement.

**Key Benefits:**
- ✅ Reduces boilerplate imports
- ✅ Perfect for prototyping and scripts
- ✅ Cleaner educational examples
- ✅ Easier exploration of APIs

**Example:**
```java
// Instead of multiple imports:
import java.util.List;
import java.util.Map;
import java.time.LocalDate;

// Use a single module import:
import module java.base;
```

**Run:**
```bash
java --enable-preview --source 26 src/features/ModuleImportDeclarations.java
```

---

### 3. 🔨 Flexible Constructor Bodies (JEP 482) - **PREVIEW**
**File:** [`src/features/FlexibleConstructorBodies.java`](src/features/FlexibleConstructorBodies.java)

Execute statements **before** `super()` or `this()` calls in constructors.

**Key Benefits:**
- ✅ Validate arguments before calling parent constructor
- ✅ Calculate values needed for super() call
- ✅ More natural code organization
- ✅ Eliminates helper method workarounds

**Example:**
```java
class Circle extends Shape {
    public Circle(double radius) {
        // Validate BEFORE super()!
        if (radius <= 0) {
            throw new IllegalArgumentException("Invalid radius");
        }
        
        // Calculate before super()
        double area = Math.PI * radius * radius;
        
        super("Circle", area);  // Now call super
        this.radius = radius;
    }
}
```

**Run:**
```bash
java --enable-preview --source 26 src/features/FlexibleConstructorBodies.java
```

---

### 4. 🌊 Stream Gatherers (JEP 473) - **FINAL**
**File:** [`src/features/StreamGatherers.java`](src/features/StreamGatherers.java)

A powerful new intermediate stream operation for custom transformations.

**Key Benefits:**
- ✅ Fixed and sliding windows
- ✅ Running calculations (scan, fold)
- ✅ Complex stateful transformations
- ✅ Better than custom collectors

**Built-in Gatherers:**
- `windowFixed(n)` - Fixed-size windows
- `windowSliding(n)` - Sliding windows
- `scan()` - Running accumulation
- `fold()` - Prefix reduction

**Example:**
```java
// Moving average with sliding window
values.stream()
    .gather(Gatherers.windowSliding(3))
    .map(window -> window.stream().mapToDouble(Double::doubleValue).average().orElse(0.0))
    .forEach(System.out::println);

// Running sum
numbers.stream()
    .gather(Gatherers.scan(() -> 0, (sum, num) -> sum + num))
    .forEach(System.out::println);
```

**Run:**
```bash
java src/features/StreamGatherers.java
```

---

### 5. 🔐 Scoped Values (JEP 481) - **FINAL**
**File:** [`src/features/ScopedValues.java`](src/features/ScopedValues.java)

Immutable, scoped alternative to `ThreadLocal` for sharing data across threads.

**Key Benefits:**
- ✅ Immutable and thread-safe
- ✅ Automatic cleanup (no memory leaks)
- ✅ Inherited by child threads
- ✅ Better performance than ThreadLocal
- ✅ Perfect for virtual threads

**Use Cases:**
- Request context (user ID, request ID)
- Security context
- Distributed tracing
- Locale/timezone information

**Example:**
```java
private static final ScopedValue<String> USER_ID = ScopedValue.newInstance();
private static final ScopedValue<String> REQUEST_ID = ScopedValue.newInstance();

ScopedValue.where(USER_ID, "user-123")
    .where(REQUEST_ID, "req-456")
    .run(() -> {
        System.out.println("User: " + USER_ID.get());
        processRequest();
    });
```

**Run:**
```bash
java src/features/ScopedValues.java
```

---

### 6. ⚡ Lazy Constants (JEP 526) - **PREVIEW**
**File:** [`src/features/LazyConstants.java`](src/features/LazyConstants.java)

Lazy constants are initialized only when first accessed, improving startup performance.

**Key Benefits:**
- ✅ Deferred initialization until first use
- ✅ Improved application startup time
- ✅ Thread-safe initialization
- ✅ No manual lazy initialization boilerplate

**Example:**
```java
// Traditional - initialized at class loading
static final String EAGER = "Eager: " + Instant.now();

// Lazy - initialized only when accessed
lazy static final String LAZY = "Lazy: " + Instant.now();
```

**Run:**
```bash
java --enable-preview --source 26 src/features/LazyConstants.java
```

---

### 7. 🔄 Structured Concurrency (JEP 525) - **PREVIEW**
**File:** [`src/features/StructuredConcurrency.java`](src/features/StructuredConcurrency.java)

Treat multiple tasks running in different threads as a single unit of work.

**Key Benefits:**
- ✅ All tasks complete or fail together
- ✅ Automatic cancellation on failure
- ✅ Clear thread ownership and lifecycle
- ✅ Better error propagation
- ✅ No thread leaks

**Example:**
```java
try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
    Future<String> user = scope.fork(() -> fetchUser());
    Future<String> orders = scope.fork(() -> fetchOrders());
    
    scope.join();           // Wait for all
    scope.throwIfFailed();  // Check for errors
    
    return user.resultNow() + ", " + orders.resultNow();
}
```

**Run:**
```bash
java --enable-preview --source 26 src/features/StructuredConcurrency.java
```

---

### 8. 🌐 HTTP/3 Client (JEP 517) - **FINAL**
**File:** [`src/features/Http3Client.java`](src/features/Http3Client.java)

HTTP/3 support for java.net.http.HttpClient using QUIC protocol.

**Key Benefits:**
- ✅ Faster connection establishment (0-RTT)
- ✅ Better performance on lossy networks
- ✅ Connection migration (survives IP changes)
- ✅ No head-of-line blocking
- ✅ Built on UDP instead of TCP

**Example:**
```java
HttpClient client = HttpClient.newBuilder()
    .version(HttpClient.Version.HTTP_3)
    .build();

HttpRequest request = HttpRequest.newBuilder()
    .uri(URI.create("https://example.com"))
    .GET()
    .build();

HttpResponse<String> response = client.send(
    request, 
    HttpResponse.BodyHandlers.ofString()
);
```

**Run:**
```bash
java src/features/Http3Client.java
```

---

### 9. 🔐 PEM Encodings (JEP 524) - **PREVIEW**
**File:** [`src/features/PemEncodings.java`](src/features/PemEncodings.java)

Standard API for reading and writing PEM-encoded cryptographic objects.

**Key Benefits:**
- ✅ Native PEM support in JDK
- ✅ Read/write keys and certificates
- ✅ No need for external libraries
- ✅ Standardized format handling
- ✅ Supports encrypted PEM files

**Example:**
```java
// Encode key to PEM format
String pem = PemEncoder.encode(keyPair.getPrivate());

// Decode PEM back to key
PrivateKey key = PemDecoder.decodePrivateKey(pem);
```

**Run:**
```bash
java --enable-preview --source 26 src/features/PemEncodings.java
```

---

## Quick Start

### Run All Examples (Easiest Method)

```bash
# Navigate to project directory
cd /Users/prasadgaikwad/Code/Java/Java26

# Run all examples at once
./run-all.sh
```

### Run Individual Examples

```bash
# Set up environment
JAVA_HOME=~/.sdkman/candidates/java/26.ea.35-open
JAVAC=$JAVA_HOME/bin/javac
JAVA=$JAVA_HOME/bin/java

# Compile all features
$JAVAC --enable-preview --source 26 src/features/*.java -d build/

# Run individual examples
$JAVA --enable-preview -cp build features.PrimitiveTypesInPatterns
$JAVA --enable-preview -cp build features.StreamGatherers
$JAVA --enable-preview -cp build features.ScopedValues
$JAVA --enable-preview -cp build features.FlexibleConstructorBodies
$JAVA --enable-preview -cp build features.ModuleImportDeclarations
$JAVA --enable-preview -cp build features.LazyConstants
$JAVA --enable-preview -cp build features.StructuredConcurrency
$JAVA --enable-preview -cp build features.Http3Client
$JAVA --enable-preview -cp build features.PemEncodings
```

## Project Structure

```
Java26/
├── README.md                                    # This file
├── QUICKREF.md                                  # Quick reference guide
├── run-all.sh                                   # Run all examples
└── src/
    └── features/
        ├── PrimitiveTypesInPatterns.java       # JEP 455/530 (Preview)
        ├── ModuleImportDeclarations.java       # JEP 476 (Preview)
        ├── StreamGatherers.java                # JEP 473 (Final)
        ├── ScopedValues.java                   # JEP 481 (Final)
        ├── FlexibleConstructorBodies.java      # JEP 482 (Preview)
        ├── LazyConstants.java                  # JEP 526 (Preview)
        ├── StructuredConcurrency.java          # JEP 525 (Preview)
        ├── Http3Client.java                    # JEP 517 (Final)
        └── PemEncodings.java                   # JEP 524 (Preview)
```

## Feature Status

| Feature | JEP | Status | Preview Flag Required |
|---------|-----|--------|----------------------|
| Primitive Types in Patterns | 455/530 | 🔬 Preview | Yes |
| Module Import Declarations | 476 | 🔬 Preview | Yes |
| Stream Gatherers | 473 | ✅ Final | No |
| Scoped Values | 481 | ✅ Final | No |
| Flexible Constructor Bodies | 482 | 🔬 Preview | Yes |
| HTTP/3 Client | 517 | ✅ Final | No |
| PEM Encodings | 524 | 🔬 Preview | Yes |
| Structured Concurrency | 525 | 🔬 Preview | Yes |
| Lazy Constants | 526 | 🔬 Preview | Yes |

**Note:** JEPs 500, 504, 516, 522, and 529 are infrastructure/internal improvements without user-facing code examples.

## Additional Resources

- [Java 26 Release Notes](https://openjdk.org/projects/jdk/26/)
- [JEP Index](https://openjdk.org/jeps/0)
- [Java Documentation](https://docs.oracle.com/en/java/javase/26/)

## Additional JEPs (No Code Examples)

The following JEPs are included in Java 26 but are internal/infrastructure improvements:

- **JEP 500**: Prepare to Make Final Mean Final (Language change preparation)
- **JEP 504**: Remove the Applet API (Removal of deprecated API)
- **JEP 516**: Ahead-of-Time Object Caching with Any GC (JVM optimization)
- **JEP 522**: G1 GC: Improve Throughput by Reducing Synchronization (GC improvement)
- **JEP 529**: Vector API (Eleventh Incubator) (Incubating API)

## Notes

- **Preview Features**: Many features are in preview and require `--enable-preview --source 26` flags
- **Virtual Threads**: Java 26 continues to improve virtual threads (used in Structured Concurrency)
- **Performance**: Focus on startup performance (Lazy Constants), network performance (HTTP/3), and GC improvements
- **Security**: Enhanced cryptographic support with native PEM encoding

## License

This is an educational project for exploring Java 26 features.

---

**Happy Coding with Java 26! 🚀**

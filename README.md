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

### 1. 🎯 Primitive Types in Patterns (JEP 455) - **FINAL**
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
```

## Project Structure

```
Java26/
├── README.md                                    # This file
└── src/
    └── features/
        ├── PrimitiveTypesInPatterns.java       # JEP 455 (Final)
        ├── ModuleImportDeclarations.java       # JEP 476 (Preview)
        ├── FlexibleConstructorBodies.java      # JEP 482 (Preview)
        ├── StreamGatherers.java                # JEP 473 (Final)
        └── ScopedValues.java                   # JEP 481 (Final)
```

## Feature Status

| Feature | JEP | Status | Preview Flag Required |
|---------|-----|--------|----------------------|
| Primitive Types in Patterns | 455 | ✅ Final | No |
| Module Import Declarations | 476 | 🔬 Preview | Yes |
| Flexible Constructor Bodies | 482 | 🔬 Preview | Yes |
| Stream Gatherers | 473 | ✅ Final | No |
| Scoped Values | 481 | ✅ Final | No |

## Additional Resources

- [Java 26 Release Notes](https://openjdk.org/projects/jdk/26/)
- [JEP Index](https://openjdk.org/jeps/0)
- [Java Documentation](https://docs.oracle.com/en/java/javase/26/)

## Notes

- **Preview Features**: Some features are in preview and require `--enable-preview --source 26` flags
- **Virtual Threads**: Java 26 continues to improve virtual threads (Project Loom)
- **Performance**: Many features focus on performance and developer ergonomics

## License

This is an educational project for exploring Java 26 features.

---

**Happy Coding with Java 26! 🚀**

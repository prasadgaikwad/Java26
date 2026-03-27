package features;

/**
 * JEP 455: Primitive Types in Patterns (Final)
 * 
 * Feature: Pattern matching now supports primitive types directly.
 * Before Java 26, you could only pattern match on reference types.
 * Now you can pattern match on int, long, double, etc.
 * 
 * Benefits:
 * - More concise and expressive code
 * - Type-safe primitive conversions
 * - Better integration with switch expressions
 */
public class PrimitiveTypesInPatterns {

    public static void main(String[] args) {
        System.out.println("=== Primitive Types in Patterns ===\n");
        
        // Example 1: Pattern matching with primitives in instanceof
        Object obj1 = 42;
        Object obj2 = 3.14;
        Object obj3 = "Hello";
        
        processValue(obj1);
        processValue(obj2);
        processValue(obj3);
        
        // Example 2: Switch with primitive patterns
        System.out.println("\n--- Switch with Primitive Patterns ---");
        demonstrateSwitchWithPrimitives(100);
        demonstrateSwitchWithPrimitives(Integer.MAX_VALUE);
        demonstrateSwitchWithPrimitives(3.14159);
        
        // Example 3: Numeric conversion patterns
        System.out.println("\n--- Numeric Conversion Patterns ---");
        convertAndPrint(128);
        convertAndPrint(300);
        convertAndPrint(70000);
    }
    
    static void processValue(Object obj) {
        if (obj instanceof int i) {
            System.out.println("Integer value: " + i + " (squared: " + (i * i) + ")");
        } else if (obj instanceof double d) {
            System.out.println("Double value: " + d + " (sqrt: " + Math.sqrt(d) + ")");
        } else {
            System.out.println("Other type: " + obj);
        }
    }
    
    static void demonstrateSwitchWithPrimitives(Object value) {
        String result = switch (value) {
            case int i when i < 0 -> "Negative integer: " + i;
            case int i when i < 100 -> "Small integer: " + i;
            case int i -> "Large integer: " + i;
            case double d -> "Decimal number: " + d;
            default -> "Not a number: " + value;
        };
        System.out.println(result);
    }
    
    static void convertAndPrint(Object value) {
        // Safe numeric conversion using patterns
        switch (value) {
            case byte b -> 
                System.out.println("Byte: " + b);
            case short s -> 
                System.out.println("Short: " + s);
            case int i when i >= Byte.MIN_VALUE && i <= Byte.MAX_VALUE -> 
                System.out.println("Int " + i + " fits in byte: " + (byte)i);
            case int i when i >= Short.MIN_VALUE && i <= Short.MAX_VALUE -> 
                System.out.println("Int " + i + " fits in short: " + (short)i);
            case int i -> 
                System.out.println("Int " + i + " requires full int");
            default -> 
                System.out.println("Not a convertible number");
        }
    }
}

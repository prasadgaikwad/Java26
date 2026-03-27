package features;

/**
 * JEP 482: Flexible Constructor Bodies (Preview)
 * 
 * Feature: Allows statements to appear before explicit constructor invocations
 * (super() or this() calls) in constructors.
 * 
 * Before Java 26:
 * - super() or this() must be the FIRST statement in a constructor
 * - Cannot validate or prepare arguments before calling super/this
 * 
 * After Java 26:
 * - Can execute statements before super() or this()
 * - Can validate arguments, perform calculations, etc.
 * - More flexible constructor initialization
 * 
 * Benefits:
 * - Better argument validation
 * - More natural code organization
 * - Eliminates need for static helper methods or factory methods
 * - Improved readability
 */
public class FlexibleConstructorBodies {
    
    public static void main(String[] args) {
        System.out.println("=== Flexible Constructor Bodies ===\n");
        
        System.out.println("Creating shapes with validation:");
        try {
            Circle circle = new Circle(5.0);
            System.out.println(circle);
            
            Rectangle rectangle = new Rectangle(4.0, 6.0);
            System.out.println(rectangle);
            
            // This should throw an exception
            Circle invalidCircle = new Circle(-5.0);
        } catch (IllegalArgumentException e) {
            System.out.println("Caught expected error: " + e.getMessage());
        }
        
        System.out.println("\n" + new SubClassExample("Test", 42));
    }
}

// Base class for demonstration
class Shape {
    private final String type;
    private final double area;
    
    public Shape(String type, double area) {
        this.type = type;
        this.area = area;
    }
    
    @Override
    public String toString() {
        return String.format("%s with area: %.2f", type, area);
    }
}

// Demonstrates flexible constructor body with validation before super()
class Circle extends Shape {
    private final double radius;
    
    public Circle(double radius) {
        // JAVA 26: Can now validate BEFORE calling super()!
        if (radius <= 0) {
            throw new IllegalArgumentException("Radius must be positive, got: " + radius);
        }
        
        // Calculate area before super() call
        double calculatedArea = Math.PI * radius * radius;
        
        // Now call super with the calculated value
        super("Circle", calculatedArea);
        this.radius = radius;
    }
    
    @Override
    public String toString() {
        return super.toString() + String.format(" (radius: %.2f)", radius);
    }
}

class Rectangle extends Shape {
    private final double width;
    private final double height;
    
    public Rectangle(double width, double height) {
        // Validate both dimensions before super()
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException(
                String.format("Dimensions must be positive: width=%.2f, height=%.2f", 
                             width, height));
        }
        
        // Pre-process and calculate
        double area = width * height;
        
        super("Rectangle", area);
        this.width = width;
        this.height = height;
    }
    
    @Override
    public String toString() {
        return super.toString() + 
               String.format(" (width: %.2f, height: %.2f)", width, height);
    }
}

// Example with this() call
class SubClassExample {
    private final String name;
    private final int value;
    private final String processedName;
    
    public SubClassExample(String name, int value) {
        // Can now process arguments before this() call
        String processed = name != null ? name.trim().toUpperCase() : "DEFAULT";
        int validated = Math.max(0, value);
        
        this(processed, validated, true);
    }
    
    private SubClassExample(String processedName, int value, boolean internal) {
        this.name = processedName;
        this.value = value;
        this.processedName = processedName;
    }
    
    @Override
    public String toString() {
        return String.format("SubClassExample{name='%s', value=%d}", name, value);
    }
}

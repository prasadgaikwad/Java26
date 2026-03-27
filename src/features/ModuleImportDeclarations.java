package features;

// JEP 476: Module Import Declarations (Preview)
// 
// Note: This is a preview feature requiring --enable-preview flag
// Module imports allow importing all packages from a module at once
//
// Syntax: import module <module-name>;
//
// Example:
// import module java.base;  // Imports all public packages from java.base
//
// Benefits:
// - Reduces boilerplate import statements
// - Makes prototyping and scripting easier
// - Better for educational purposes

import java.util.*;
import java.time.*;
import java.nio.file.*;

/**
 * Module Import Declarations Demo
 * 
 * While we can't demonstrate the full module import syntax in this simple example
 * (as it requires proper module setup), here's what it enables:
 * 
 * Instead of:
 *   import java.util.List;
 *   import java.util.Map;
 *   import java.util.Set;
 *   import java.time.LocalDate;
 *   import java.time.Duration;
 * 
 * You can write:
 *   import module java.base;
 * 
 * This imports all public packages from the java.base module.
 */
public class ModuleImportDeclarations {
    
    public static void main(String[] args) {
        System.out.println("=== Module Import Declarations ===\n");
        
        System.out.println("This feature allows importing entire modules:");
        System.out.println("Syntax: import module <module-name>;");
        System.out.println();
        
        System.out.println("Examples:");
        System.out.println("  import module java.base;");
        System.out.println("  import module java.sql;");
        System.out.println("  import module java.xml;");
        System.out.println();
        
        System.out.println("Benefits:");
        System.out.println("  ✓ Less boilerplate code");
        System.out.println("  ✓ Easier prototyping");
        System.out.println("  ✓ Simplified educational examples");
        System.out.println("  ✓ Better for scripts and small programs");
        System.out.println();
        
        // Demonstrate usage of various classes that would be imported
        demonstrateImportedClasses();
    }
    
    static void demonstrateImportedClasses() {
        System.out.println("Using classes from different packages:");
        
        // From java.util
        List<String> list = List.of("Apple", "Banana", "Cherry");
        System.out.println("List: " + list);
        
        // From java.time
        LocalDate today = LocalDate.now();
        System.out.println("Today: " + today);
        
        // From java.nio.file
        Path currentPath = Paths.get(".");
        System.out.println("Current path: " + currentPath.toAbsolutePath());
        
        System.out.println("\nWith module imports, all these classes would be");
        System.out.println("available without individual import statements!");
    }
}

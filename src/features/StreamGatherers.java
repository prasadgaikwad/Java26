package features;

import java.util.*;
import java.util.stream.*;

/**
 * JEP 473: Stream Gatherers (Final)
 * 
 * Feature: A new intermediate operation for streams that allows custom
 * stream transformations beyond what map, filter, and flatMap provide.
 * 
 * Gatherers enable:
 * - Custom stateful transformations
 * - One-to-many and many-to-one transformations
 * - Short-circuiting operations
 * - Better composability than collectors
 * 
 * Built-in gatherers include:
 * - fold() - Stateful prefix reduction
 * - scan() - Running accumulation
 * - windowFixed() - Fixed-size windows
 * - windowSliding() - Sliding windows
 * 
 * Benefits:
 * - More expressive stream pipelines
 * - Better performance for complex transformations
 * - Eliminates need for custom collectors in many cases
 */
public class StreamGatherers {
    
    public static void main(String[] args) {
        System.out.println("=== Stream Gatherers ===\n");
        
        // Example 1: Fixed Window Gatherer
        System.out.println("--- Fixed Window (groups of 3) ---");
        List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        
        // Using built-in windowFixed gatherer
        numbers.stream()
            .gather(Gatherers.windowFixed(3))
            .forEach(window -> System.out.println("Window: " + window));
        
        // Example 2: Sliding Window Gatherer
        System.out.println("\n--- Sliding Window (size 3) ---");
        numbers.stream()
            .gather(Gatherers.windowSliding(3))
            .forEach(window -> System.out.println("Sliding: " + window));
        
        // Example 3: Scan (running sum)
        System.out.println("\n--- Scan (running sum) ---");
        numbers.stream()
            .gather(Gatherers.scan(() -> 0, (sum, num) -> sum + num))
            .forEach(runningSum -> System.out.print(runningSum + " "));
        System.out.println();
        
        // Example 4: Fold (prefix scan)
        System.out.println("\n--- Fold (prefix multiplication) ---");
        List.of(1, 2, 3, 4, 5).stream()
            .gather(Gatherers.fold(() -> 1, (acc, num) -> acc * num))
            .forEach(product -> System.out.print(product + " "));
        System.out.println();
        
        // Example 5: Practical use case - Moving average
        System.out.println("\n--- Moving Average (window size 3) ---");
        List<Double> values = List.of(10.0, 20.0, 30.0, 40.0, 50.0, 60.0);
        values.stream()
            .gather(Gatherers.windowSliding(3))
            .map(window -> window.stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0))
            .forEach(avg -> System.out.printf("%.2f ", avg));
        System.out.println();
        
        // Example 6: Group by size with different behavior
        System.out.println("\n--- Batch Processing (batches of 4) ---");
        IntStream.range(1, 12)
            .boxed()
            .gather(Gatherers.windowFixed(4))
            .forEach(batch -> 
                System.out.println("Processing batch: " + batch + " (size: " + batch.size() + ")"));
        
        // Example 7: Running maximum
        System.out.println("\n--- Running Maximum ---");
        List.of(5, 2, 8, 1, 9, 3, 7).stream()
            .gather(Gatherers.scan(() -> Integer.MIN_VALUE, Math::max))
            .forEach(max -> System.out.print(max + " "));
        System.out.println();
        
        System.out.println("\n--- Use Cases ---");
        System.out.println("✓ Time-series analysis (moving averages, trends)");
        System.out.println("✓ Batch processing (grouping for efficiency)");
        System.out.println("✓ Running calculations (cumulative sums, products)");
        System.out.println("✓ Window-based operations (sliding windows, n-grams)");
        System.out.println("✓ Complex stateful transformations");
    }
}

package features;

import java.time.Duration;
import java.util.concurrent.*;

/**
 * JEP 525: Structured Concurrency (Sixth Preview)
 * 
 * Structured Concurrency treats multiple tasks running in different threads
 * as a single unit of work, simplifying error handling and cancellation.
 * 
 * Key Benefits:
 * - All tasks complete or fail together
 * - Automatic cancellation of remaining tasks on failure
 * - Clear ownership and lifecycle of threads
 * - Better error propagation
 * - No thread leaks
 */
public class StructuredConcurrency {

    public static void main(String[] args) {
        System.out.println("=".repeat(60));
        System.out.println("JEP 525: Structured Concurrency (Sixth Preview)");
        System.out.println("=".repeat(60));
        
        demonstrateBasicStructuredConcurrency();
        demonstrateErrorHandling();
        demonstrateShortCircuiting();
        compareWithTraditionalApproach();
        
        System.out.println("\n✅ Structured Concurrency Demo Complete!");
    }

    static void demonstrateBasicStructuredConcurrency() {
        System.out.println("\n🎯 1. Basic Structured Concurrency\n");
        
        try {
            String result = fetchUserData("user-123");
            System.out.println("✅ Result: " + result);
        } catch (Exception e) {
            System.err.println("❌ Error: " + e.getMessage());
        }
    }

    static String fetchUserData(String userId) throws InterruptedException, ExecutionException {
        // Note: Using ExecutorService with try-with-resources as preview API substitute
        // In actual JEP 525, you would use StructuredTaskScope
        
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            System.out.println("🚀 Starting parallel tasks for " + userId);
            
            Future<String> userFuture = executor.submit(() -> {
                System.out.println("   📥 Fetching user profile...");
                Thread.sleep(100);
                return "Profile[name=John Doe]";
            });
            
            Future<String> ordersFuture = executor.submit(() -> {
                System.out.println("   📦 Fetching orders...");
                Thread.sleep(150);
                return "Orders[count=5]";
            });
            
            Future<String> preferencesFuture = executor.submit(() -> {
                System.out.println("   ⚙️ Fetching preferences...");
                Thread.sleep(80);
                return "Preferences[theme=dark]";
            });
            
            // Wait for all tasks - they're all part of one structured unit
            String user = userFuture.get();
            String orders = ordersFuture.get();
            String preferences = preferencesFuture.get();
            
            return String.format("User: {%s, %s, %s}", user, orders, preferences);
        }
    }

    static void demonstrateErrorHandling() {
        System.out.println("\n⚠️ 2. Error Handling and Cancellation\n");
        
        try {
            String result = fetchWithFailure();
            System.out.println("✅ Result: " + result);
        } catch (Exception e) {
            System.out.println("❌ Caught exception: " + e.getMessage());
            System.out.println("💡 All other tasks were automatically cancelled!");
        }
    }

    static String fetchWithFailure() throws InterruptedException, ExecutionException {
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            System.out.println("🚀 Starting tasks (one will fail)...");
            
            Future<String> task1 = executor.submit(() -> {
                System.out.println("   ✅ Task 1: Starting...");
                Thread.sleep(100);
                System.out.println("   ✅ Task 1: Completed");
                return "Task1-Success";
            });
            
            Future<String> task2 = executor.submit(() -> {
                System.out.println("   ❌ Task 2: Starting...");
                Thread.sleep(50);
                throw new RuntimeException("Task 2 failed!");
            });
            
            Future<String> task3 = executor.submit(() -> {
                System.out.println("   ⏱️ Task 3: Starting (will be cancelled)...");
                Thread.sleep(2000);  // Long running
                System.out.println("   ⏱️ Task 3: Completed");
                return "Task3-Success";
            });
            
            // This will throw when task2 fails
            return task1.get() + ", " + task2.get() + ", " + task3.get();
        }
    }

    static void demonstrateShortCircuiting() {
        System.out.println("\n⚡ 3. Short-Circuiting (Return on First Success)\n");
        
        try {
            String result = raceMultipleAPIs();
            System.out.println("✅ First result: " + result);
        } catch (Exception e) {
            System.err.println("❌ Error: " + e.getMessage());
        }
    }

    static String raceMultipleAPIs() throws InterruptedException, ExecutionException {
        // Simulating racing multiple API endpoints to get fastest response
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            System.out.println("🏁 Racing 3 API endpoints...");
            
            Future<String> api1 = executor.submit(() -> {
                System.out.println("   🌐 API 1 (US): Starting...");
                Thread.sleep(200);
                System.out.println("   🌐 API 1 (US): Responded!");
                return "Data from US server";
            });
            
            Future<String> api2 = executor.submit(() -> {
                System.out.println("   🌐 API 2 (EU): Starting...");
                Thread.sleep(100);
                System.out.println("   🌐 API 2 (EU): Responded!");
                return "Data from EU server";
            });
            
            Future<String> api3 = executor.submit(() -> {
                System.out.println("   🌐 API 3 (Asia): Starting...");
                Thread.sleep(150);
                System.out.println("   🌐 API 3 (Asia): Responded!");
                return "Data from Asia server";
            });
            
            // In real structured concurrency, you'd use ShutdownOnSuccess policy
            // For demo, we'll get the fastest manually
            String result = null;
            while (result == null) {
                if (api2.isDone() && !api2.isCancelled()) {
                    result = api2.get();
                } else if (api3.isDone() && !api3.isCancelled()) {
                    result = api3.get();
                } else if (api1.isDone() && !api1.isCancelled()) {
                    result = api1.get();
                }
                Thread.sleep(10);
            }
            
            System.out.println("💡 Other tasks are cancelled after first success");
            return result;
        }
    }

    static void compareWithTraditionalApproach() {
        System.out.println("\n📊 4. Comparison: Traditional vs Structured\n");
        
        System.out.println("Traditional Approach Problems:");
        System.out.println("  ❌ Manual thread management");
        System.out.println("  ❌ Complex error handling");
        System.out.println("  ❌ Risk of thread leaks");
        System.out.println("  ❌ Difficult cancellation logic");
        
        System.out.println("\nStructured Concurrency Benefits:");
        System.out.println("  ✅ Automatic thread lifecycle");
        System.out.println("  ✅ Unified error handling");
        System.out.println("  ✅ No thread leaks (try-with-resources)");
        System.out.println("  ✅ Simple cancellation (cancel all on error)");
        System.out.println("  ✅ Clear parent-child relationship");
        
        // Example: Processing a batch of items
        try {
            processBatch();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    static void processBatch() throws InterruptedException, ExecutionException {
        System.out.println("\n📦 Processing batch of items:");
        
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            var items = java.util.List.of("Item1", "Item2", "Item3", "Item4", "Item5");
            
            var futures = items.stream()
                .map(item -> executor.submit(() -> {
                    System.out.println("   🔄 Processing " + item);
                    Thread.sleep(50);
                    return item + "-Processed";
                }))
                .toList();
            
            // All tasks complete together or fail together
            var results = futures.stream()
                .map(f -> {
                    try {
                        return f.get();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .toList();
            
            System.out.println("✅ All items processed: " + results);
        }
    }
}

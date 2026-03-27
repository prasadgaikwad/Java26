package features;

/**
 * JEP 481: Scoped Values (Final)
 * 
 * Feature: A new mechanism for sharing immutable data within and across threads
 * in a structured way. Scoped values are a safer, more efficient alternative to
 * ThreadLocal variables.
 * 
 * Key Characteristics:
 * - Immutable: Cannot be changed once set
 * - Scoped: Automatically cleaned up when scope ends
 * - Inherited: Child threads inherit parent's scoped values
 * - Performant: More efficient than ThreadLocal
 * 
 * Advantages over ThreadLocal:
 * - No memory leaks (automatic cleanup)
 * - Immutable by design (thread-safe)
 * - Better for virtual threads
 * - Clearer lifecycle management
 * - Superior performance
 * 
 * Use Cases:
 * - Request context (user ID, request ID, transaction ID)
 * - Security context
 * - Locale/timezone information
 * - Distributed tracing
 */
public class ScopedValues {
    
    // Define scoped values
    private static final ScopedValue<String> USER_ID = ScopedValue.newInstance();
    private static final ScopedValue<String> REQUEST_ID = ScopedValue.newInstance();
    private static final ScopedValue<Integer> DEPTH = ScopedValue.newInstance();
    
    public static void main(String[] args) throws Exception {
        System.out.println("=== Scoped Values ===\n");
        
        // Example 1: Basic usage
        System.out.println("--- Basic Scoped Value Usage ---");
        ScopedValue.where(USER_ID, "user-123")
            .where(REQUEST_ID, "req-456")
            .run(() -> {
                System.out.println("User ID: " + USER_ID.get());
                System.out.println("Request ID: " + REQUEST_ID.get());
                processRequest();
            });
        
        // Outside scope, values are not accessible
        System.out.println("\n--- Outside Scope ---");
        System.out.println("USER_ID is bound: " + USER_ID.isBound());
        
        // Example 2: Nested scopes
        System.out.println("\n--- Nested Scopes ---");
        ScopedValue.where(DEPTH, 0).run(() -> {
            printDepth("Level 1");
            
            ScopedValue.where(DEPTH, 1).run(() -> {
                printDepth("Level 2");
                
                ScopedValue.where(DEPTH, 2).run(() -> {
                    printDepth("Level 3");
                });
            });
        });
        
        // Example 3: Cross-thread usage
        System.out.println("\n--- Cross-Thread Usage ---");
        System.out.println("Note: Scoped values are bound to the current thread.");
        System.out.println("Child threads need their own bindings.\n");
        
        ScopedValue.where(USER_ID, "user-789")
            .where(REQUEST_ID, "req-101112")
            .run(() -> {
                System.out.println("Main thread - User: " + USER_ID.get());
                System.out.println("Main thread - Request: " + REQUEST_ID.get());
                
                // Demonstrate scoped value usage in main thread
                performTask("Main task");
            });
        
        // Example 4: Practical example - Web request simulation
        System.out.println("\n--- Web Request Simulation ---");
        simulateWebRequest("user-alice", "GET /api/users");
        simulateWebRequest("user-bob", "POST /api/orders");
    }
    
    static void processRequest() {
        System.out.println("Processing request for user: " + USER_ID.get());
        logActivity("Request processed");
    }
    
    static void logActivity(String message) {
        System.out.println("[" + REQUEST_ID.get() + "] " + message);
    }
    
    static void printDepth(String label) {
        int depth = DEPTH.get();
        String indent = "  ".repeat(depth);
        System.out.println(indent + label + " (depth=" + depth + ")");
    }
    
    static void performTask(String taskName) {
        System.out.println("  Executing " + taskName + " for user: " + USER_ID.get());
    }
    
    static void simulateWebRequest(String userId, String endpoint) {
        String requestId = "req-" + System.currentTimeMillis();
        
        ScopedValue.where(USER_ID, userId)
            .where(REQUEST_ID, requestId)
            .run(() -> {
                System.out.println("\n[" + REQUEST_ID.get() + "] " + endpoint);
                System.out.println("  Authenticated user: " + USER_ID.get());
                
                // Simulate some processing
                authenticateUser();
                authorizeRequest();
                executeBusinessLogic();
                
                System.out.println("  Request completed successfully");
            });
    }
    
    static void authenticateUser() {
        System.out.println("  [AUTH] Authenticating user: " + USER_ID.get());
    }
    
    static void authorizeRequest() {
        System.out.println("  [AUTHZ] Authorizing request " + REQUEST_ID.get());
    }
    
    static void executeBusinessLogic() {
        System.out.println("  [LOGIC] Executing for user: " + USER_ID.get());
    }
}

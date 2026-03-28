package features;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * JEP 517: HTTP/3 for the HTTP Client API
 * 
 * Adds HTTP/3 support to java.net.http.HttpClient, enabling the use of
 * QUIC protocol for faster, more reliable connections.
 * 
 * Key Benefits:
 * - Faster connection establishment (0-RTT)
 * - Better performance on lossy networks
 * - Connection migration (survives IP address changes)
 * - Improved multiplexing (no head-of-line blocking)
 * - Built on UDP instead of TCP
 */
public class Http3Client {

    public static void main(String[] args) {
        System.out.println("=".repeat(60));
        System.out.println("JEP 517: HTTP/3 for the HTTP Client API");
        System.out.println("=".repeat(60));
        
        demonstrateBasicHttp3();
        demonstrateVersionNegotiation();
        compareHttpVersions();
        demonstrateConnectionMigration();
        
        System.out.println("\n✅ HTTP/3 Client Demo Complete!");
    }

    static void demonstrateBasicHttp3() {
        System.out.println("\n🌐 1. Basic HTTP/3 Request\n");
        
        // Note: HTTP/3 support requires the server to support it
        // For demonstration, we'll show the API usage
        
        try {
            HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_3)  // Prefer HTTP/3
                .connectTimeout(Duration.ofSeconds(10))
                .build();
            
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://www.google.com"))
                .GET()
                .build();
            
            System.out.println("📤 Sending HTTP/3 request to " + request.uri());
            System.out.println("💡 Client will use HTTP/3 if server supports it");
            
            HttpResponse<String> response = client.send(
                request, 
                HttpResponse.BodyHandlers.ofString()
            );
            
            System.out.println("\n✅ Response received:");
            System.out.println("   Protocol: " + response.version());
            System.out.println("   Status: " + response.statusCode());
            System.out.println("   Headers: " + response.headers().map().size() + " headers");
            System.out.println("   Body length: " + response.body().length() + " characters");
            
        } catch (Exception e) {
            System.err.println("❌ Error: " + e.getMessage());
            System.out.println("💡 Note: HTTP/3 requires server support and may fallback to HTTP/2");
        }
    }

    static void demonstrateVersionNegotiation() {
        System.out.println("\n🔄 2. HTTP Version Negotiation\n");
        
        System.out.println("HTTP/3 uses ALPN (Application-Layer Protocol Negotiation):");
        System.out.println("  1. Client advertises HTTP/3 support");
        System.out.println("  2. Server responds with supported version");
        System.out.println("  3. Falls back to HTTP/2 or HTTP/1.1 if needed\n");
        
        // Test different version preferences
        var versions = new HttpClient.Version[]{
            HttpClient.Version.HTTP_3,
            HttpClient.Version.HTTP_2,
            HttpClient.Version.HTTP_1_1
        };
        
        for (var version : versions) {
            try {
                HttpClient client = HttpClient.newBuilder()
                    .version(version)
                    .connectTimeout(Duration.ofSeconds(5))
                    .build();
                
                HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://www.example.com"))
                    .GET()
                    .build();
                
                System.out.println("🔍 Trying with preference: " + version);
                HttpResponse<Void> response = client.send(
                    request, 
                    HttpResponse.BodyHandlers.discarding()
                );
                
                System.out.println("   ✅ Connected using: " + response.version());
                
            } catch (Exception e) {
                System.out.println("   ❌ Failed: " + e.getMessage());
            }
        }
    }

    static void compareHttpVersions() {
        System.out.println("\n📊 3. HTTP Version Comparison\n");
        
        System.out.println("┌─────────────┬──────────┬──────────┬──────────┐");
        System.out.println("│ Feature     │ HTTP/1.1 │ HTTP/2   │ HTTP/3   │");
        System.out.println("├─────────────┼──────────┼──────────┼──────────┤");
        System.out.println("│ Transport   │ TCP      │ TCP      │ UDP/QUIC │");
        System.out.println("│ Multiplexing│ No       │ Yes      │ Yes      │");
        System.out.println("│ HOL Blocking│ Yes      │ Partial  │ No       │");
        System.out.println("│ 0-RTT       │ No       │ No       │ Yes      │");
        System.out.println("│ Migration   │ No       │ No       │ Yes      │");
        System.out.println("│ Encryption  │ Optional │ Required │ Required │");
        System.out.println("└─────────────┴──────────┴──────────┴──────────┘");
        
        System.out.println("\nKey HTTP/3 Advantages:");
        System.out.println("  🚀 Faster connection establishment");
        System.out.println("  📱 Better for mobile (connection migration)");
        System.out.println("  🌐 No head-of-line blocking");
        System.out.println("  🔒 Always encrypted (TLS 1.3)");
        System.out.println("  💨 Better on lossy networks");
    }

    static void demonstrateConnectionMigration() {
        System.out.println("\n🔄 4. Connection Migration Concept\n");
        
        System.out.println("HTTP/3 Connection Migration:");
        System.out.println("  📱 Mobile switches from WiFi to cellular");
        System.out.println("  🔄 IP address changes");
        System.out.println("  ✅ HTTP/3 connection continues seamlessly");
        System.out.println("  ❌ HTTP/2 would require new connection\n");
        
        System.out.println("Use Case Examples:");
        System.out.println("  • Mobile apps with downloads");
        System.out.println("  • Video streaming while moving");
        System.out.println("  • Long-running API connections");
        System.out.println("  • Real-time communication apps");
        
        // Example configuration for production use
        System.out.println("\n💼 Production Configuration Example:");
        System.out.println("```java");
        System.out.println("HttpClient client = HttpClient.newBuilder()");
        System.out.println("    .version(HttpClient.Version.HTTP_3)");
        System.out.println("    .connectTimeout(Duration.ofSeconds(10))");
        System.out.println("    .followRedirects(HttpClient.Redirect.NORMAL)");
        System.out.println("    .build();");
        System.out.println("```");
    }

    static void demonstrateAsyncRequests() {
        System.out.println("\n⚡ 5. Async HTTP/3 Requests\n");
        
        try {
            HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_3)
                .build();
            
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.github.com"))
                .GET()
                .build();
            
            System.out.println("📤 Sending async HTTP/3 request...");
            
            client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> {
                    System.out.println("✅ Async response received");
                    System.out.println("   Version: " + response.version());
                    System.out.println("   Status: " + response.statusCode());
                    return response;
                })
                .thenAccept(response -> {
                    System.out.println("   Body preview: " + 
                        response.body().substring(0, Math.min(100, response.body().length())) + "...");
                })
                .exceptionally(e -> {
                    System.err.println("❌ Error: " + e.getMessage());
                    return null;
                })
                .join(); // Wait for completion in demo
                
        } catch (Exception e) {
            System.err.println("❌ Error: " + e.getMessage());
        }
    }
}

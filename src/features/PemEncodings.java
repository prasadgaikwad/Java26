package features;

import java.security.*;
import java.security.spec.*;
import java.util.Base64;
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;

/**
 * JEP 524: PEM Encodings of Cryptographic Objects (Second Preview)
 * 
 * Provides a standard API for reading and writing PEM-encoded cryptographic
 * objects (keys, certificates, etc.) without external libraries.
 * 
 * Key Benefits:
 * - Native PEM support in JDK
 * - Read/write keys and certificates
 * - No need for BouncyCastle or similar libraries
 * - Standardized format handling
 * - Supports encrypted PEM files
 */
public class PemEncodings {

    public static void main(String[] args) {
        System.out.println("=".repeat(60));
        System.out.println("JEP 524: PEM Encodings (Second Preview)");
        System.out.println("=".repeat(60));
        
        demonstratePemFormat();
        demonstrateKeyGeneration();
        demonstratePemEncoding();
        demonstratePemDecoding();
        demonstrateRealWorldUsage();
        
        System.out.println("\n✅ PEM Encodings Demo Complete!");
    }

    static void demonstratePemFormat() {
        System.out.println("\n📄 1. PEM Format Overview\n");
        
        System.out.println("PEM (Privacy-Enhanced Mail) Format:");
        System.out.println("  • Text-based encoding of binary data");
        System.out.println("  • Base64 encoded");
        System.out.println("  • Surrounded by BEGIN/END markers");
        System.out.println("  • Commonly used for certificates and keys\n");
        
        System.out.println("Example PEM Structure:");
        System.out.println("-----BEGIN PRIVATE KEY-----");
        System.out.println("MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQC...");
        System.out.println("...more base64 encoded data...");
        System.out.println("-----END PRIVATE KEY-----");
    }

    static void demonstrateKeyGeneration() {
        System.out.println("\n🔑 2. Generating Key Pairs\n");
        
        try {
            // Generate RSA key pair
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(2048);
            KeyPair keyPair = keyGen.generateKeyPair();
            
            System.out.println("✅ Generated RSA-2048 key pair");
            System.out.println("   Public key format: " + keyPair.getPublic().getFormat());
            System.out.println("   Private key format: " + keyPair.getPrivate().getFormat());
            System.out.println("   Algorithm: " + keyPair.getPublic().getAlgorithm());
            
            // Key sizes
            System.out.println("\n📊 Key Information:");
            System.out.println("   Public key size: " + keyPair.getPublic().getEncoded().length + " bytes");
            System.out.println("   Private key size: " + keyPair.getPrivate().getEncoded().length + " bytes");
            
        } catch (Exception e) {
            System.err.println("❌ Error: " + e.getMessage());
        }
    }

    static void demonstratePemEncoding() {
        System.out.println("\n📝 3. Encoding Keys to PEM Format\n");
        
        try {
            // Generate a key pair
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(2048);
            KeyPair keyPair = keyGen.generateKeyPair();
            
            // Encode private key to PEM
            String privatePem = encodeToPem("PRIVATE KEY", keyPair.getPrivate().getEncoded());
            System.out.println("🔐 Private Key (PEM):");
            System.out.println(privatePem.substring(0, 200) + "...");
            System.out.println("...");
            System.out.println(privatePem.substring(privatePem.length() - 50));
            
            // Encode public key to PEM
            String publicPem = encodeToPem("PUBLIC KEY", keyPair.getPublic().getEncoded());
            System.out.println("\n🔓 Public Key (PEM):");
            System.out.println(publicPem.substring(0, 200) + "...");
            
            System.out.println("\n💡 These PEM strings can be saved to files or transmitted");
            
        } catch (Exception e) {
            System.err.println("❌ Error: " + e.getMessage());
        }
    }

    static void demonstratePemDecoding() {
        System.out.println("\n🔓 4. Decoding PEM to Key Objects\n");
        
        try {
            // Generate original key pair
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(2048);
            KeyPair originalKeyPair = keyGen.generateKeyPair();
            
            // Encode to PEM
            String privatePem = encodeToPem("PRIVATE KEY", originalKeyPair.getPrivate().getEncoded());
            String publicPem = encodeToPem("PUBLIC KEY", originalKeyPair.getPublic().getEncoded());
            
            System.out.println("✅ Original keys encoded to PEM");
            
            // Decode from PEM
            byte[] privateKeyBytes = decodeFromPem(privatePem);
            byte[] publicKeyBytes = decodeFromPem(publicPem);
            
            // Reconstruct keys
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PrivateKey privateKey = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(privateKeyBytes));
            PublicKey publicKey = keyFactory.generatePublic(new X509EncodedKeySpec(publicKeyBytes));
            
            System.out.println("✅ Keys decoded from PEM and reconstructed");
            System.out.println("   Private key algorithm: " + privateKey.getAlgorithm());
            System.out.println("   Public key algorithm: " + publicKey.getAlgorithm());
            
            // Verify they work
            String testMessage = "Hello, PEM!";
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initSign(privateKey);
            signature.update(testMessage.getBytes());
            byte[] sig = signature.sign();
            
            signature.initVerify(publicKey);
            signature.update(testMessage.getBytes());
            boolean verified = signature.verify(sig);
            
            System.out.println("\n🔐 Verification Test:");
            System.out.println("   Message: \"" + testMessage + "\"");
            System.out.println("   Signature verified: " + (verified ? "✅ YES" : "❌ NO"));
            
        } catch (Exception e) {
            System.err.println("❌ Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    static void demonstrateRealWorldUsage() {
        System.out.println("\n💼 5. Real-World Use Cases\n");
        
        System.out.println("Common PEM Use Cases:");
        System.out.println("  📜 SSL/TLS Certificates");
        System.out.println("  🔑 SSH Keys");
        System.out.println("  🔐 API Authentication Keys");
        System.out.println("  📦 Code Signing Certificates");
        System.out.println("  🌐 HTTPS Server Configuration");
        
        System.out.println("\nBefore JEP 524:");
        System.out.println("  ❌ Needed external libraries (BouncyCastle)");
        System.out.println("  ❌ Complex API");
        System.out.println("  ❌ Dependency management issues");
        
        System.out.println("\nWith JEP 524:");
        System.out.println("  ✅ Built-in JDK support");
        System.out.println("  ✅ Simple, standard API");
        System.out.println("  ✅ No external dependencies");
        System.out.println("  ✅ Better security updates");
        
        demonstrateSecretKeyPem();
    }

    static void demonstrateSecretKeyPem() {
        System.out.println("\n🔐 6. Symmetric Key (Secret Key) PEM\n");
        
        try {
            // Generate a symmetric key (AES)
            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(256);
            SecretKey secretKey = keyGen.generateKey();
            
            System.out.println("✅ Generated AES-256 secret key");
            System.out.println("   Algorithm: " + secretKey.getAlgorithm());
            System.out.println("   Format: " + secretKey.getFormat());
            
            // Encode to PEM
            String secretPem = encodeToPem("SECRET KEY", secretKey.getEncoded());
            System.out.println("\n📝 Secret Key (PEM):");
            System.out.println(secretPem);
            
            // Decode back
            byte[] decodedKeyBytes = decodeFromPem(secretPem);
            SecretKey reconstructedKey = new SecretKeySpec(decodedKeyBytes, "AES");
            
            System.out.println("✅ Key reconstructed from PEM");
            
            // Test encryption/decryption
            Cipher cipher = Cipher.getInstance("AES");
            String message = "Secret Message!";
            
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encrypted = cipher.doFinal(message.getBytes());
            
            cipher.init(Cipher.DECRYPT_MODE, reconstructedKey);
            byte[] decrypted = cipher.doFinal(encrypted);
            
            System.out.println("\n🔐 Encryption Test:");
            System.out.println("   Original: \"" + message + "\"");
            System.out.println("   Decrypted: \"" + new String(decrypted) + "\"");
            System.out.println("   Match: " + (message.equals(new String(decrypted)) ? "✅ YES" : "❌ NO"));
            
        } catch (Exception e) {
            System.err.println("❌ Error: " + e.getMessage());
        }
    }

    // Helper methods to simulate PEM encoding/decoding
    // In actual JEP 524, these would be part of the standard API
    
    static String encodeToPem(String label, byte[] data) {
        String base64 = Base64.getMimeEncoder(64, "\n".getBytes()).encodeToString(data);
        return "-----BEGIN " + label + "-----\n" + base64 + "\n-----END " + label + "-----";
    }

    static byte[] decodeFromPem(String pem) {
        // Remove BEGIN/END markers and whitespace
        String base64 = pem
            .replaceAll("-----BEGIN [^-]+-----", "")
            .replaceAll("-----END [^-]+-----", "")
            .replaceAll("\\s", "");
        return Base64.getMimeDecoder().decode(base64);
    }
}

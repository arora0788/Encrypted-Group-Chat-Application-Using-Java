import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

import javax.crypto.Cipher;

public class ED
{
    //static String plainText = "Plain text which need to be encrypted by Java RSA Encryption in ECB Mode";
    static PublicKey publicKey;
    static PrivateKey privateKey;
    static KeyPairGenerator keyPairGenerator;
    static KeyPair keyPair;
    
    /*public static void main(String[] args) throws Exception
    {
        
        
        //System.out.println("Original Text  : "+plainText);
        
        // Encryption
        //byte[] cipherTextArray = encrypt(plainText);
        //String encryptedText = Base64.getEncoder().encodeToString(cipherTextArray);
        //System.out.println("Encrypted Text : "+encryptedText);
        
        // Decryption
        //String decryptedText = decrypt(cipherTextArray);
        //System.out.println("DeCrypted Text : "+decryptedText);
    }*/
    
    private static void initialize() throws Exception
    {
    	// Get an instance of the RSA key generator
        keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(4096);
        
        // Generate the KeyPair
        keyPair = keyPairGenerator.generateKeyPair();
        
        // Get the public and private key
        publicKey = keyPair.getPublic();
        privateKey = keyPair.getPrivate();
    }
    
    public static byte[] encrypt(String plainText) throws Exception
    {
    	initialize();
    	
        //Get Cipher Instance RSA With ECB Mode and OAEPWITHSHA-512ANDMGF1PADDING Padding
        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWITHSHA-512ANDMGF1PADDING");
        
        //Initialize Cipher for ENCRYPT_MODE
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        
        //Perform Encryption
        byte[] cipherText = cipher.doFinal(plainText.getBytes()) ;
        
        //String encryptedText = Base64.getEncoder().encodeToString(cipherText);
        //System.out.println("Encrypted Text : "+encryptedText);

        return cipherText;
    }
    
    public static String decrypt(byte[] cipherTextArray) throws Exception
    {
    	initialize();
    	
        //Get Cipher Instance RSA With ECB Mode and OAEPWITHSHA-512ANDMGF1PADDING Padding
        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWITHSHA-512ANDMGF1PADDING");
        
        //Initialize Cipher for DECRYPT_MODE
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        
        //Perform Decryption
        byte[] decryptedTextArray = cipher.doFinal(cipherTextArray);
        
        return new String(decryptedTextArray);
    }
}
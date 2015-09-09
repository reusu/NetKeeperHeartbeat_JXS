package name.yumao.netkeeper.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class AESUtils {
	public static byte[] encrypt(byte[] content,String password) throws Exception{
        SecretKeySpec key = new SecretKeySpec(password.getBytes(), "AES");  
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");  
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] result = cipher.doFinal(content);  
        return result;
	}
	public static byte[] decrypt(byte[] content,String password) throws Exception{
        SecretKeySpec key = new SecretKeySpec(password.getBytes(), "AES");  
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");  
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] result = cipher.doFinal(content);  
        return result;
	}
}

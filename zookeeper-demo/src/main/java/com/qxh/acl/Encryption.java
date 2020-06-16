package com.qxh.acl;




import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.apache.commons.codec.binary.Base64;

/**
 * 生成 user:pass 的签名 sha1->base64
 */
public class Encryption {

    public static void main(String[] args) throws NoSuchAlgorithmException {
        String userPass = "qxh:123456789";
        byte[] digest = MessageDigest.getInstance("SHA1").digest(
                userPass.getBytes()
        );
        Base64 base64 = new Base64();
        String encodeToString = base64.encodeToString(digest);
        System.out.println(encodeToString);
    }

}

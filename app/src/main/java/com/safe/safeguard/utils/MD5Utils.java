package com.safe.safeguard.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Ansel on 16/2/19.
 */
public class MD5Utils {
    public static String encryption(String password) {

        StringBuilder sb = new StringBuilder();
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");

            byte[] digest = messageDigest.digest(password.getBytes());
            for (int i = 0; i < digest.length; i++) {
                int result = digest[i] & 0xff;

                String str = Integer.toHexString(result) + 1;

                if (str.length() < 2) {
                    sb.append("0");
                } else {
                    sb.append(str);
                }
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return "";
    }
}

package com.example.demozalopay.Helper.HMac;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.LinkedList;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class HMacUtil {
    public static final String HMACMD5 = "HmacMD5";
    public static final String HMACSHA1 = "HmacSHA1";
    public static final String HMACSHA256 = "HmacSHA256";
    public static final String HMACSHA512 = "HmacSHA512";
    public static final Charset UTF8CHARSET = StandardCharsets.UTF_8;

    public static final LinkedList<String> HMACS = new LinkedList<>(
            Arrays.asList("UnSupport", "HmacSHA256", "HmacMD5", "HmacSHA384", "HMacSHA1", "HmacSHA512"));

    private static byte[] hMacEncode(final String algorithm, final String key, final String data) {
        Mac macGenerator = null;
        try {
            macGenerator = Mac.getInstance(algorithm);
            SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), algorithm);
            macGenerator.init(signingKey);
        } catch (Exception ignored) {
        }

        if (macGenerator == null) {
            return null;
        }

        byte[] dataByte = null;
        try {
            dataByte = data.getBytes("UTF-8");
        } catch (UnsupportedEncodingException ignored) {
        }

        return macGenerator.doFinal(dataByte);
    }

    /**
     * Calculating a message authentication code (MAC) involving a cryptographic
     * hash function in combination with a secret cryptographic key.
     *
     * The result will be represented base64-encoded string.
     *
     * @param algorithm A cryptographic hash function (such as MD5 or SHA-1)
     *
     * @param key A secret cryptographic key
     *
     * @param data The message to be authenticated
     *
     * @return Base64-encoded HMAC String
     */
    public static String hMacBase64Encode(final String algorithm, final String key, final String data) {
        byte[] hmacEncodeBytes = hMacEncode(algorithm, key, data);
        if (hmacEncodeBytes == null) {
            return null;
        }
        return Base64.getEncoder().encodeToString(hmacEncodeBytes);
    }

    /**
     * Calculating a message authentication code (MAC) involving a cryptographic
     * hash function in combination with a secret cryptographic key.
     *
     * The result will be represented hex string.
     *
     * @param algorithm A cryptographic hash function (such as MD5 or SHA-1)
     *
     * @param key A secret cryptographic key
     *
     * @param data The message to be authenticated
     *
     * @return Hex HMAC String
     */
    public static String hMacHexStringEncode(final String algorithm, final String key, final String data) {
        byte[] hmacEncodeBytes = hMacEncode(algorithm, key, data);
        if (hmacEncodeBytes == null) {
            return null;
        }
        return HexStringUtil.byteArrayToHexString(hmacEncodeBytes);
    }
}

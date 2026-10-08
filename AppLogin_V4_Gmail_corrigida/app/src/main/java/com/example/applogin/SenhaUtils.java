package com.example.applogin;

import android.os.Build;
import android.util.Base64;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public final class SenhaUtils {
    private static final int ITERACOES = 120000;
    private static final int TAMANHO_SALT = 16;
    private static final int TAMANHO_CHAVE = 256;

    private SenhaUtils() {}

    public static String gerarHash(String senha) throws Exception {
        byte[] salt = new byte[TAMANHO_SALT];
        new SecureRandom().nextBytes(salt);
        byte[] hash = gerarPBKDF2(senha.toCharArray(), salt);
        return Base64.encodeToString(salt, Base64.NO_WRAP) + ":"
                + Base64.encodeToString(hash, Base64.NO_WRAP);
    }

    public static boolean verificarSenha(String senha, String senhaArmazenada) {
        try {
            String[] partes = senhaArmazenada.split(":");
            if (partes.length != 2) return false;
            byte[] salt = Base64.decode(partes[0], Base64.DEFAULT);
            byte[] esperado = Base64.decode(partes[1], Base64.DEFAULT);
            byte[] atual = gerarPBKDF2(senha.toCharArray(), salt);
            return MessageDigest.isEqual(esperado, atual);
        } catch (Exception e) {
            return false;
        }
    }

    private static byte[] gerarPBKDF2(char[] senha, byte[] salt) throws Exception {
        String algoritmo = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
                ? "PBKDF2WithHmacSHA256" : "PBKDF2WithHmacSHA1";
        KeySpec spec = new PBEKeySpec(senha, salt, ITERACOES, TAMANHO_CHAVE);
        return SecretKeyFactory.getInstance(algoritmo).generateSecret(spec).getEncoded();
    }
}

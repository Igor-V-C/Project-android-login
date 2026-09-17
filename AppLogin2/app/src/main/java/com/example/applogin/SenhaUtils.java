package com.example.applogin;

import android.os.Build;
import android.util.Base64;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.security.spec.KeySpec;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class SenhaUtils {

    private static final int ITERACOES = 120000;
    private static final int TAMANHO_SALT = 16;
    private static final int TAMANHO_CHAVE = 256;

    public static String gerarHash(String senha) throws Exception {

        byte[] salt = new byte[TAMANHO_SALT];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(salt);

        byte[] hash = gerarPBKDF2(senha.toCharArray(), salt);

        String saltBase64 = Base64.encodeToString(salt, Base64.NO_WRAP);
        String hashBase64 = Base64.encodeToString(hash, Base64.NO_WRAP);

        return saltBase64 + ":" + hashBase64;
    }

    public static boolean verificarSenha(String senha, String senhaArmazenada) {

        try {

            String[] partes = senhaArmazenada.split(":");

            if (partes.length != 2) {
                return false;
            }

            byte[] salt = Base64.decode(partes[0], Base64.DEFAULT);
            byte[] hashEsperado = Base64.decode(partes[1], Base64.DEFAULT);

            byte[] hashAtual = gerarPBKDF2(
                    senha.toCharArray(),
                    salt
            );

            return MessageDigest.isEqual(hashEsperado, hashAtual);

        } catch (Exception e) {
            return false;
        }
    }

    private static byte[] gerarPBKDF2(char[] senha, byte[] salt)
            throws Exception {

        String algoritmo;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            algoritmo = "PBKDF2WithHmacSHA256";
        } else {
            algoritmo = "PBKDF2WithHmacSHA1";
        }

        KeySpec spec = new PBEKeySpec(
                senha,
                salt,
                ITERACOES,
                TAMANHO_CHAVE
        );

        SecretKeyFactory factory =
                SecretKeyFactory.getInstance(algoritmo);

        return factory.generateSecret(spec).getEncoded();
    }
}

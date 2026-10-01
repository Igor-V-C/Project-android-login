package com.example.applogin;

import android.os.Handler;
import android.os.Looper;

import java.security.SecureRandom;

/** Estado temporário do código aleatório da V4. */
public final class RecuperacaoCodigo {

    private static final long VALIDADE_MS = 40_000L;
    private static final Handler HANDLER = new Handler(Looper.getMainLooper());

    private static String codigoAtual;
    private static long usuarioId = -1;
    private static long expiraEm = 0L;
    private static Runnable expiracao;

    private RecuperacaoCodigo() {
    }

    public static String gerarCodigo() {
        return String.format("%06d", new SecureRandom().nextInt(1_000_000));
    }

    public static synchronized void ativarCodigo(String codigo, long id) {
        invalidarSilenciosamente();
        codigoAtual = codigo;
        usuarioId = id;
        expiraEm = System.currentTimeMillis() + VALIDADE_MS;
        expiracao = RecuperacaoCodigo::invalidar;
        HANDLER.postDelayed(expiracao, VALIDADE_MS);
    }

    public static synchronized boolean verificar(String codigoDigitado) {
        if (codigoAtual == null || System.currentTimeMillis() >= expiraEm) {
            invalidarSilenciosamente();
            return false;
        }
        return codigoAtual.equals(codigoDigitado);
    }

    public static synchronized long getUsuarioId() {
        return usuarioId;
    }

    public static synchronized void invalidar() {
        invalidarSilenciosamente();
    }

    private static void invalidarSilenciosamente() {
        if (expiracao != null) {
            HANDLER.removeCallbacks(expiracao);
        }
        expiracao = null;
        codigoAtual = null;
        usuarioId = -1;
        expiraEm = 0L;
    }
}

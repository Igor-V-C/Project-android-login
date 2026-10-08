package com.example.applogin;

import android.util.Log;

import org.apache.commons.mail2.jakarta.SimpleEmail;

public final class EmailSender {

    private static final String TAG = "EmailSender";

    private EmailSender() {
    }

    public static void enviarCodigo(
            String destino,
            String codigo
    ) throws Exception {

        if (!EmailConfig.isConfigured()) {
            throw new IllegalStateException(
                    "EmailConfig não está configurado."
            );
        }

        if (destino == null || destino.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Destinatário inválido."
            );
        }

        if (codigo == null || !codigo.matches("\\d{6}")) {
            throw new IllegalArgumentException(
                    "O código deve ter exatamente 6 dígitos."
            );
        }

        try {

            SimpleEmail email = new SimpleEmail();

            email.setHostName(
                    EmailConfig.SMTP_HOST
            );

            email.setSmtpPort(
                    EmailConfig.SMTP_PORT
            );

            email.setAuthentication(
                    EmailConfig.SMTP_USERNAME,
                    EmailConfig.SMTP_PASSWORD
            );

            email.setStartTLSEnabled(true);
            email.setStartTLSRequired(true);

            email.setFrom(
                    EmailConfig.FROM_ADDRESS,
                    EmailConfig.FROM_NAME
            );

            email.addTo(destino);

            email.setSubject(
                    "Código de recuperação - AppLogin"
            );

            email.setMsg(
                    "Seu código de recuperação é: "
                            + codigo
            );

            email.send();

        } catch (Exception e) {

            Log.e(
                    TAG,
                    "Erro ao enviar e-mail",
                    e
            );

            throw e;
        }
    }
}
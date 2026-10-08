package com.example.applogin;

<<<<<<< HEAD
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
=======
import org.apache.commons.mail2.jakarta.SimpleEmail;

/** Envia o código de recuperação usando Gmail SMTP. */
public final class EmailSender {

    private EmailSender() {
    }

    public static void enviarCodigo(String destino, String codigo) throws Exception {
        if (!EmailConfig.isConfigured()) {
            throw new IllegalStateException(
                    "Preencha o EmailConfig.java com o Gmail e a Senha de app."
>>>>>>> 5608e2a81b07f6193180be7504e29eb7f9641ca4
            );
        }

        if (destino == null || destino.trim().isEmpty()) {
<<<<<<< HEAD
            throw new IllegalArgumentException(
                    "Destinatário inválido."
            );
=======
            throw new IllegalArgumentException("Destinatário inválido.");
>>>>>>> 5608e2a81b07f6193180be7504e29eb7f9641ca4
        }

        if (codigo == null || !codigo.matches("\\d{6}")) {
            throw new IllegalArgumentException(
                    "O código deve ter exatamente 6 dígitos."
            );
        }

<<<<<<< HEAD
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
=======
        SimpleEmail email = new SimpleEmail();
        email.setHostName(EmailConfig.SMTP_HOST);
        email.setSmtpPort(EmailConfig.SMTP_PORT);
        email.setAuthentication(
                EmailConfig.SMTP_USERNAME,
                EmailConfig.SMTP_PASSWORD
        );
        email.setStartTLSEnabled(true);
        email.setStartTLSRequired(true);
        email.setCharset("UTF-8");
        email.setFrom(
                EmailConfig.FROM_ADDRESS,
                EmailConfig.FROM_NAME
        );
        email.addTo(destino.trim());
        email.setSubject("Código de recuperação - AppLogin");
        email.setMsg(
                "Seu código de recuperação é: " + codigo
                        + "\n\n"
                        + "Esse código é válido por 40 segundos.\n"
                        + "Não compartilhe este código."
        );
        email.send();
    }
}
>>>>>>> 5608e2a81b07f6193180be7504e29eb7f9641ca4

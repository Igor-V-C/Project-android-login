package com.example.applogin;

import org.apache.commons.mail2.jakarta.SimpleEmail;

/** Envia o código de recuperação usando Gmail SMTP. */
public final class EmailSender {

    private EmailSender() {
    }

    public static void enviarCodigo(String destino, String codigo) throws Exception {
        if (!EmailConfig.isConfigured()) {
            throw new IllegalStateException(
                    "Preencha o EmailConfig.java com o Gmail e a Senha de app."
            );
        }

        if (destino == null || destino.trim().isEmpty()) {
            throw new IllegalArgumentException("Destinatário inválido.");
        }

        if (codigo == null || !codigo.matches("\\d{6}")) {
            throw new IllegalArgumentException(
                    "O código deve ter exatamente 6 dígitos."
            );
        }

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

package com.example.applogin;

/**
 * Configuração do Gmail usada pelo Apache Commons Email.
 *
 * SMTP_USERNAME: endereço Gmail que enviará os códigos.
 * SMTP_PASSWORD: Senha de app do Google, e não a senha normal da conta.
 */
public final class EmailConfig {

    private EmailConfig() {
    }

    public static final String SMTP_HOST = "smtp.gmail.com";
    public static final int SMTP_PORT = 587;

    // Preencha com a conta Gmail remetente.
    public static final String SMTP_USERNAME = "SEU_EMAIL@gmail.com";

    // Preencha com a Senha de app de 16 caracteres do Google.
    public static final String SMTP_PASSWORD = "SUA_SENHA_DE_APP";

    public static final String FROM_NAME = "AppLogin";
    public static final String FROM_ADDRESS = SMTP_USERNAME;

    public static boolean isConfigured() {
        return !SMTP_USERNAME.equals("SEU_EMAIL@gmail.com")
                && !SMTP_PASSWORD.equals("SUA_SENHA_DE_APP")
                && !SMTP_USERNAME.trim().isEmpty()
                && !SMTP_PASSWORD.trim().isEmpty();
    }
}

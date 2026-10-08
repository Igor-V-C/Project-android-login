package com.example.applogin;

public final class EmailConfig {

    private EmailConfig() {
    }

    public static final String SMTP_HOST =
            "smtp.gmail.com";

    public static final int SMTP_PORT =
            587;

    public static final String SMTP_USERNAME =
            "";

    public static final String SMTP_PASSWORD =
            "";

    public static final String FROM_NAME =
            "AppLogin";

    public static final String FROM_ADDRESS =
            SMTP_USERNAME;

    public static boolean isConfigured() {
        return SMTP_USERNAME.equals("")
                && SMTP_PASSWORD.equals("");
    }
}

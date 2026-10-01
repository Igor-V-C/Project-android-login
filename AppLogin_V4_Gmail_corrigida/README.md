# AppLogin V4 - Gmail SMTP + Apache Commons Email

Esta versão usa Gmail SMTP diretamente através do Apache Commons Email.

## Configuração

1. Abra `app/src/main/java/com/example/applogin/EmailConfig.java`.
2. Em `SMTP_USERNAME`, coloque a conta Gmail que será usada para enviar as mensagens.
3. Em `SMTP_PASSWORD`, coloque a **Senha de app** da Conta Google, não a senha normal.
4. Faça Sync do Gradle e execute o projeto.

O Gmail usa `smtp.gmail.com` na porta `587` com STARTTLS. O Google informa que as Senhas de app têm 16 caracteres e exigem verificação em duas etapas.

### V4
- Código de recuperação aleatório com 6 dígitos.
- Novo código invalida o anterior.
- Cada código fica válido por 40 segundos.
- O código é digitado no próprio campo de senha.
- Usuários e e-mails continuam sendo buscados no SQLite da V2.

## Dependência de e-mail

`org.apache.commons:commons-email2-jakarta:2.0.0-M1`.

## Segurança

A senha de app não deveria ficar embutida em um APK de produção. Para um trabalho acadêmico/teste local, ela fica centralizada no `EmailConfig.java` para simplificar a configuração.

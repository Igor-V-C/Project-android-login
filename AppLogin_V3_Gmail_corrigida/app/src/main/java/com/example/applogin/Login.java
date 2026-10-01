package com.example.applogin;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Login extends AppCompatActivity {

    private static final String CODIGO_FIXO = "123456";

    private EditText edtUsuario;
    private EditText edtSenha;
    private BancoDadosHelper banco;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        banco = new BancoDadosHelper(this);

        edtUsuario = findViewById(R.id.edtUsuarioLogin);
        edtSenha = findViewById(R.id.edtSenhaLogin);
        SenhaViewUtils.configurarOlhoSenha(edtSenha);

        Button btnLogin = findViewById(R.id.btnLogin);
        Button btnEsqueciSenha = findViewById(R.id.btnEsqueciSenha);
        Button btnCadastro = findViewById(R.id.btnCadastro);

        btnLogin.setOnClickListener(v -> fazerLogin());
        btnEsqueciSenha.setOnClickListener(v -> abrirRecuperacao());
        btnCadastro.setOnClickListener(v ->
                startActivity(new Intent(this, Cadastro.class))
        );
    }

    private void fazerLogin() {
        String senha = edtSenha.getText().toString();

        // Código de recuperação também pode ser usado no campo de senha.
        if (RecuperacaoCodigo.verificar(senha)) {
            long id = RecuperacaoCodigo.getUsuarioId();
            RecuperacaoCodigo.invalidar();
            entrarNaHome(id);
            return;
        }

        String usuario = edtUsuario.getText().toString().trim();
        int tentativas = getIntent().getIntExtra("TENTATIVAS", 0);

        if (usuario.isEmpty()) {
            edtUsuario.setError("Digite um usuário");
            return;
        }

        if (senha.isEmpty()) {
            edtSenha.setError("Digite sua senha");
            return;
        }

        executor.execute(() -> {
            Usuario encontrado = banco.buscarPorUsuario(usuario);
            boolean correta = encontrado != null
                    && SenhaUtils.verificarSenha(
                    senha,
                    encontrado.getSenhaHash()
            );

            runOnUiThread(() -> {
                if (correta) {
                    entrarNaHome(encontrado.getId());
                    return;
                }

                int novasTentativas = tentativas + 1;

                if (novasTentativas >= 3) {
                    Toast.makeText(
                            this,
                            "Terceira tentativa incorreta.",
                            Toast.LENGTH_SHORT
                    ).show();

                    Intent intent = new Intent(this, MainActivity.class);
                    intent.addFlags(
                            Intent.FLAG_ACTIVITY_CLEAR_TOP
                                    | Intent.FLAG_ACTIVITY_SINGLE_TOP
                    );
                    startActivity(intent);
                    finish();
                    return;
                }

                Intent intent = new Intent(this, WrongPassword.class);
                intent.putExtra("TENTATIVAS", novasTentativas);
                startActivity(intent);
                finish();
            });
        });
    }

    private void entrarNaHome(long id) {
        Intent intent = new Intent(this, Home.class);
        intent.putExtra("USUARIO_ID", id);
        startActivity(intent);
        finish();
    }

    private void abrirRecuperacao() {
        final EditText emailInput = new EditText(this);
        emailInput.setHint("Digite seu e-mail");
        emailInput.setInputType(
                InputType.TYPE_CLASS_TEXT
                        | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        );

        int padding = (int) (
                24 * getResources().getDisplayMetrics().density
        );
        emailInput.setPadding(
                padding,
                padding / 2,
                padding,
                padding / 2
        );

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Recuperar senha")
                .setMessage("Informe o e-mail cadastrado para receber um código.")
                .setView(emailInput)
                .setNegativeButton("Cancelar", null)
                .setPositiveButton("Enviar código", null)
                .create();

        dialog.setOnShowListener(d ->
                dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                        .setOnClickListener(v -> {
                            String email = emailInput
                                    .getText()
                                    .toString()
                                    .trim();

                            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                                emailInput.setError("E-mail inválido");
                                return;
                            }

                            dialog.dismiss();
                            executor.execute(() -> enviarCodigoFixo(email));
                        })
        );

        dialog.show();
    }

    private void enviarCodigoFixo(String email) {
        Usuario usuario = banco.buscarPorEmail(email);

        if (usuario == null) {
            runOnUiThread(() -> Toast.makeText(
                    this,
                    "E-mail não encontrado.",
                    Toast.LENGTH_LONG
            ).show());
            return;
        }

        try {
            EmailSender.enviarCodigo(email, CODIGO_FIXO);
            RecuperacaoCodigo.ativarCodigoFixo(
                    CODIGO_FIXO,
                    usuario.getId()
            );

            runOnUiThread(() -> Toast.makeText(
                    this,
                    "Código enviado. Ele é válido por 40 segundos.",
                    Toast.LENGTH_LONG
            ).show());

        } catch (Exception e) {
            RecuperacaoCodigo.invalidar();

            runOnUiThread(() -> Toast.makeText(
                    this,
                    "Falha ao enviar e-mail: " + e.getMessage(),
                    Toast.LENGTH_LONG
            ).show());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executor.shutdown();
        if (banco != null) {
            banco.close();
        }
    }
}

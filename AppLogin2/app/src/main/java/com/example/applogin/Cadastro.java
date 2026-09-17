package com.example.applogin;

import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Cadastro extends AppCompatActivity {

    private EditText edtUsuario;
    private EditText edtSenha;
    private EditText edtEmail;
    private EditText edtTelefone;
    private EditText edtNome;
    private EditText edtDataNascimento;
    private EditText edtEndereco;
    private EditText edtCidade;
    private EditText edtEstado;

    private BancoDadosHelper banco;

    private final ExecutorService executor =
            Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_cadastro);

        banco = new BancoDadosHelper(this);

        edtUsuario = findViewById(R.id.edtUsuarioCadastro);
        edtSenha = findViewById(R.id.edtSenhaCadastro);
        edtEmail = findViewById(R.id.edtEmailCadastro);
        edtTelefone = findViewById(R.id.edtTelefoneCadastro);
        edtNome = findViewById(R.id.edtNomeCadastro);
        edtDataNascimento = findViewById(R.id.edtDataNascimentoCadastro);
        edtEndereco = findViewById(R.id.edtEnderecoCadastro);
        edtCidade = findViewById(R.id.edtCidadeCadastro);
        edtEstado = findViewById(R.id.edtEstadoCadastro);

        Button btnCadastrar =
                findViewById(R.id.btnCadastrar);

        btnCadastrar.setOnClickListener(v -> cadastrar());
    }

    private void cadastrar() {

        String usuario =
                edtUsuario.getText().toString().trim();

        String senha =
                edtSenha.getText().toString();

        String email =
                edtEmail.getText().toString().trim();

        String telefone =
                edtTelefone.getText().toString().trim();

        String nome =
                edtNome.getText().toString().trim();

        String dataNascimento =
                edtDataNascimento.getText().toString().trim();

        String endereco =
                edtEndereco.getText().toString().trim();

        String cidade =
                edtCidade.getText().toString().trim();

        String estado =
                edtEstado.getText().toString().trim();

        // Validações básicas

        if (usuario.isEmpty()) {
            edtUsuario.setError("Digite um usuário");
            edtUsuario.requestFocus();
            return;
        }

        if (senha.length() < 6) {
            edtSenha.setError("A senha deve ter pelo menos 6 caracteres");
            edtSenha.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtEmail.setError("Digite um e-mail válido");
            edtEmail.requestFocus();
            return;
        }

        if (telefone.isEmpty()) {
            edtTelefone.setError("Digite um telefone");
            edtTelefone.requestFocus();
            return;
        }

        if (nome.isEmpty()) {
            edtNome.setError("Digite seu nome");
            edtNome.requestFocus();
            return;
        }

        executor.execute(() -> {

            try {

                // Verifica se o usuário já existe
                Usuario usuarioExistente = banco.buscarPorUsuario(usuario);

                if (usuarioExistente != null) {
                    runOnUiThread(() ->
                            Toast.makeText(
                                    Cadastro.this,
                                    "Esse usuário já existe.",
                                    Toast.LENGTH_LONG
                            ).show()
                    );
                    return;
                }

                String senhaHash = SenhaUtils.gerarHash(senha);

                Usuario novoUsuario = new Usuario(
                        0,
                        usuario,
                        senhaHash,
                        email,
                        telefone,
                        nome,
                        dataNascimento,
                        endereco,
                        cidade,
                        estado
                );

                long resultado =
                        banco.inserirUsuario(novoUsuario);

                if (resultado == -1) {

                    runOnUiThread(() ->
                            Toast.makeText(
                                    Cadastro.this,
                                    "Não foi possível cadastrar.",
                                    Toast.LENGTH_LONG
                            ).show()
                    );

                    return;
                }

                runOnUiThread(() -> {

                    Toast.makeText(
                            Cadastro.this,
                            "Cadastro realizado com sucesso!",
                            Toast.LENGTH_LONG
                    ).show();

                    finish();
                });

            } catch (Exception e) {

                runOnUiThread(() ->
                        Toast.makeText(
                                Cadastro.this,
                                "Erro: " + e.getMessage(),
                                Toast.LENGTH_LONG
                        ).show()
                );
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        executor.shutdown();
        banco.close();
    }
}
package com.example.applogin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Login extends AppCompatActivity {

    private EditText edtUsuario;
    private EditText edtSenha;

    private BancoDadosHelper banco;

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        banco = new BancoDadosHelper(this);

        edtUsuario = findViewById(R.id.editTextEmail);
        edtSenha = findViewById(R.id.editTextSenha);

        Button btnLogin = findViewById(R.id.botao);

        btnLogin.setOnClickListener(v -> fazerLogin());
    }

    private void fazerLogin() {

        String usuario =
                edtUsuario.getText().toString().trim();

        String senha =
                edtSenha.getText().toString();

        if (usuario.isEmpty()) {
            edtUsuario.setError("Digite seu usuário");
            edtUsuario.requestFocus();
            return;
        }

        if (senha.isEmpty()) {
            edtSenha.setError("Digite sua senha");
            edtSenha.requestFocus();
            return;
        }

        executor.execute(() -> {

            Usuario usuarioEncontrado =
                    banco.buscarPorUsuario(usuario);

            if (usuarioEncontrado == null) {

                runOnUiThread(() ->
                        Toast.makeText(
                                Login.this,
                                "Usuário não encontrado.",
                                Toast.LENGTH_LONG
                        ).show()
                );

                return;
            }

            boolean senhaCorreta =
                    SenhaUtils.verificarSenha(
                            senha,
                            usuarioEncontrado.getSenhaHash()
                    );

            if (!senhaCorreta) {

                runOnUiThread(() ->
                        Toast.makeText(
                                Login.this,
                                "Senha incorreta.",
                                Toast.LENGTH_LONG
                        ).show()
                );

                return;
            }

            // Login correto
            Intent intent =
                    new Intent(
                            Login.this,
                            Home.class
                    );

            // Mandamos o ID do usuário para a Home
            intent.putExtra(
                    "USUARIO_ID",
                    usuarioEncontrado.getId()
            );

            startActivity(intent);

            finish();
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        executor.shutdown();
        banco.close();
    }
}
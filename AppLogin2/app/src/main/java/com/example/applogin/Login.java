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
        Button btnCadastro = findViewById(R.id.botao2);

        btnLogin.setOnClickListener(v -> fazerLogin());
        btnCadastro.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                    Intent intent = new Intent(Login.this, Cadastro.class);
                    startActivity(intent);

            }});
        configurarOlhoSenha(edtSenha);

    }
    private void configurarOlhoSenha(EditText campoSenha) {

        campoSenha.setOnTouchListener((v, event) -> {

            if (campoSenha.getCompoundDrawablesRelative()[2] == null) {
                return false;
            }

            int drawableWidth =
                    campoSenha.getCompoundDrawablesRelative()[2].getBounds().width();

            float limite =
                    campoSenha.getWidth()
                            - campoSenha.getPaddingEnd()
                            - drawableWidth
                            - campoSenha.getCompoundDrawablePadding();

            boolean tocouNoOlho = event.getX() >= limite;

            if (!tocouNoOlho) {
                return false;
            }

            switch (event.getAction()) {

                case android.view.MotionEvent.ACTION_DOWN:

                    campoSenha.setInputType(
                            android.text.InputType.TYPE_CLASS_TEXT
                                    | android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                    );

                    campoSenha.setSelection(
                            campoSenha.getText().length()
                    );

                    return true;

                case android.view.MotionEvent.ACTION_UP:
                case android.view.MotionEvent.ACTION_CANCEL:

                    campoSenha.setInputType(
                            android.text.InputType.TYPE_CLASS_TEXT
                                    | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
                    );

                    campoSenha.setSelection(
                            campoSenha.getText().length()
                    );

                    return true;
            }

            return false;
        });
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
                Intent intent = new Intent(Login.this, Cadastro.class);
                startActivity(intent);
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
                Intent intent = new Intent(Login.this, Cadastro.class);
                startActivity(intent);
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
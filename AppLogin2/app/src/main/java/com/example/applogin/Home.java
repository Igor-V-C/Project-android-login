package com.example.applogin;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Home extends AppCompatActivity {

    private TextView txtNome;
    private TextView txtUsuario;
    private TextView txtEmail;
    private TextView txtTelefone;
    private TextView txtDataNascimento;
    private TextView txtEndereco;
    private TextView txtCidade;
    private TextView txtEstado;

    private BancoDadosHelper banco;

    private final ExecutorService executor =
            Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);

        banco = new BancoDadosHelper(this);

        txtNome = findViewById(R.id.txtNome);
        txtUsuario = findViewById(R.id.txtUsuario);
        txtEmail = findViewById(R.id.txtEmail);
        txtTelefone = findViewById(R.id.txtTelefone);
        txtDataNascimento =
                findViewById(R.id.txtDataNascimento);
        txtEndereco = findViewById(R.id.txtEndereco);
        txtCidade = findViewById(R.id.txtCidade);
        txtEstado = findViewById(R.id.txtEstado);

        carregarUsuario();
    }

    private void carregarUsuario() {

        long usuarioId =
                getIntent().getLongExtra(
                        "USUARIO_ID",
                        -1
                );

        if (usuarioId == -1) {

            Toast.makeText(
                    this,
                    "Usuário não identificado.",
                    Toast.LENGTH_LONG
            ).show();

            finish();
            return;
        }

        executor.execute(() -> {

            Usuario usuario =
                    banco.buscarPorId(usuarioId);

            if (usuario == null) {

                runOnUiThread(() ->
                        Toast.makeText(
                                Home.this,
                                "Não foi possível carregar os dados.",
                                Toast.LENGTH_LONG
                        ).show()
                );

                return;
            }

            runOnUiThread(() -> {

                txtNome.setText(
                        usuario.getNomeCompleto()
                );

                txtUsuario.setText(
                        usuario.getUsuario()
                );

                txtEmail.setText(
                        usuario.getEmail()
                );

                txtTelefone.setText(
                        usuario.getTelefone()
                );

                txtDataNascimento.setText(
                        usuario.getDataNascimento()
                );

                txtEndereco.setText(
                        usuario.getEndereco()
                );

                txtCidade.setText(
                        usuario.getCidade()
                );

                txtEstado.setText(
                        usuario.getEstado()
                );
            });
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        executor.shutdown();
        banco.close();
    }
}
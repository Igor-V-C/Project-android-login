package com.example.applogin;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Home extends AppCompatActivity {
    private BancoDadosHelper banco;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private TextView txtNome, txtUsuario, txtEmail, txtTelefone, txtDataNascimento, txtEndereco, txtCidade, txtEstado;
    private ImageView imgFoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        banco = new BancoDadosHelper(this);

        txtNome = findViewById(R.id.txtNome);
        txtUsuario = findViewById(R.id.txtUsuario);
        txtEmail = findViewById(R.id.txtEmail);
        txtTelefone = findViewById(R.id.txtTelefone);
        txtDataNascimento = findViewById(R.id.txtDataNascimento);
        txtEndereco = findViewById(R.id.txtEndereco);
        txtCidade = findViewById(R.id.txtCidade);
        txtEstado = findViewById(R.id.txtEstado);
        imgFoto = findViewById(R.id.imgFotoHome);

        Button voltar = findViewById(R.id.btnVoltarMain);
        voltar.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });

        carregarUsuario();
    }

    private void carregarUsuario() {
        long id = getIntent().getLongExtra("USUARIO_ID", -1);
        if (id == -1) { Toast.makeText(this, "Usuário não identificado.", Toast.LENGTH_LONG).show(); finish(); return; }

        executor.execute(() -> {
            Usuario usuario = banco.buscarPorId(id);
            if (usuario == null) {
                runOnUiThread(() -> Toast.makeText(this, "Não foi possível carregar os dados.", Toast.LENGTH_LONG).show());
                return;
            }
            runOnUiThread(() -> {
                txtNome.setText(usuario.getNomeCompleto());
                txtUsuario.setText(usuario.getUsuario());
                txtEmail.setText(usuario.getEmail());
                txtTelefone.setText(usuario.getTelefone());
                txtDataNascimento.setText(usuario.getDataNascimento());
                txtEndereco.setText(usuario.getEndereco());
                txtCidade.setText(usuario.getCidade());
                txtEstado.setText(usuario.getEstado());
                if (usuario.getFotoPath() != null) {
                    imgFoto.setImageBitmap(BitmapFactory.decodeFile(usuario.getFotoPath()));
                }
            });
        });
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        executor.shutdown();
        if (banco != null) banco.close();
    }
}

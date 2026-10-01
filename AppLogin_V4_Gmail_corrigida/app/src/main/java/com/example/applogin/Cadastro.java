package com.example.applogin;

import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Cadastro extends AppCompatActivity {
    private EditText edtUsuario, edtSenha, edtEmail, edtTelefone, edtNome;
    private EditText edtDataNascimento, edtEndereco, edtCidade, edtEstado;
    private ImageView imgFoto;
    private String fotoPath;
    private BancoDadosHelper banco;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    private final ActivityResultLauncher<String[]> seletorImagem =
            registerForActivityResult(new ActivityResultContracts.OpenDocument(), uri -> {
                if (uri == null) return;
                executor.execute(() -> {
                    String caminho = copiarImagemParaApp(uri);
                    runOnUiThread(() -> {
                        if (caminho == null) {
                            Toast.makeText(this, "Não foi possível carregar a foto.", Toast.LENGTH_LONG).show();
                            return;
                        }
                        String anterior = fotoPath;
                        fotoPath = caminho;
                        imgFoto.setImageBitmap(BitmapFactory.decodeFile(caminho));
                        if (anterior != null) new File(anterior).delete();
                    });
                });
            });

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
        imgFoto = findViewById(R.id.imgFotoCadastro);

        Button escolherFoto = findViewById(R.id.btnEscolherFoto);
        Button cadastrar = findViewById(R.id.btnCadastrar);
        escolherFoto.setOnClickListener(v -> seletorImagem.launch(new String[]{"image/*"}));
        SenhaViewUtils.configurarOlhoSenha(edtSenha);
        cadastrar.setOnClickListener(v -> cadastrarUsuario());
    }

    private void cadastrarUsuario() {
        String usuario = edtUsuario.getText().toString().trim();
        String senha = edtSenha.getText().toString();
        String email = edtEmail.getText().toString().trim();
        String telefone = edtTelefone.getText().toString().trim();
        String nome = edtNome.getText().toString().trim();
        String data = edtDataNascimento.getText().toString().trim();
        String endereco = edtEndereco.getText().toString().trim();
        String cidade = edtCidade.getText().toString().trim();
        String estado = edtEstado.getText().toString().trim();

        if (usuario.isEmpty()) { edtUsuario.setError("Digite um usuário"); return; }
        if (senha.isEmpty()) { edtSenha.setError("Digite uma senha"); return; }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) { edtEmail.setError("Digite um e-mail válido"); return; }
        if (telefone.isEmpty()) { edtTelefone.setError("Digite um telefone"); return; }
        if (nome.isEmpty()) { edtNome.setError("Digite seu nome"); return; }
        if (fotoPath == null || !new File(fotoPath).exists()) {
            Toast.makeText(this, "Escolha uma foto de perfil.", Toast.LENGTH_LONG).show();
            return;
        }

        executor.execute(() -> {
            try {
                if (banco.buscarPorUsuario(usuario) != null) {
                    runOnUiThread(() -> Toast.makeText(this, "Esse usuário já existe.", Toast.LENGTH_LONG).show());
                    return;
                }
                if (banco.buscarPorEmail(email) != null) {
                    runOnUiThread(() -> Toast.makeText(this, "Esse e-mail já está cadastrado.", Toast.LENGTH_LONG).show());
                    return;
                }

                Usuario novo = new Usuario(
                        0, usuario, SenhaUtils.gerarHash(senha), email, telefone, nome,
                        data, endereco, cidade, estado, fotoPath
                );
                long resultado = banco.inserirUsuario(novo);
                if (resultado == -1) {
                    new File(fotoPath).delete();
                    runOnUiThread(() -> Toast.makeText(this, "Não foi possível cadastrar.", Toast.LENGTH_LONG).show());
                    return;
                }
                runOnUiThread(() -> {
                    Toast.makeText(this, "Cadastro realizado com sucesso!", Toast.LENGTH_LONG).show();
                    finish();
                });
            } catch (Exception e) {
                runOnUiThread(() -> Toast.makeText(this, "Erro: " + e.getMessage(), Toast.LENGTH_LONG).show());
            }
        });
    }

    private String copiarImagemParaApp(Uri uri) {
        File destino = new File(getFilesDir(), "foto_" + UUID.randomUUID() + ".img");
        try (InputStream input = getContentResolver().openInputStream(uri);
             FileOutputStream output = new FileOutputStream(destino)) {
            if (input == null) return null;
            byte[] buffer = new byte[8192];
            int lidos;
            while ((lidos = input.read(buffer)) != -1) output.write(buffer, 0, lidos);
            output.flush();
            return destino.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executor.shutdown();
        if (banco != null) banco.close();
    }
}

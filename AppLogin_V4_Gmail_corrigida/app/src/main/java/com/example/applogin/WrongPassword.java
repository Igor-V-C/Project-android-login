package com.example.applogin;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class WrongPassword extends AppCompatActivity {

    private TextView contador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wrong_password);

        contador = findViewById(R.id.contador);
        int tentativas = getIntent().getIntExtra("TENTATIVAS", 1);

        new CountDownTimer(5000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                contador.setText("Senha incorreta!\nVoltando em "
                        + (millisUntilFinished / 1000)
                        + " segundos...");
            }

            @Override
            public void onFinish() {
                Intent intent = new Intent(WrongPassword.this, Login.class);
                intent.putExtra("TENTATIVAS", tentativas);
                startActivity(intent);
                finish();
            }
        }.start();
    }
}

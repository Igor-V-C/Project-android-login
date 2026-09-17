
package com.example.applogin;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.applogin.Login;
import com.example.applogin.MainActivity;

public class WrongPassword extends AppCompatActivity {

    private TextView contador;
    private static int tentativas = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wrong_password);
        contador = findViewById(R.id.contador);
        tentativas++;
        if (tentativas >= 3) {

            Intent intent = new Intent(WrongPassword.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                    Intent.FLAG_ACTIVITY_NEW_TASK);
            tentativas = 0;

            startActivity(intent);
            finish();

            return;
        } else {

            new CountDownTimer(5000, 1000) {

                @Override
                public void onTick(long millisUntilFinished) {
                    contador.setText(
                            "Senha incorreta!\nVoltando em "
                                    + (millisUntilFinished / 1000)
                                    + " segundos..."
                    );
                }

                @Override
                public void onFinish() {
                    Intent intent = new Intent(WrongPassword.this, Login.class);
                    startActivity(intent);
                    finish();
                }

            }.start();
        }
    }
}
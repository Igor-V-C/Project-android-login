package com.example.applogin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        EditText EmailCaixa = (EditText) findViewById(R.id.editTextEmail);
        EditText SenhaCaixa = (EditText) findViewById(R.id.editTextSenha);
        Button myButton = findViewById(R.id.botao);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        myButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                if (EmailCaixa.getText().toString().equals("Email123") && SenhaCaixa.getText().toString().equals("Senha123")){
                    Intent intent = new Intent(Login.this, Home.class);
                    startActivity(intent);
                }
                else {
                    Intent intent = new Intent(Login.this, WrongPassword.class);
                    startActivity(intent);
                }
            }});
    }
}
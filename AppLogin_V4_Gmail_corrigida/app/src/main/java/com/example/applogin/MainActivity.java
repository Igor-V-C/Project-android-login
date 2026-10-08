package com.example.applogin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button entrar = findViewById(R.id.entrar);
        entrar.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, Login.class))
        );
    }
}

package com.example.app1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class login extends AppCompatActivity {
    EditText userName, passWord;
    TextView register;
    Button sesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Referenciar los objetos (instanciados)
        userName = findViewById(R.id.etUserName);
        passWord = findViewById(R.id.etPassword);
        register = findViewById(R.id.tvRegister);
        sesion = findViewById(R.id.btnSesion);
        // Eventos
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Invocar la actividad del registro de usuario
                // a través de la clase Intent
                // Si la actividad a llamar no se le envian datos,
                // se hace de la siguiente manera
                startActivity(new Intent(getApplicationContext(),reguser.class));
            }
        });
    }
}
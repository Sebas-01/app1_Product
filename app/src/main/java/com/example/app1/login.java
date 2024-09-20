package com.example.app1;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class login extends AppCompatActivity {
    EditText userName, passWord;
    TextView register;
    Button sesion;

    clsDB oDB = new clsDB(this, "dbProduct", null, 1);
    // Objeto para manipular la tabla de Product
    User oUser = new User();
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

        sesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mUser= userName.getText().toString(); // tomando el contenido de este objeto referenciado
                String mPassword = passWord.getText().toString();
                if (checkData(mUser,mPassword)){
                    if (searchUser(mUser,mPassword).size()>0){
                        //invocar la actividad del CRUD del producto
                        Intent intProduct = new Intent(getApplicationContext(), MainActivity.class);

                        //enviar parametros a  otra actividad
                        intProduct.putExtra("xFullname", oUser.getFullname());
                        startActivity(intProduct);
                    }else {
                        Toast.makeText(getApplicationContext(), "el usuario o contraseño no son correctos", Toast.LENGTH_SHORT);
                    }

                }
                else {
                    Toast.makeText(getApplicationContext(), "Llene por favor tpdos los campos", Toast.LENGTH_SHORT);
                }

            }
        });

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

    //buscar el usuario
    private ArrayList<User> searchUser(String mUser, String mPassword) {
        // Crear el objeto de tipo ArrayList que será el valor que retorne
        ArrayList<User> arrUser = new ArrayList<User>();
        // Crear un objeto de la clase SQLiteDatabase
        SQLiteDatabase osdbRead = oDB.getReadableDatabase();
        String query = "Select username,fullname from user where username = '"+mUser+"' and password= '"+mPassword+"'";
        // Generar una tabla cursor para almacenar los datos del query
        Cursor cUser = osdbRead.rawQuery(query,null);
        // Chequear como quedo la tabla cursor
        if (cUser.moveToFirst()){
            oUser.setUsername(mUser);
            oUser.setFullname(cUser.getString(1));
            arrUser.add(oUser);
        }
        return arrUser;
    }

    private boolean checkData(String mUser, String mPassword) {
        return !mUser.isEmpty() && !mPassword.isEmpty();
    }
}
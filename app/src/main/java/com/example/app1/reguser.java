package com.example.app1;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
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

public class reguser extends AppCompatActivity {
    EditText userName, fullName, password;
    Button register;
    TextView sesion;

    clsDB oDB = new clsDB(this, "dbProduct", null, 1);
    // Objeto para manipular la tabla de Product
    User oUser = new User();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reguser);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        userName = findViewById(R.id.etUserNameR);
        fullName = findViewById(R.id.etFullNameR);
        password = findViewById(R.id.etPasswordR);
        register = findViewById(R.id.btnRegisterR);
        sesion = findViewById(R.id.tvSesionR);
        // Eventos
        sesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), login.class));
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mUser= userName.getText().toString(); // tomando el contenido de este objeto referenciado
                String mFullName = fullName.getText().toString();
                String mPassword = password.getText().toString();
                if (checkData(mUser, mFullName, mPassword)){
                    if (searchUser(mUser).size() == 0){
                        // Codigo para guardar el registro (user)
                        SQLiteDatabase osdbWrite = oDB.getWritableDatabase();
                        // ContentValues para agregar el registro
                        ContentValues cvUser = new ContentValues();
                        // Asignar el contenido de cada campo con los valores digitados
                        cvUser.put("username",mUser);
                        cvUser.put("fullname", mFullName);
                        cvUser.put("password", mPassword);

                        // Agregar el nuevo usuario a la tabla
                        osdbWrite.insert("user",null, cvUser);
                        // Cerrar la conexion a la bd
                        osdbWrite.close();
                        Toast.makeText(getApplicationContext(), "usuario agregado correctamente", Toast.LENGTH_LONG).show();

                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Este usuario ya exite...", Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(),"Ingrese todos los datos", Toast.LENGTH_LONG).show();
                }
            }

        });
    }



    private ArrayList<User> searchUser(String mUser) {
        // Crear el objeto de tipo ArrayList que será el valor que retorne
        ArrayList<User> arrUser = new ArrayList<User>();
        // Crear un objeto de la clase SQLiteDatabase
        SQLiteDatabase osdbRead = oDB.getReadableDatabase();
        String query = "Select username, fullname, password from user where username = '"+mUser+"'";
        // Generar una tabla cursor para almacenar los datos del query
        Cursor cUser = osdbRead.rawQuery(query,null);
        // Chequear como quedo la tabla cursor
        if (cUser.moveToFirst()){
            oUser.setUsername(mUser);
            oUser.setFullname(cUser.getString(1));
            oUser.setPassword(cUser.getString(2));

            arrUser.add(oUser);
        }
        return arrUser;
    }


    private boolean checkData(String mUser, String mFullName, String mPassword) {
        return !mUser.isEmpty() && !mFullName.isEmpty() && !mPassword.isEmpty();
    }
}
package com.example.app1;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
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
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mUser= userName.getText().toString(); // tomando el contenido de este objeto referenciado
                String mFullName = fullName.getText().toString();
                String mPassword = password.getText().toString();
                if (checkData(mUser, mFullName, mPassword)){
                    if (searchUser(mUser).size() == 0){
                        // Codigo para guardar el registro (producto)
                        SQLiteDatabase osdbWrite = oDB.getWritableDatabase();
                        // ContentValues para agregar el registro
                        ContentValues cvProduct = new ContentValues();
                        // Asignar el contenido de cada campo con los valores digitados
                        cvProduct.put("reference",mRef);
                        cvProduct.put("description", mDesc);
                        cvProduct.put("price", mPrice);
                        cvProduct.put("typeref", mTypeRef.equals("Comestible") ? 1 : 0 );
                        // Agregar el nuevo producto a la tabla
                        osdbWrite.insert("product",null, cvProduct);
                        // Cerrar la conexion a la bd
                        osdbWrite.close();
                        message.setTextColor(Color.GREEN);
                        message.setText("Producto agregado correctamente...");

                    }
                    else{
                        message.setTextColor(Color.RED);
                        message.setText("Referencia EXISTENTE. Inténtelo con otra");
                    }
                }
                else{
                    message.setTextColor(Color.RED);
                    message.setText("Debe ingresar todos los datos...");
                }
            }

        });
    }

    private ArrayList<User> searchUser(String mUser) {
        // Crear el objeto de tipo ArrayList que será el valor que retorne
        ArrayList<Product> arrProduct = new ArrayList<Product>();
        // Crear un objeto de la clase SQLiteDatabase
        SQLiteDatabase osdbRead = oDB.getReadableDatabase();
        String query = "Select description, price, typeref from product where reference = '"+mRef+"'";
        // Generar una tabla cursor para almacenar los datos del query
        Cursor cProduct = osdbRead.rawQuery(query,null);
        // Chequear como quedo la tabla cursor
        if (cProduct.moveToFirst()){
            oProduct.setReference(mRef);
            oProduct.setDescription(cProduct.getString(0));
            oProduct.setPrice(cProduct.getInt(1));
            oProduct.setTyperef(cProduct.getInt(2));
            arrProduct.add(oProduct);
        }
        return arrProduct;
    }


    private boolean checkData(String mUser, String mFullName, String mPassword) {
        return !mUser.isEmpty() && !mFullName.isEmpty() && !mPassword.isEmpty();
    }
}
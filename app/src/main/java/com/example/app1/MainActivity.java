package com.example.app1;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    // Instanciar todos los elementos que tienen id en el archivo xml
    EditText reference, description, price;
    Spinner refType;
    ImageButton save, search, edit, delete, list;
    TextView message;
    // Definir el array para llenar el spinner
    String[] arrayTypeRef = {"Comestible","No Comestible"};
    // Instanciar la clase clsDB para actualizar la base de datos
    clsDB oDB = new clsDB(this, "dbProduct", null, 1);
    // Objeto para manipular la tabla de Product
    Product oProduct = new Product();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Referenciar estos objetos con los ids, respectivamente
        reference = findViewById(R.id.editReference);
        description = findViewById(R.id.editDescription);
        price = findViewById(R.id.editPrice);
        refType = findViewById(R.id.spRefType);
        save = findViewById(R.id.ibSave);
        search = findViewById(R.id.ibSearch);
        edit = findViewById(R.id.ibEdit);
        delete = findViewById(R.id.ibDelete);
        list = findViewById(R.id.ibList);
        message = findViewById(R.id.tvMessage);
        // Definir el arrayAdapter para llenar el spinner
        ArrayAdapter<String> adpTypeRef = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_checked,arrayTypeRef);
        // Asignar el adaptador al Spinner
        refType.setAdapter(adpTypeRef);
        // Eventos de cada Boton
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mRef = reference.getText().toString();
                String mDesc = description.getText().toString();
                int mPrice = Integer.parseInt(price.getText().toString());
                int mrefType = refType.getSelectedItem().toString().equals("Comestible") ? 1 : 0;
                if (checkData(mRef, mDesc, String.valueOf(mPrice))){
                    // Actualizar el producto con todos los datos
                    SQLiteDatabase osdbWrite = oDB.getWritableDatabase();
                    osdbWrite.execSQL("Update product SET description = '"+mDesc+"', price = "+mPrice+", typeref = "+mrefType+" Where reference = '"+mRef+"'");
                    message.setTextColor(Color.GREEN);
                    message.setText("El producto se actualizó correctamente...");
                }
                else{
                    message.setTextColor(Color.RED);
                    message.setText("Debe ingresar todos los datos para actualizar");
                }
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!reference.getText().toString().isEmpty()){
                    // Buscar la referencia a través del método searchReference
                    if (searchReference(reference.getText().toString()).size() > 0){
                        // tomar los datos del objeto oProduct (global)
                        // asignar el contenido de cada atributo
                        // a cada editText y spinner, respectivamente
                        //reference.setEnabled(false);
                        description.setText(oProduct.getDescription());
                        price.setText(String.valueOf(oProduct.getPrice()));
                        // Seleccionar la opcion del spinner para:
                        // 1: Comestible y 0: No comestible
                        refType.setSelection(oProduct.getTyperef() == 1 ? 0 : 1);
                    }
                    else{
                        message.setTextColor(Color.RED);
                        message.setText("La referencia NO EXISTE. Inténtelo con otra...!");
                    }

                }
                else{
                    message.setTextColor(Color.RED);
                    message.setText("Ingrese la referencia a buscar!");
                }
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mRef = reference.getText().toString(); // tomando el contenido de este objeto referenciado
                String mDesc = description.getText().toString();
                String mPrice = price.getText().toString();
                String mTypeRef = refType.getSelectedItem().toString();
                if (checkData(mRef, mDesc, mPrice)){
                    if (searchReference(mRef).size() == 0){
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

    private ArrayList<Product> searchReference(String mRef) {
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

    private boolean checkData(String mRef, String mDesc, String mPrice) {
        return !mRef.isEmpty() && !mDesc.isEmpty() && !mPrice.isEmpty();
    }
}
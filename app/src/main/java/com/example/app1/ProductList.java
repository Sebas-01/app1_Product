package com.example.app1;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class ProductList extends AppCompatActivity {

    ListView lvpProducts;
    ArrayList<String> arrProduct;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_product_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        lvpProducts = findViewById(R.id.lvProducts);

        loadProduct();
    }

    private void loadProduct() {
        arrProduct = getAllProducts(); //ya tiene los productos
        if(arrProduct.size()>0){
            //llenar el array adapter
            ArrayAdapter<String> adpProducts = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,arrProduct);
            lvpProducts.setAdapter(adpProducts);
        }
        else {
            Toast.makeText(getApplicationContext(),"No hay productos en el inventario", Toast.LENGTH_LONG).show();
        }
    }

    private ArrayList<String> getAllProducts() {
        ArrayList<String> arrayProduct = new ArrayList<String>();

        clsDB oDB = new clsDB(this, "dbProduct", null, 1);

        SQLiteDatabase osdbProduct = oDB.getReadableDatabase();

        Cursor cProducts = osdbProduct.rawQuery("select reference, description, price from product",null);
        if(cProducts.moveToFirst()){
            //recorrer la tabla cursor
            do {
                // pasar los datos al array list
                String lProducts = cProducts.getString(0)+ "\n"+ cProducts.getString(1) + "\n" +cProducts.getInt(2);
                arrayProduct.add(lProducts);
            }while (cProducts.moveToNext());


        }

        return arrayProduct;
    }
}
package com.example.lab3databases;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    TextView productId;
    EditText productName, productPrice;
    Button addBtn, findBtn, deleteBtn;
    ListView productListView;

    ArrayList<String> productList;
    ArrayAdapter adapter;
    MyDBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        productList = new ArrayList<>();

        // info layout
        productId = findViewById(R.id.productId);
        productName = findViewById(R.id.productName);
        productPrice = findViewById(R.id.productPrice);

        //buttons
        addBtn = findViewById(R.id.addBtn);
        findBtn = findViewById(R.id.findBtn);
        deleteBtn = findViewById(R.id.deleteBtn);

        // listview
        productListView = findViewById(R.id.productListView);

        // db handler
        dbHandler = new MyDBHandler(this);

        // button listeners
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name;
                double price;
                try{
                    name = productName.getText().toString();
                    price = Double.parseDouble(productPrice.getText().toString());

                } catch(Exception e){
                    Toast.makeText(MainActivity.this, "Invalid entry", Toast.LENGTH_SHORT).show();
                    return;
                }
                Product product = new Product(name, price);

                dbHandler.addProduct(name,price);

                productName.setText("");
                productPrice.setText("");

//                Toast.makeText(MainActivity.this, "Add product", Toast.LENGTH_SHORT).show();
                viewProducts();
            }
        });

        findBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name;
                double price;
                try{
                    name = productName.getText().toString();
                } catch(Exception e){
                    name = "";
                }
                try{
                    price = Double.parseDouble(productPrice.getText().toString());
                } catch(Exception e){
                    price = -1;
                }
                find(name, price);
                //Toast.makeText(MainActivity.this, "Find product", Toast.LENGTH_SHORT).show();
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name;
                try{
                    name = productName.getText().toString();
                } catch(Exception e){
                    Toast.makeText(MainActivity.this, "No Match Found", Toast.LENGTH_SHORT).show();
                    return;
                }

                boolean result = dbHandler.deleteProduct(name);

                if(result){
                    Toast.makeText(MainActivity.this, "Record Deleted", Toast.LENGTH_SHORT).show();
                    productName.setText("");
                    viewProducts();
                }
                else{
                    Toast.makeText(MainActivity.this, "No Match Found", Toast.LENGTH_SHORT).show();

                }
            }
        });

        viewProducts();
    }

    private void viewProducts() {
        productList.clear();
        Cursor cursor = dbHandler.getData();
        if (cursor.getCount() == 0) {
            Toast.makeText(MainActivity.this, "Nothing to show", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                productList.add(cursor.getString(1) + " (" +cursor.getString(2)+")");
            }
        }

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, productList);
        productListView.setAdapter(adapter);
    }

    private void find(String name, double price){
        boolean exists = false;
        productList.clear();
        Cursor cursor = dbHandler.getData();
        if (cursor.getCount() == 0) {
            Toast.makeText(MainActivity.this, "Nothing to show", Toast.LENGTH_SHORT).show();
        } else {
            String dbname;
            String dbprice;
            while (cursor.moveToNext()) {
                dbname = cursor.getString(1);
                dbprice = cursor.getString(2);

                if(name.equals(dbname) || name.equals("")){
                    if(Double.compare(price, Double.parseDouble(dbprice)) == 0 || Double.compare(price,-1) == 0){
                        exists = true;
                        productList.add( dbname + " (" + dbprice +")");

                    }
                }
            }
        }

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, productList);
        productListView.setAdapter(adapter);
        if (!exists)
            Toast.makeText(MainActivity.this, "No Match Found", Toast.LENGTH_SHORT).show();
    }
}
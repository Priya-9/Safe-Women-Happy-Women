package com.example.safewomenhappywomen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class add_contacts extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contacts);

        final EditText phone1 = findViewById(R.id.phone1);
        final EditText phone2 = findViewById(R.id.phone2);
        final EditText email = findViewById(R.id.email);
        Button button = findViewById(R.id.submit);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String p1 = phone1.getText().toString();
                String p2 = phone2.getText().toString();
                String e = email.getText().toString();
                if(p1=="" || e=="")
                    Toast.makeText(getApplicationContext(), "Atleast Enter one Phone number and Email",Toast.LENGTH_SHORT).show();
                else
                {
                    SharedPreferences shrd = getSharedPreferences("contact", MODE_PRIVATE);
                    SharedPreferences.Editor editor = shrd.edit();
                    editor.putString("phone1", p1);
                    editor.putString("phone2", p2);
                    editor.putString("email", e);
                    editor.commit();
                    Toast.makeText(getApplicationContext(), p1+"   "+p2+"   "+e+"  !!!",Toast.LENGTH_SHORT).show();
                }
            }
        });

        BottomNavigationView bnv = findViewById(R.id.bottomnavi);
        bnv.setSelectedItemId(R.id.add_contact_icon);

        bnv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId()){
                    case R.id.add_contact_icon :
                        return true;
                    case R.id.home : startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.instruction_icon : startActivity(new Intent(getApplicationContext(), instruction.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });
    }
}
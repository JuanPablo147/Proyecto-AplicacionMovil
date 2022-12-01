package com.example.myapplication.ui.custom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Registrarse extends AppCompatActivity {

    private FirebaseFirestore DB;
    private FirebaseAuth db;
    private EditText nombre;
    private EditText contraseña1;
    private EditText contraseña2;
    private EditText correo;
    private EditText telefono;

    private boolean campoVacio;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarse);
        this.setTitle("Registro usuario");
        getSupportActionBar().setDisplayShowCustomEnabled(true);

        db=FirebaseAuth.getInstance();
        DB = FirebaseFirestore.getInstance();

        nombre=findViewById(R.id.EntradaNombre);
        contraseña1=findViewById(R.id.Entrada1Contraseña);
        contraseña2=findViewById(R.id.Entrada2Contraseña);
        correo=findViewById(R.id.EntradaEmail);
        telefono=findViewById(R.id.EntradaTelefono);
        Button Registrar=(Button) findViewById(R.id.button3);



    }

    public void onStart(){
        super.onStart();
        FirebaseUser usr=db.getCurrentUser();
    }
    /*
    public boolean longitudNombre(){
        boolean res=true;
        if(nombre.length()>50 && nombre.length()<3 ){

            Toast.makeText(getApplicationContext(), "Nombre no valido", Toast.LENGTH_SHORT).show();
            res=false;
        }
        return res;
    }
    public boolean longitudTelefono(){
        boolean res=true;
        if(telefono.length()>7 && telefono.length()<11 ){

            Toast.makeText(getApplicationContext(), "Numero de Telefono no valido", Toast.LENGTH_SHORT).show();
            res=false;
        }
        return res;
    }
    public boolean longitudContraseña(){
        boolean res=true;
        if(contraseña1.length()<6 && contraseña2.length()<6){

            Toast.makeText(getApplicationContext(), "Tamaño de contraseña debe ser mayor a 6", Toast.LENGTH_SHORT).show();
            res=false;
        }
        return res;
    }*/

    public void registrarUsuario(View v){
        /*if(longitudTelefono()){
        if(longitudNombre()){*/
        if(EsVacia1()) {

            if (contraseña1.getText().toString().equals(contraseña2.getText().toString()) ) {
                db.createUserWithEmailAndPassword(correo.getText().toString(), contraseña1.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override

                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = db.getCurrentUser();
                            Toast.makeText(getApplicationContext(), "Creado exitosamente", Toast.LENGTH_SHORT).show();

                            nuevaColeccion(nombre.getText().toString(), contraseña1.getText().toString(), contraseña2.getText().toString(), correo.getText().toString(), telefono.getText().toString());
                            onSupportNavegateUp();
                        } else {
                            EsVacia1();
                            Toast.makeText(getApplicationContext(), "ERROR: ingrese datos validos", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            } else {
                Toast.makeText(getApplicationContext(), "La contraseña no coincide", Toast.LENGTH_SHORT).show();

            }
        }//}}
    }
    public boolean EsVacia1(){
        boolean flag = true;


        if (nombre.getText().toString().isEmpty()) {
            //Toast.makeText(this, "Campo nombre vacio", Toast.LENGTH_LONG).show();
            nombre.setError("llenar campo");
            flag = false;
        }
        if (telefono.getText().toString().isEmpty()) {
           // Toast.makeText(this, "Campo telefono vacio", Toast.LENGTH_LONG).show();
            flag = false;
        }
        if (correo.getText().toString().isEmpty()) {
           // Toast.makeText(this, "Campo correo vacio", Toast.LENGTH_LONG).show();
            correo.setError("llenar campo");
            flag = false;
        }else{
            if(correo.getText().toString().contains("@gmail.com")){
                Toast.makeText(this,"Campo Email correcto", Toast.LENGTH_LONG).show();
            }else{
                correo.setError("llenar campo con correo");
                flag = false;
            }
        }



        if(contraseña1.getText().toString().isEmpty()){
            Toast.makeText(this,"Campo contraseña vacio", Toast.LENGTH_LONG).show();

            flag = false;}

        if(contraseña2.getText().toString().isEmpty()){

              Toast.makeText(this,"Campo confirmar contraseña vacio", Toast.LENGTH_LONG).show();

            flag =  false;
            }
        if(nombre.length()>50 || nombre.length()<3 ){

           // Toast.makeText(getApplicationContext(), "Nombre no valido", Toast.LENGTH_SHORT).show();
            nombre.setError("nombre no valido");
            flag =  false;
        }
        if(telefono.length()!=8){

          //  Toast.makeText(getApplicationContext(), "Numero de Telefono no valido", Toast.LENGTH_SHORT).show();
            telefono.setError("telefono con 8 digitos");
            flag =  false;
        }
        if(contraseña1.length()<5 && contraseña2.length()<5){

            Toast.makeText(getApplicationContext(), "Tamaño de contraseña debe ser mayor a 6", Toast.LENGTH_SHORT).show();

            flag =  false;
        }



        return flag;
    }
    public void esVacia(){
        boolean campoVacio=false;
        if(nombre.getText().toString().isEmpty()){
            campoVacio= true;
            Toast.makeText(this,"Campo nombre vacio", Toast.LENGTH_LONG).show();
        }else{

            if(telefono.getText().toString().isEmpty()){
                campoVacio= true;
                Toast.makeText(this,"Campo telefono vacio", Toast.LENGTH_LONG).show();
            }else{
                if(correo.getText().toString().isEmpty()){
                    campoVacio= true;
                    Toast.makeText(this,"Campo Email vacio", Toast.LENGTH_LONG).show();
                }
                else{
                    if(contraseña1.getText().toString().isEmpty()){
                        campoVacio= true;
                        Toast.makeText(this,"Campo contraseña vacio", Toast.LENGTH_LONG).show();
                    }else{
                        if(contraseña2.getText().toString().isEmpty()){
                            campoVacio= true;
                            Toast.makeText(this,"Campo confirmar contraseña vacio", Toast.LENGTH_LONG).show();

                        }
                    }
                }
            }

        }

    }
    public void nuevaColeccion(String n,String c,String c2,String gmail,String telf){
        Map<String, Object> user = new HashMap<>();
        user.put("nombre", n);
        user.put("contraseña1", c);
        user.put("contraseña2", c2);
        user.put("correo",gmail);
        user.put("telefono",telf);
        DB.collection("usuario").add(user);
    }

    public boolean onSupportNavegateUp(){

        onBackPressed();
        return false;

    }

}
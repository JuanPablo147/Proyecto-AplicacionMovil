package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.ui.custom.Registrarse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class INICIARSESION extends AppCompatActivity {

    private EditText correo,contraseña;
    private Button iniciar, crear;
    FirebaseAuth fAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciarsesion);
        this.setTitle("Iniciar sesión");
        getSupportActionBar().setDisplayShowCustomEnabled(true);


        fAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("usuario");
        correo = findViewById(R.id.EntradaEmailInicio);
        contraseña = findViewById(R.id.EntradaContraseñaInicio);
        iniciar = findViewById(R.id.button6);
        crear = findViewById(R.id.button7);

        if(fAuth.getCurrentUser() !=null){
            Toast.makeText(INICIARSESION.this, "Ya se ha iniciado sesión en este dispositvo", Toast.LENGTH_SHORT).show();
            startActivity((new Intent(getApplicationContext(), InicioActivity.class)));

            finish();
        }

        iniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser firebaseUser = fAuth.getCurrentUser();
                final String regex = "(?:[^<>()\\[\\].,;:\\s@\"]+(?:\\.[^<>()\\[\\].,;:\\s@\"]+)*|\"[^\\n\"]+\")@(?:[^<>()\\[\\].,;:\\s@\"]+\\.)+[^<>()\\[\\]\\.,;:\\s@\"]{2,63}";
                final String email = correo.getText().toString().trim();
                String password = contraseña.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(INICIARSESION.this, "Debes ingresar tu email", Toast.LENGTH_SHORT).show();
                    correo.setError("Llena el campo");
                    return;
                }
                if (!email.matches(regex)) {
                    Toast.makeText(INICIARSESION.this, "Formato de email incorrecro", Toast.LENGTH_SHORT).show();
                    correo.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(INICIARSESION.this, "Debes ingresar tu contraseña", Toast.LENGTH_SHORT).show();
                    contraseña.requestFocus();
                    return;
                }

                fAuth.signInWithEmailAndPassword(email, password).
                        addOnCompleteListener(INICIARSESION.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@androidx.annotation.NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    saveEmailUser(email);
                                    Toast.makeText(INICIARSESION.this, "Bienvenido", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(),InicioActivity.class);
                                    String easyPuzzle = email;
                                    intent.putExtra("epuzzle", easyPuzzle);
                                    startActivity(intent);

                                } else {
                                    Toast.makeText(INICIARSESION.this, "Correo y/o contraseña no valido", Toast.LENGTH_SHORT).show();
                                    correo.setText("");
                                    contraseña.setText("");
                                }

                            }
                        });



            }
        });


        crear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(INICIARSESION.this, Registrarse.class);

                startActivity(intent);
            }
        });


    }
    private void saveEmailUser(String email) {
        SharedPreferences sharedPref = getSharedPreferences("shared_preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("email",email);
        editor.commit();
    }
}
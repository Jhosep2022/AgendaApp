package com.example.agenda_online;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class Pantalla_De_Carga extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pantalla_de_carga);

        firebaseAuth = FirebaseAuth.getInstance();

        int Tiempo = 3000;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                VerificarUsuario();
            }
        }, Tiempo);
    }

    private void VerificarUsuario(){
        FirebaseAuth firebaseUser = FirebaseAuth.getInstance();

        if(firebaseUser == null){
            startActivity(new Intent(Pantalla_De_Carga.this, MainActivity.class));
            finish();
        }else {
            startActivity(new Intent(Pantalla_De_Carga.this, MenuPrincipal.class));
            finish();
        }
    }
}
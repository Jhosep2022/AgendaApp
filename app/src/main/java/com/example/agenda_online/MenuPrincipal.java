package com.example.agenda_online;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MenuPrincipal extends AppCompatActivity {

    Button BtnAgregar, BtnMisNotas, BtnImportantes, BtnContactos, BtnAcercaDe, BtnSalir;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    TextView NombresPrincipal, CorreoPrincipal;

    DatabaseReference Usuarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Agenda Online");
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        NombresPrincipal = findViewById(R.id.NombresPrincipal);
        CorreoPrincipal = findViewById(R.id.CorreoPrincipal);

        Usuarios = FirebaseDatabase.getInstance().getReference("Usuarios");

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        BtnAgregar = findViewById(R.id.BtnAgregar);
        BtnMisNotas = findViewById(R.id.BtnMisNotas);
        BtnImportantes = findViewById(R.id.BtnImportantes);
        BtnContactos = findViewById(R.id.BtnContactos);
        BtnAcercaDe = findViewById(R.id.BtnAcercaDe);
        BtnSalir = findViewById(R.id.BtnSalir);

        BtnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SalirAplicacion();
            }
        });

        BtnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MenuPrincipal.this, AgregarNota.class));
            }
        });

        BtnMisNotas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MenuPrincipal.this, VerNotas.class));
            }
        });

        BtnImportantes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MenuPrincipal.this, VerTareasImportantes.class));
            }
        });

        BtnAcercaDe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MenuPrincipal.this, AcercaDe.class));
            }
        });




        // Set onClickListeners for the other buttons here
    }

    @Override
    protected void onStart() {
        super.onStart();
        ComporbarInicioSesion();
    }

    private void ComporbarInicioSesion() {
        if (firebaseUser == null) {
            startActivity(new Intent(MenuPrincipal.this, MainActivity.class));
            finish();
        } else {
            CargaDeDatos();
        }
    }

    private void CargaDeDatos() {
        Usuarios.child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    NombresPrincipal.setVisibility(View.VISIBLE);
                    CorreoPrincipal.setVisibility(View.VISIBLE);

                    String nombres = "" + snapshot.child("nombre").getValue();
                    String correo = "" + snapshot.child("correo").getValue();

                    NombresPrincipal.setText(nombres);
                    CorreoPrincipal.setText(correo);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void SalirAplicacion() {
        firebaseAuth.signOut();
        startActivity(new Intent(MenuPrincipal.this, MainActivity.class));
        Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_SHORT).show();
    }
}

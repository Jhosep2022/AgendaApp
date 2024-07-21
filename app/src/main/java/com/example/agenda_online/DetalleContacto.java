package com.example.agenda_online;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.io.InputStream;

public class DetalleContacto extends AppCompatActivity {

    private ImageView avatarImageView;
    private TextView detalleNombreTextView, detalleApellidoTextView, detalleEmailTextView, detalleTelefonoTextView;
    private Button btnLlamar, btnMensaje;

    private DatabaseReference contactosReference;
    private String contactoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_contacto);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        avatarImageView = findViewById(R.id.avatarImageView);
        detalleNombreTextView = findViewById(R.id.detalleNombreTextView);
        detalleApellidoTextView = findViewById(R.id.detalleApellidoTextView);
        detalleEmailTextView = findViewById(R.id.detalleEmailTextView);
        detalleTelefonoTextView = findViewById(R.id.detalleTelefonoTextView);
        btnLlamar = findViewById(R.id.btnLlamar);
        btnMensaje = findViewById(R.id.btnMensaje);

        contactoId = getIntent().getStringExtra("contactoId");
        contactosReference = FirebaseDatabase.getInstance().getReference("Contactos");

        cargarDetallesContacto();

        btnLlamar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llamarContacto();
            }
        });

        btnMensaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviarMensaje();
            }
        });
    }

    private void cargarDetallesContacto() {
        contactosReference.child(contactoId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String nombre = snapshot.child("nombre").getValue(String.class);
                    String apellido = snapshot.child("apellido").getValue(String.class);
                    String email = snapshot.child("email").getValue(String.class);
                    String telefono = snapshot.child("telefono").getValue(String.class);
                    String genero = snapshot.child("genero").getValue(String.class);

                    detalleNombreTextView.setText("Nombre: " + nombre);
                    detalleApellidoTextView.setText("Apellido: " + apellido);
                    detalleEmailTextView.setText("Correo: " + email);
                    detalleTelefonoTextView.setText("Teléfono: " + telefono);

                    String imagenPath = genero.equalsIgnoreCase("femenino") ? "Mujer.jpg" : "Varon.jpg";
                    try {
                        InputStream inputStream = getAssets().open(imagenPath);
                        Drawable drawable = Drawable.createFromStream(inputStream, null);
                        avatarImageView.setImageDrawable(drawable);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });
    }

    private void llamarContacto() {
        String telefono = detalleTelefonoTextView.getText().toString().replace("Teléfono: ", "");
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + telefono));
        startActivity(intent);
    }

    private void enviarMensaje() {
        String telefono = detalleTelefonoTextView.getText().toString().replace("Teléfono: ", "");
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("sms:" + telefono));
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

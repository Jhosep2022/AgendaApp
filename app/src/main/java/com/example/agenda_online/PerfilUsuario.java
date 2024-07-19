package com.example.agenda_online;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.io.InputStream;

public class PerfilUsuario extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private ImageView avatarImageView;
    private TextView correoTextView, nombreTextView, apellidosTextView, edadTextView, domicilioTextView, universidadTextView, profesionTextView, telefonoTextView;
    private Button btnCambiarFoto;
    private Uri imageUri;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference usuariosReference;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_usuario);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        avatarImageView = findViewById(R.id.avatarImageView);
        correoTextView = findViewById(R.id.correoTextView);
        nombreTextView = findViewById(R.id.nombreTextView);
        btnCambiarFoto = findViewById(R.id.btnCambiarFoto);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        usuariosReference = FirebaseDatabase.getInstance().getReference("Usuarios");
        storageReference = FirebaseStorage.getInstance().getReference("Usuarios");

        cargarDetallesUsuario();

        btnCambiarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seleccionarImagen();
            }
        });
    }

    private void cargarDetallesUsuario() {
        if (firebaseUser != null) {
            usuariosReference.child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String correo = snapshot.child("correo").getValue(String.class);
                        String nombre = snapshot.child("nombre").getValue(String.class);
                        String apellidos = snapshot.child("apellidos").getValue(String.class);
                        String edad = snapshot.child("edad").getValue(String.class);
                        String domicilio = snapshot.child("domicilio").getValue(String.class);
                        String universidad = snapshot.child("universidad").getValue(String.class);
                        String profesion = snapshot.child("profesion").getValue(String.class);
                        String telefono = snapshot.child("telefono").getValue(String.class);
                        String imagenUrl = snapshot.child("imagenUrl").getValue(String.class);

                        correoTextView.setText(correo);
                        nombreTextView.setText(nombre);
                        apellidosTextView.setText(apellidos);
                        edadTextView.setText(edad);
                        domicilioTextView.setText(domicilio);
                        universidadTextView.setText(universidad);
                        profesionTextView.setText(profesion);
                        telefonoTextView.setText(telefono);

                        if (imagenUrl != null && !imagenUrl.isEmpty()) {
                            Glide.with(PerfilUsuario.this).load(imagenUrl).into(avatarImageView);
                        } else {
                            // Mostrar imagen por defecto si no hay URL
                            avatarImageView.setImageResource(R.drawable.ic_launcher_foreground);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle error
                }
            });
        }
    }

    private void seleccionarImagen() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            avatarImageView.setImageURI(imageUri);
            subirImagen();
        }
    }

    private void subirImagen() {
        if (imageUri != null) {
            StorageReference fileReference = storageReference.child(firebaseUser.getUid() + ".jpg");
            fileReference.putFile(imageUri).addOnSuccessListener(taskSnapshot -> fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                usuariosReference.child(firebaseUser.getUid()).child("imagenUrl").setValue(uri.toString());
                Toast.makeText(PerfilUsuario.this, "Imagen actualizada exitosamente", Toast.LENGTH_SHORT).show();
            })).addOnFailureListener(e -> {
                Toast.makeText(PerfilUsuario.this, "Error al subir la imagen", Toast.LENGTH_SHORT).show();
            });
        }
    }
}

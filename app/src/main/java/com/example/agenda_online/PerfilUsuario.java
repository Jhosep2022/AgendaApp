package com.example.agenda_online;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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

import java.io.ByteArrayOutputStream;

public class PerfilUsuario extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int TAKE_PHOTO_REQUEST = 2;
    private static final int CAMERA_PERMISSION_REQUEST = 100;

    private ImageView avatarImageView;
    private TextView correoTextView, nombreTextView, apellidosTextView, edadTextView, domicilioTextView, universidadTextView, profesionTextView, telefonoTextView;
    private Button btnCambiarFoto, btnTomarFoto;
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
        apellidosTextView = findViewById(R.id.apellidosTextView);
        edadTextView = findViewById(R.id.edadTextView);
        domicilioTextView = findViewById(R.id.domicilioTextView);
        universidadTextView = findViewById(R.id.universidadTextView);
        profesionTextView = findViewById(R.id.profesionTextView);
        telefonoTextView = findViewById(R.id.telefonoTextView);
        btnCambiarFoto = findViewById(R.id.btnCambiarFoto);
        btnTomarFoto = findViewById(R.id.btnTomarFoto);

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

        btnTomarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tomarFoto();
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

    private void tomarFoto() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST);
        } else {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, TAKE_PHOTO_REQUEST);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                tomarFoto();
            } else {
                Toast.makeText(this, "Permiso de cámara denegado", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == PICK_IMAGE_REQUEST && data.getData() != null) {
                imageUri = data.getData();
                avatarImageView.setImageURI(imageUri);
                subirImagen(imageUri);
            } else if (requestCode == TAKE_PHOTO_REQUEST && data.getExtras() != null) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                avatarImageView.setImageBitmap(photo);
                Uri tempUri = getImageUri(photo);
                subirImagen(tempUri);
            }
        }
    }

    private Uri getImageUri(Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Title", null);
        return Uri.parse(path);
    }

    private void subirImagen(Uri uri) {
        if (uri != null) {
            StorageReference fileReference = storageReference.child(firebaseUser.getUid() + ".jpg");
            fileReference.putFile(uri)
                    .addOnSuccessListener(taskSnapshot -> fileReference.getDownloadUrl()
                            .addOnSuccessListener(downloadUri -> {
                                usuariosReference.child(firebaseUser.getUid()).child("imagenUrl").setValue(downloadUri.toString());
                                Toast.makeText(PerfilUsuario.this, "Imagen actualizada exitosamente", Toast.LENGTH_SHORT).show();
                            }))
                    .addOnFailureListener(e -> {
                        Toast.makeText(PerfilUsuario.this, "Error al subir la imagen", Toast.LENGTH_SHORT).show();
                    });
        }
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

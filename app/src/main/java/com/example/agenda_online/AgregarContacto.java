package com.example.agenda_online;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AgregarContacto extends AppCompatActivity {

    private EditText nombreEditText, apellidoEditText, emailEditText, telefonoEditText;
    private Spinner generoSpinner;
    private Button guardarContactoButton;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference contactosReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_contacto);

        nombreEditText = findViewById(R.id.nombreEditText);
        apellidoEditText = findViewById(R.id.apellidoEditText);
        emailEditText = findViewById(R.id.emailEditText);
        telefonoEditText = findViewById(R.id.telefonoEditText);
        generoSpinner = findViewById(R.id.generoSpinner);
        guardarContactoButton = findViewById(R.id.guardarContactoButton);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.genero_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        generoSpinner.setAdapter(adapter);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        contactosReference = FirebaseDatabase.getInstance().getReference("Contactos");

        guardarContactoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agregarContacto();
            }
        });
    }

    private void agregarContacto() {
        String nombre = nombreEditText.getText().toString();
        String apellido = apellidoEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String telefono = telefonoEditText.getText().toString();
        String genero = generoSpinner.getSelectedItem().toString();

        if (nombre.isEmpty() || apellido.isEmpty() || email.isEmpty() || telefono.isEmpty() || genero.isEmpty()) {
            Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        String uidContacto = contactosReference.push().getKey();
        Contacto nuevoContacto = new Contacto(uidContacto, nombre, apellido, email, genero, firebaseUser.getUid(), telefono);

        contactosReference.child(uidContacto).setValue(nuevoContacto);

        Toast.makeText(this, "Contacto agregado exitosamente", Toast.LENGTH_SHORT).show();
        finish(); // Finaliza la actividad y regresa a la lista de contactos
    }
}

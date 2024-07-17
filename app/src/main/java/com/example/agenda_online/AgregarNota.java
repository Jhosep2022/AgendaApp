package com.example.agenda_online;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class AgregarNota extends AppCompatActivity {

    private TextView fechaTextView;
    private TextView fechaRegistro;
    private TextInputEditText tituloEditText, descripcionEditText;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference tareasReference;
    private String estado = "No finalizado"; // Estado por defecto

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_agregar_nota);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Agregar Nota");
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        fechaTextView = findViewById(R.id.fechaTextView);
        fechaRegistro = findViewById(R.id.fechaRegistro);
        tituloEditText = findViewById(R.id.tituloEditText);
        descripcionEditText = findViewById(R.id.descripcionEditText);

        Button calendarioButton = findViewById(R.id.calendarioButton);
        Button guardarTareaButton = findViewById(R.id.guardarTareaButton);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        tareasReference = FirebaseDatabase.getInstance().getReference("Tareas");

        // Set the current date
        setFechaActual();

        calendarioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDatePickerDialog();
            }
        });

        guardarTareaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarTarea();
            }
        });
    }

    private void setFechaActual() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy/HH:mm:ss a", Locale.getDefault());
        String currentDateandTime = sdf.format(Calendar.getInstance().getTime());
        fechaRegistro.setText(currentDateandTime);
    }

    private void mostrarDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                AgregarNota.this,
                (view, year1, month1, dayOfMonth) -> {
                    month1 = month1 + 1;
                    String date = dayOfMonth + "/" + month1 + "/" + year1;
                    fechaTextView.setText(date);
                },
                year, month, day);

        datePickerDialog.show();
    }

    private void guardarTarea() {
        String uidTarea = tareasReference.push().getKey();
        String fechaDeRegistro = fechaRegistro.getText().toString();
        String fechaDeCulminacion = fechaTextView.getText().toString();
        String titulo = tituloEditText.getText().toString().trim();
        String descripcion = descripcionEditText.getText().toString().trim();
        String uidUsuario = firebaseUser.getUid();

        if (titulo.isEmpty() || descripcion.isEmpty() || fechaDeCulminacion.equals("00/00/0000")) {
            Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        HashMap<String, Object> tareaMap = new HashMap<>();
        tareaMap.put("uidTarea", uidTarea);
        tareaMap.put("fechaDeRegistro", fechaDeRegistro);
        tareaMap.put("fechaDeCulminacion", fechaDeCulminacion);
        tareaMap.put("titulo", titulo);
        tareaMap.put("descripcion", descripcion);
        tareaMap.put("uidUsuario", uidUsuario);
        tareaMap.put("estado", estado);

        tareasReference.child(uidTarea).setValue(tareaMap)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(AgregarNota.this, "Tarea guardada exitosamente", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(AgregarNota.this, "Error al guardar la tarea: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
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

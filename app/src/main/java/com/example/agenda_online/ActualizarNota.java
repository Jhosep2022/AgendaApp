package com.example.agenda_online;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ActualizarNota extends AppCompatActivity {

    private TextView fechaRegistro;
    private TextInputEditText tituloEditText, descripcionEditText;
    private TextView fechaTextView;
    private Button calendarioButton, guardarTareaButton;
    private Nota nota;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_actualizar_nota);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Actualizar Nota");
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        nota = (Nota) getIntent().getSerializableExtra("nota");

        fechaRegistro = findViewById(R.id.fechaRegistro);
        tituloEditText = findViewById(R.id.tituloEditText);
        descripcionEditText = findViewById(R.id.descripcionEditText);
        fechaTextView = findViewById(R.id.fechaTextView);
        calendarioButton = findViewById(R.id.calendarioButton);
        guardarTareaButton = findViewById(R.id.guardarTareaButton);

        // Set the current values
        setFechaActual();
        tituloEditText.setText(nota.getTitulo());
        descripcionEditText.setText(nota.getDescripcion());
        fechaTextView.setText(nota.getFechaDeCulminacion());

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
                ActualizarNota.this,
                (view, year1, month1, dayOfMonth) -> {
                    month1 = month1 + 1;
                    String date = dayOfMonth + "/" + month1 + "/" + year1;
                    fechaTextView.setText(date);
                },
                year, month, day);

        datePickerDialog.show();
    }

    private void guardarTarea() {
        DatabaseReference notaRef = FirebaseDatabase.getInstance().getReference("Tareas").child(nota.getUidTarea());

        nota.setTitulo(tituloEditText.getText().toString());
        nota.setDescripcion(descripcionEditText.getText().toString());
        nota.setFechaDeCulminacion(fechaTextView.getText().toString());

        notaRef.setValue(nota);
        finish();
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

package com.example.agenda_online;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.IOException;
import java.io.InputStream;

public class AcercaDe extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_acerca_de);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mostrarDialogoAcercaDe();
    }

    private void mostrarDialogoAcercaDe() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_acerca_de, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();

        // Cargar la imagen desde assets
        ImageView imageView = dialogView.findViewById(R.id.imageView);
        try {
            InputStream inputStream = getAssets().open("about.jpg");
            Drawable drawable = Drawable.createFromStream(inputStream, null);
            imageView.setImageDrawable(drawable);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Button btnEntendido = dialogView.findViewById(R.id.btnEntendido);
        btnEntendido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                // Redirigir a la pantalla del menú
                Intent intent = new Intent(AcercaDe.this, MenuPrincipal.class);
                startActivity(intent);
                finish(); // Finaliza la actividad actual
            }
        });

        dialog.show();
    }
}

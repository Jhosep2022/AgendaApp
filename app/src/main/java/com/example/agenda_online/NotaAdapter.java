package com.example.agenda_online;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class NotaAdapter extends RecyclerView.Adapter<NotaAdapter.NotaViewHolder> {

    private List<Nota> notasList;
    private Context context;

    public NotaAdapter(List<Nota> notasList, Context context) {
        this.notasList = notasList;
        this.context = context;
    }

    @NonNull
    @Override
    public NotaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_nota, parent, false);
        return new NotaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotaViewHolder holder, int position) {
        Nota nota = notasList.get(position);
        holder.tituloNotaTextView.setText(nota.getTitulo());
        holder.descripcionNotaTextView.setText(nota.getDescripcion());
        holder.fechaCulminacionTextView.setText(nota.getFechaDeCulminacion());

        if ("Finalizado".equals(nota.getEstado())) {
            holder.estadoNotaTextView.setTextColor(ContextCompat.getColor(context, android.R.color.holo_green_dark));
        } else {
            holder.estadoNotaTextView.setTextColor(ContextCompat.getColor(context, android.R.color.holo_red_dark));
        }

        holder.estadoNotaTextView.setText(nota.getEstado());

        // Actualizar el ícono del corazón basado en el estado importante
        if (nota.isImportante()) {
            holder.importantButton.setImageResource(R.drawable.ic_heart_outline);  // Ícono de corazón lleno
        } else {
            holder.importantButton.setImageResource(R.drawable.ic_heart_filled);  // Ícono de corazón vacío
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarOpcionesDialog(nota, holder.getAdapterPosition());
            }
        });

        holder.importantButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleImportante(nota);
            }
        });
    }

    @Override
    public int getItemCount() {
        return notasList.size();
    }

    private void mostrarOpcionesDialog(Nota nota, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Seleccione una opción")
                .setItems(new String[]{"Finalizar", "Eliminar", "Actualizar"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                finalizarNota(nota);
                                break;
                            case 1:
                                eliminarNota(nota.getUidTarea(), position);
                                break;
                            case 2:
                                Intent intent = new Intent(context, ActualizarNota.class);
                                intent.putExtra("nota", nota);
                                context.startActivity(intent);
                                break;
                        }
                    }
                }).show();
    }

    private void finalizarNota(Nota nota) {
        DatabaseReference notaRef = FirebaseDatabase.getInstance().getReference("Tareas").child(nota.getUidTarea());
        nota.setEstado("Finalizado");
        notaRef.setValue(nota);
    }

    private void eliminarNota(String uidTarea, int position) {
        DatabaseReference notaRef = FirebaseDatabase.getInstance().getReference("Tareas").child(uidTarea);
        notaRef.removeValue();
        notasList.remove(position);
        notifyItemRemoved(position);
    }

    private void toggleImportante(Nota nota) {
        DatabaseReference notaRef = FirebaseDatabase.getInstance().getReference("Tareas").child(nota.getUidTarea());
        nota.setImportante(!nota.isImportante());
        notaRef.setValue(nota);
    }

    public static class NotaViewHolder extends RecyclerView.ViewHolder {

        TextView tituloNotaTextView, descripcionNotaTextView, fechaCulminacionTextView, estadoNotaTextView;
        ImageView importantButton;

        public NotaViewHolder(@NonNull View itemView) {
            super(itemView);
            tituloNotaTextView = itemView.findViewById(R.id.tituloNotaTextView);
            descripcionNotaTextView = itemView.findViewById(R.id.descripcionNotaTextView);
            fechaCulminacionTextView = itemView.findViewById(R.id.fechaCulminacionTextView);
            estadoNotaTextView = itemView.findViewById(R.id.estadoNotaTextView);
            importantButton = itemView.findViewById(R.id.importantButton);
        }
    }
}

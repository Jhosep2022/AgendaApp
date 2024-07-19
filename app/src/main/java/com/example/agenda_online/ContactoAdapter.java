package com.example.agenda_online;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class ContactoAdapter extends RecyclerView.Adapter<ContactoAdapter.ContactoViewHolder> {

    private List<Contacto> contactosList;
    private Context context;

    public ContactoAdapter(List<Contacto> contactosList, Context context) {
        this.contactosList = contactosList;
        this.context = context;
    }

    @NonNull
    @Override
    public ContactoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contacto, parent, false);
        return new ContactoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactoViewHolder holder, int position) {
        Contacto contacto = contactosList.get(position);
        holder.nombreTextView.setText(contacto.getNombre());
        holder.apellidoTextView.setText(contacto.getApellido());
        holder.emailTextView.setText(contacto.getEmail());
        holder.telefonoTextView.setText(contacto.getTelefono());

        String imagenPath = contacto.getGenero().equalsIgnoreCase("femenino") ? "Mujer.jpg" : "Varon.jpg";
        try {
            InputStream inputStream = context.getAssets().open(imagenPath);
            Drawable drawable = Drawable.createFromStream(inputStream, null);
            holder.avatarImageView.setImageDrawable(drawable);
        } catch (IOException e) {
            e.printStackTrace();
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context instanceof Contactos) {
                    ((Contactos) context).onContactoClick(contacto);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return contactosList.size();
    }

    static class ContactoViewHolder extends RecyclerView.ViewHolder {

        TextView nombreTextView, apellidoTextView, emailTextView, telefonoTextView;
        ImageView avatarImageView;

        public ContactoViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreTextView = itemView.findViewById(R.id.nombreTextView);
            apellidoTextView = itemView.findViewById(R.id.apellidoTextView);
            emailTextView = itemView.findViewById(R.id.emailTextView);
            telefonoTextView = itemView.findViewById(R.id.telefonoTextView);
            avatarImageView = itemView.findViewById(R.id.avatarImageView);
        }
    }
}

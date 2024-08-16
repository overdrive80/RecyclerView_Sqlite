package com.overdrive.recyclerview_bbdd.persona_recyclerview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.overdrive.recyclerview_bbdd.dto.Persona;
import com.overdrive.recyclerview_bbdd.R;
import java.util.List;

public class PersonaAdapter extends RecyclerView.Adapter<PersonaHolder> {

    private List<Persona> personas;
    private OnItemClickListener listener;

    public PersonaAdapter(List<Persona> personas, OnItemClickListener listener) {
        this.personas = personas;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PersonaHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_persona, parent, false);
        return new PersonaHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull PersonaHolder holder, int position) {
        holder.tvID.setText("ID: " + personas.get(position).getId());
        holder.tvNombre.setText("Nombre: " + personas.get(position).getNombre());
        holder.tvApellido.setText("Apellido: " + personas.get(position).getApellido());
    }

    @Override
    public int getItemCount() {
        return personas.size();
    }

    @Override
    public long getItemId(int position) {
        return personas.get(position).getId(); // suponiendo que Persona tiene un método getId()
    }

    public int getPosition(Persona persona) {
        for (int i = 0; i < personas.size(); i++) {
            if (getItemId(i) == persona.getId()) {
                return i;
            }
        }
        return -1; // no encontrado
    }

    public void setPersonas(List<Persona> personas) {
        this.personas = personas;
        notifyDataSetChanged();
    }

    public List<Persona> getPersonas() {
        return personas;
    }

    // CallBack Interface para que MainActivity o cualquier otra actividad implemente
    // el listener y pueda capturar el evento fuera del adaptador
    public interface OnItemClickListener {
        void onItemClick(View view,int position);
    }
}




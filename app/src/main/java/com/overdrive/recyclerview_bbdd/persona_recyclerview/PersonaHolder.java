package com.overdrive.recyclerview_bbdd.persona_recyclerview;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.overdrive.recyclerview_bbdd.R;

public class PersonaHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    // Definimos los campos del elemento de la lista del RecyclerView
    protected TextView tvID;
    protected TextView tvNombre;
    protected TextView tvApellido;
    private PersonaAdapter.OnItemClickListener clickListener;

    public PersonaHolder(@NonNull View itemView, PersonaAdapter.OnItemClickListener clickListener) {
        super(itemView);

        tvID = itemView.findViewById(R.id.tvID);
        tvNombre = itemView.findViewById(R.id.tvNombre);
        tvApellido = itemView.findViewById(R.id.tvTelefono);

        // Asignamos el listener
        itemView.setOnClickListener(this);
        this.clickListener = clickListener;

    }

    @Override
    public void onClick(View view) {
        //Propaga el evento hacía fuera, para capturarlo en cualquier parte de
        //nuestra aplicación
        clickListener.onItemClick(view, getLayoutPosition());

    }
}

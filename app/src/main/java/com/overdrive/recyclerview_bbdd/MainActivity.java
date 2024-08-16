package com.overdrive.recyclerview_bbdd;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.overdrive.recyclerview_bbdd.bbdd.BBDD;
import com.overdrive.recyclerview_bbdd.bbdd.PersonaDAO;
import com.overdrive.recyclerview_bbdd.dto.Persona;
import com.overdrive.recyclerview_bbdd.persona_recyclerview.PersonaAdapter;
import com.overdrive.recyclerview_bbdd.tools.TecladoTools;

import java.io.File;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PersonaAdapter.OnItemClickListener {

    private EditText etNombre, etApellido, etID;
    private Button btnInsertar, btnBorrar, btnBuscar, btnActualizar;
    private RecyclerView rvPersonas;
    private PersonaAdapter adapter;
    private PersonaDAO personaDAO;
    private static final String TAG = "BBDD";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Mapeo de componentes
        mapearComponentes();

        //Configuramos el RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvPersonas.setLayoutManager(layoutManager);

        //Inicializar la base de datos
        personaDAO = new PersonaDAO(this);
        eliminarBaseDeDatos();
        poblarBaseDeDatos();

        //Creamos el Adaptador y lo vinculamos al RecyclerView con los datos de la BBDD
        adapter = new PersonaAdapter(personaDAO.getPersonas(),this);
        rvPersonas.setAdapter(adapter);

        //Establecer listeners
        setListeners();
    }

    private void setListeners() {
        setListenerInsertar();
        setListenerBuscar();
        setListenerActualizar();
        setListenerBorrar();
    }

    private void setListenerBuscar() {
        btnBuscar.setOnClickListener(v -> {

            TecladoTools.ocultarTeclado(this);

            try {
                int id = Integer.parseInt(etID.getText().toString());
                Persona persona = personaDAO.buscarPersona(id);

                //Mostramos los valores en los campos del formulario
                etNombre.setText(persona.getNombre());
                etApellido.setText(persona.getApellido());

                // Hacemos scroll a la posición del objeto en el RecyclerView
                int pos = adapter.getPosition(persona);
                rvPersonas.scrollToPosition(pos);

            } catch (NumberFormatException e) {
                Toast.makeText(this, "El ID debe ser un número", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void setListenerBorrar() {
        btnBorrar.setOnClickListener(v -> {

            TecladoTools.ocultarTeclado(this);

            try {
                int id = Integer.parseInt(etID.getText().toString());
                Persona persona = personaDAO.buscarPersona(id);

                personaDAO.borrarPersona(persona);

                // Actualizar la lista a partir de la BBDD con los nuevos datos
                refrescarDatosRecyclerView();

                limpiarCampos();

            } catch (NumberFormatException e) {
                Toast.makeText(this, "El ID debe ser un número", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setListenerActualizar() {
        btnActualizar.setOnClickListener(v -> {
            TecladoTools.ocultarTeclado(this);

            try {
                int id = Integer.parseInt(etID.getText().toString());
                Persona persona = personaDAO.buscarPersona(id);

                persona.setNombre(etNombre.getText().toString());
                persona.setApellido(etApellido.getText().toString());

                //Actualizamos la persona en la BBDD
                personaDAO.actualizarPersona(persona);

                // Actualizar la lista a partir de la BBDD con los nuevos datos
                refrescarDatosRecyclerView();

                // Hacemos scroll a la posición del objeto en el RecyclerView
                int pos = adapter.getPosition(persona);
                rvPersonas.scrollToPosition(pos);

            } catch (NumberFormatException e) {
                Toast.makeText(this, "El ID debe ser un número", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            limpiarCampos();
        });
    }

    private void setListenerInsertar() {
        btnInsertar.setOnClickListener(v -> {
            TecladoTools.ocultarTeclado(this);

            try {
                int id = Integer.parseInt(etID.getText().toString());
                String nombre = etNombre.getText().toString();
                String apellidos = etApellido.getText().toString();

                Persona persona = new Persona(id, nombre, apellidos);
                personaDAO.insertarPersona(persona);

                // Actualizar la lista a partir de la BBDD con los nuevos datos
                refrescarDatosRecyclerView();

                // Hacemos scroll a la posición del objeto en el RecyclerView
                int pos = adapter.getPosition(persona);
                rvPersonas.scrollToPosition(pos);

                limpiarCampos();
            } catch (NumberFormatException e) {
                Toast.makeText(this, "El ID debe ser un número", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mapearComponentes() {
        etID = findViewById(R.id.etID);
        etNombre = findViewById(R.id.etNombre);
        etApellido = findViewById(R.id.etApellido);
        btnInsertar = findViewById(R.id.btnInsertar);
        btnBorrar = findViewById(R.id.btnBorrar);
        btnBuscar = findViewById(R.id.btnBuscar);
        btnActualizar = findViewById(R.id.btnActualizar);
        rvPersonas = findViewById(R.id.rvPersonas);
    }

    /**
     * ---------------------------- GESTIONES SOBRE LA BBDD ---------------------------
     **/
    private void poblarBaseDeDatos() {
        Persona p1 = new Persona(1, "Juan", "Pérez");
        Persona p2 = new Persona(2, "María", "López");
        Persona p3 = new Persona(3, "Pedro", "Gómez");
        Persona p4 = new Persona(4, "Ana", "Martínez");
        Persona p5 = new Persona(5, "Luis", "Fernández");
        Persona p6 = new Persona(6, "Laura", "Sánchez");

        personaDAO.insertarPersona(p1);
        personaDAO.insertarPersona(p2);
        personaDAO.insertarPersona(p3);
        personaDAO.insertarPersona(p4);
        personaDAO.insertarPersona(p5);
        personaDAO.insertarPersona(p6);
    }

    public void eliminarBaseDeDatos() {
        File database = getBaseDeDatos();

        if (database.exists() && database.delete()) {
            Log.d(TAG, "Base de datos eliminada");
        } else {
            Log.d(TAG, "No se pudo eliminar la base de datos");
        }
    }

    private File getBaseDeDatos() {
        return getApplicationContext().getDatabasePath(BBDD.NOMBRE_BBDD);
    }

    private void limpiarCampos() {
        etID.setText("");
        etNombre.setText("");
        etApellido.setText("");
    }

    private void refrescarDatosRecyclerView() {
        adapter.setPersonas(personaDAO.getPersonas());
    }

    /**
     * ---------------------------- METODOS SOBRESCRITOS ---------------------------
     **/

    @Override
    public void onItemClick(View view, int position) {

        List<Persona> personas = adapter.getPersonas();

        etID.setText(String.valueOf(personas.get(position).getId()));
        etNombre.setText(personas.get(position).getNombre());
        etApellido.setText(personas.get(position).getApellido());

        Toast.makeText(view.getContext(), "Has pulsado el elemento " + personas.get(position).getNombre(), Toast.LENGTH_SHORT).show();

        TecladoTools.ocultarTeclado(this);
    }

    @Override
    protected void onDestroy() {
        personaDAO.getConexion().close();
        super.onDestroy();
    }
}
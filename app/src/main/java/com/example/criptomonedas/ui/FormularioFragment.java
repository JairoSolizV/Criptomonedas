package com.example.criptomonedas.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.criptomonedas.R;
import com.example.criptomonedas.model.Item;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import androidx.lifecycle.ViewModelProvider;
import com.example.criptomonedas.ui.CriptoViewModel;


public class FormularioFragment extends Fragment {

    private TextInputEditText etNombre, etSimbolo, etPrecio;
    private MaterialButton btnGuardar;
    private Item itemEditando;
    private CriptoViewModel viewModel;
    

    public FormularioFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_formulario, container, false);

        etNombre = view.findViewById(R.id.etNombre);
        etSimbolo = view.findViewById(R.id.etSimbolo);
        etPrecio = view.findViewById(R.id.etPrecio);
        btnGuardar = view.findViewById(R.id.btnGuardar);

        viewModel = new ViewModelProvider(requireActivity()).get(CriptoViewModel.class);

        // Si hay un ítem para editar, cargar sus datos
        itemEditando = viewModel.getItemEditando();
        int posEditando = viewModel.getPosicionEditando();
        if (itemEditando != null && posEditando != -1) {
            etNombre.setText(itemEditando.getNombre());
            etSimbolo.setText(itemEditando.getSimbolo());
            etPrecio.setText(String.valueOf(itemEditando.getPrecio()));
        }

        setupGuardarButton();

        return view;
    }

    private void setupGuardarButton() {
        btnGuardar.setOnClickListener(v -> {
            String nombre = etNombre.getText().toString();
            String simbolo = etSimbolo.getText().toString();
            String precioStr = etPrecio.getText().toString();
            
            double precio;
            try {
                precio = Double.parseDouble(precioStr);
            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), "Precio inválido", Toast.LENGTH_SHORT).show();
                return;
            }

            Item nuevoItem = new Item(nombre, simbolo, precio, "https://example.com/" + simbolo + ".png");

            int posEditando = viewModel.getPosicionEditando();
            if (posEditando != -1) {
                // Actualizar
                viewModel.actualizarCripto(posEditando, nuevoItem);
                Toast.makeText(getContext(), "Criptomoneda actualizada", Toast.LENGTH_SHORT).show();
            } else {
                // Agregar
                viewModel.agregarCripto(nuevoItem);
                Toast.makeText(getContext(), "Criptomoneda agregada", Toast.LENGTH_SHORT).show();
            }
            viewModel.limpiarEdicion();
            limpiarFormulario();
        });
    }

    private void limpiarFormulario() {
        etNombre.setText("");
        etSimbolo.setText("");
        etPrecio.setText("");
    }
}

package com.example.criptomonedas.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.lifecycle.ViewModelProvider;

import com.example.criptomonedas.R;
import com.example.criptomonedas.adapter.ItemAdapter;
import com.example.criptomonedas.api.RetrofitService;
import com.example.criptomonedas.api.ApiClient;
import com.example.criptomonedas.model.CriptoResponse;
import com.example.criptomonedas.model.Item;
import com.example.criptomonedas.ui.CriptoViewModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListadoFragment extends Fragment {

    private RecyclerView recyclerView;
    private ItemAdapter adapter;
    private List<Item> listaOriginal = new ArrayList<>();
    private SearchView searchView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private CriptoViewModel viewModel;

    public ListadoFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_listado, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        searchView = view.findViewById(R.id.searchView);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ItemAdapter(getContext(), new ArrayList<>());
        recyclerView.setAdapter(adapter);

        // Click largo para eliminar
        adapter.setOnItemLongClickListener(position -> confirmarEliminacion(position));

        // Click en el botón de editar
        adapter.setOnEditClickListener(position -> {
            Item item = adapter.getItemList().get(position);
            viewModel.setItemEditando(item, position);
            requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.contenedorFragmentos, new FormularioFragment())
                .addToBackStack(null)
                .commit();
        });

        viewModel = new ViewModelProvider(requireActivity()).get(CriptoViewModel.class);
        viewModel.getListaCriptos().observe(getViewLifecycleOwner(), lista -> {
            adapter.setItems(lista);
        });

        // Solo cargar desde la API si la lista está vacía
        if (viewModel.getListaCriptos().getValue() == null || viewModel.getListaCriptos().getValue().isEmpty()) {
            cargarDatos();
        }

        swipeRefreshLayout.setOnRefreshListener(this::cargarDatos);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filtrar(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filtrar(newText);
                return true;
            }
        });

        return view;
    }

    private void cargarDatos() {
        swipeRefreshLayout.setRefreshing(true);
        RetrofitService apiService = ApiClient.getRetrofit().create(RetrofitService.class);
        apiService.getCriptomonedas().enqueue(new Callback<CriptoResponse>() {
            @Override
            public void onResponse(@NonNull Call<CriptoResponse> call, @NonNull Response<CriptoResponse> response) {
                swipeRefreshLayout.setRefreshing(false);
                if (response.isSuccessful() && response.body() != null) {
                    // Guardar la lista de la API en el ViewModel
                    viewModel.setListaCriptos(response.body().getCriptomonedas());
                }
            }

            @Override
            public void onFailure(@NonNull Call<CriptoResponse> call, @NonNull Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void filtrar(String texto) {
        List<Item> lista = viewModel.getListaCriptos().getValue();
        if (lista == null) return;
        List<Item> filtrados = new ArrayList<>();
        for (Item item : lista) {
            if (item.getNombre().toLowerCase().contains(texto.toLowerCase())
                || item.getSimbolo().toLowerCase().contains(texto.toLowerCase())) {
                filtrados.add(item);
            }
        }
        adapter.setItems(filtrados);
    }

    private void confirmarEliminacion(int position) {
        new android.app.AlertDialog.Builder(requireContext())
            .setTitle("Eliminar")
            .setMessage("¿Deseas eliminar esta criptomoneda?")
            .setPositiveButton("Sí", (dialog, which) -> {
                // Eliminar del ViewModel
                List<Item> lista = viewModel.getListaCriptos().getValue();
                if (lista != null && position >= 0 && position < lista.size()) {
                    lista.remove(position);
                    viewModel.setListaCriptos(new ArrayList<>(lista));
                }
            })
            .setNegativeButton("Cancelar", null)
            .show();
    }
}

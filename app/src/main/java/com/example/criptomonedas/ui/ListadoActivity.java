package com.example.criptomonedas.ui;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.criptomonedas.R;
import com.example.criptomonedas.adapter.ItemAdapter;
import com.example.criptomonedas.api.ApiClient;
import com.example.criptomonedas.api.RetrofitService;
import com.example.criptomonedas.model.CriptoResponse;
import com.example.criptomonedas.model.Item;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListadoActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ItemAdapter itemAdapter;
    private List<Item> listaCompleta = new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        itemAdapter = new ItemAdapter(this, null);
        recyclerView.setAdapter(itemAdapter);

        itemAdapter.setOnDeleteClickListener(position -> confirmarEliminacion(position));

        SearchView searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filtrarCriptomonedas(newText);
                return true;
            }
        });

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            cargarDatosDesdeAPI();
        });

        cargarDatosDesdeAPI();
    }


    private void filtrarCriptomonedas(String texto) {
        List<Item> filtrada = new ArrayList<>();
        for (Item item : listaCompleta) {
            if (item.getNombre().toLowerCase().contains(texto.toLowerCase()) ||
                item.getSimbolo().toLowerCase().contains(texto.toLowerCase())) {
                filtrada.add(item);
            }
        }
        itemAdapter.setItems(filtrada);
    }

    private void confirmarEliminacion(int position) {
        Log.d("Eliminar", "Se presionó el botón de eliminar en la posición: " + position); // Verifica si se llama
        new android.app.AlertDialog.Builder(this)
                .setTitle("Eliminar")
                .setMessage("¿Deseas eliminar esta criptomoneda?")
                .setPositiveButton("Sí", (dialog, which) -> {
                    // Obtener el elemento a eliminar
                    Item itemAEliminar = itemAdapter.getItemList().get(position);

                    // Eliminar de la lista completa
                    listaCompleta.remove(itemAEliminar);

                    // Eliminar de la lista filtrada y notificar al adaptador
                    itemAdapter.getItemList().remove(position);
                    itemAdapter.notifyItemRemoved(position);

                    Toast.makeText(this, "Elemento eliminado", Toast.LENGTH_SHORT).show(); // Confirmación visual
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }
    private void cargarDatosDesdeAPI() {
        RetrofitService service = ApiClient.getRetrofit().create(RetrofitService.class);
        Call<CriptoResponse> call = service.getCriptomonedas();

        call.enqueue(new Callback<CriptoResponse>() {
            @Override
            public void onResponse(Call<CriptoResponse> call, Response<CriptoResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listaCompleta = response.body().getCriptomonedas();
                    itemAdapter.setItems(listaCompleta);
                } else {
                    Toast.makeText(ListadoActivity.this, "Error al obtener datos", Toast.LENGTH_SHORT).show();
                }
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<CriptoResponse> call, Throwable t) {
                Log.e("API_ERROR", t.getMessage());
                Toast.makeText(ListadoActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }
}

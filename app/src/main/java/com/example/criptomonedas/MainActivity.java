package com.example.criptomonedas;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.criptomonedas.R;
import com.example.criptomonedas.ui.FormularioFragment;
import com.example.criptomonedas.ui.ListadoFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            if (item.getItemId() == R.id.nav_listado) {
                selectedFragment = new ListadoFragment();
            } else if (item.getItemId() == R.id.nav_formulario) {
                selectedFragment = new FormularioFragment();
            }

            if (selectedFragment != null) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.contenedorFragmentos, selectedFragment)
                        .commit();
                return true;
            }

            return false;
        });

        // Fragmento inicial por defecto
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.contenedorFragmentos, new ListadoFragment())
                    .commit();
        }
    }
}

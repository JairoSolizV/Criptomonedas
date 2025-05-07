package com.example.criptomonedas.ui;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.criptomonedas.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DetalleActivity extends AppCompatActivity {

    private TextView txtNombre;
    private LineChart lineChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle);

        txtNombre = findViewById(R.id.txtNombre);
        lineChart = findViewById(R.id.lineChart);

        // Recibir el nombre desde el intent
        String nombre = getIntent().getStringExtra("nombre");
        txtNombre.setText(nombre);

        mostrarGrafico();
    }

    private void mostrarGrafico() {
        List<Entry> entradas = new ArrayList<>();
        Random random = new Random();

        for (int i = 1; i <= 7; i++) {
            float precio = 1 + random.nextFloat() * 100;
            entradas.add(new Entry(i, precio));
        }

        LineDataSet dataSet = new LineDataSet(entradas, "Precio últimos 7 días");
        dataSet.setColor(getResources().getColor(R.color.purple_200));
        dataSet.setValueTextColor(getResources().getColor(R.color.white));
        dataSet.setLineWidth(2f);
        dataSet.setCircleRadius(4f);

        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);

        Description desc = new Description();
        desc.setText("");
        lineChart.setDescription(desc);
        lineChart.invalidate(); // Refrescar
    }
}
package com.example.viametroone;


import android.database.Cursor;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.*;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.*;

public class Admin extends AppCompatActivity {

    private BarChart barChartDestino, barChartOrigem;
    private BancoHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        barChartDestino = findViewById(R.id.barChartDestino);
        barChartOrigem = findViewById(R.id.barChartOrigem);
        dbHelper = new BancoHelper(this);

        mostrarGrafico("destino", barChartDestino, "Destinos");
        mostrarGrafico("origem", barChartOrigem, "Origens");
    }

    private void mostrarGrafico(String coluna, BarChart barChart, String label) {
        Cursor cursor = dbHelper.listarPesquisas();
        HashMap<String, Integer> contagem = new HashMap<>();

        if (cursor.moveToFirst()) {
            do {
                String valor = cursor.getString(cursor.getColumnIndexOrThrow(coluna));
                contagem.put(valor, contagem.getOrDefault(valor, 0) + 1);
            } while (cursor.moveToNext());
        }
        cursor.close();

        List<BarEntry> entries = new ArrayList<>();
        List<String> labels = new ArrayList<>();
        int index = 0;

        for (Map.Entry<String, Integer> entrada : contagem.entrySet()) {
            entries.add(new BarEntry(index, entrada.getValue()));
            labels.add(entrada.getKey());
            index++;
        }

        BarDataSet dataSet = new BarDataSet(entries, label);
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        BarData data = new BarData(dataSet);
        data.setBarWidth(0.9f);

        barChart.setData(data);
        barChart.setFitBars(true);
        barChart.getDescription().setEnabled(false);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));

        xAxis.setGranularity(1f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setLabelRotationAngle(45f);

        barChart.invalidate(); // Atualiza o gráfico
    }
}

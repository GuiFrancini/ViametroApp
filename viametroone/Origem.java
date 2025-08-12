package com.example.viametroone;

import android.os.Bundle;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.util.*;
import android.content.Intent;
import android.widget.Toast;


public class Origem extends AppCompatActivity {

    LinearLayout buttonContainer;
    TextView selectedText;
    Button btnAvancar;
    Button selectedButton = null;

    String[] amarelos = {
            "Luz", "República", "Higienópolis-Mackenzie", "Paulista", "Oscar Freire", "Fradique Coutinho",
            "Faria Lima", "Pinheiros", "Butantã", "São Paulo-Morumbi", "Vila Sônia"
    };

    String[] verdes = {
            "Vila Madalena", "Sumare", "Clinicas", "Paulista", "Trianon Masp", "Brigadeiro", "Paraiso",
            "Ana Rosa", "Chácara Klabin", "Santos Imigrantes", "Alto do Ipiranga",
            "Sacoma", "Tamanduatei", "Vila Prudente"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_origem);

        buttonContainer = findViewById(R.id.buttonContainer);
        selectedText = findViewById(R.id.selectedText);
        btnAvancar = findViewById(R.id.btnAvancar);

        List<String> allButtons = new ArrayList<>();
        Map<String, String> colorMap = new HashMap<>();

        for (String name : amarelos) {
            allButtons.add(name);
            colorMap.put(name, "#FFD700"); // amarelo
        }

        for (String name : verdes) {
            allButtons.add(name);
            colorMap.put(name, "#32CD32"); // verde
        }

        for (int i = 0; i < allButtons.size(); i += 3) {
            LinearLayout row = new LinearLayout(this);
            row.setOrientation(LinearLayout.HORIZONTAL);
            row.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            row.setPadding(0, 8, 0, 8);

            for (int j = i; j < i + 3 && j < allButtons.size(); j++) {
                final String name = allButtons.get(j);
                Button btn = new Button(this);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0,
                        LinearLayout.LayoutParams.WRAP_CONTENT, 1);
                params.setMargins(8, 0, 8, 0);
                btn.setLayoutParams(params);
                btn.setText(name);
                btn.setBackground(getRoundedDrawable(colorMap.get(name)));

                btn.setOnClickListener(view -> {
                    if (selectedButton != null) {
                        String originalColor = colorMap.get(selectedButton.getText().toString());
                        selectedButton.setBackground(getRoundedDrawable(originalColor));
                    }
                    selectedButton = btn;
                    btn.setBackground(getRoundedDrawable("#2196F3")); // azul
                    selectedText.setText("Selecionado: " + name);
                });

                row.addView(btn);
            }

            buttonContainer.addView(row);

            btnAvancar.setOnClickListener(view -> {
                if (selectedButton != null) {
                    String nomeSelecionado = selectedButton.getText().toString();

                    // Salvar no banco
                    BancoHelper db = new BancoHelper(Origem.this);
                    db.salvarOrigem(nomeSelecionado);

                    // Enviar para próxima tela
                    Intent intent = new Intent(Origem.this, Destino.class);
                    intent.putExtra("origem", nomeSelecionado);
                    startActivity(intent);
                } else {
                    Toast.makeText(Origem.this, "Selecione uma estação primeiro!", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    private GradientDrawable getRoundedDrawable(String colorHex) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(android.graphics.Color.parseColor(colorHex));
        drawable.setCornerRadius(32);
        drawable.setStroke(2, android.graphics.Color.DKGRAY);
        return drawable;
    }
}

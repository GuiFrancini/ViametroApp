package com.example.viametroone;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Cadastro extends AppCompatActivity {
    private EditText edtNome, edtTelefone, edtCidade;
    private TextView txtDataHora, txtEndereco;
    private Button btnConcluir;
    private FusedLocationProviderClient fusedLocationClient;

    private static final int REQUEST_LOCATION_PERMISSION = 1;

    private String localizacaoFormatada = "Localização não encontrada";
    private String dataHoraAtual = "";

    private BancoHelper dbHelper;

    private String origemSelecionada = "";
    private String destinoSelecionado = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        edtNome = findViewById(R.id.edtNome);
        edtTelefone = findViewById(R.id.edtTelefone);
        edtCidade = findViewById(R.id.edtCidade);
        txtDataHora = findViewById(R.id.txtDataHora);
        txtEndereco = findViewById(R.id.txtEndereco);
        btnConcluir = findViewById(R.id.btnConcluir);

        dbHelper = new BancoHelper(this);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        dataHoraAtual = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date());
        txtDataHora.setText("Data e Hora: " + dataHoraAtual);

        // Recuperar origem e destino da Intent
        Intent intent = getIntent();
        origemSelecionada = intent.getStringExtra("origem");
        destinoSelecionado = intent.getStringExtra("destino");

        // Permissão de localização
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        } else {
            pegarLocalizacao();
        }

        btnConcluir.setOnClickListener(v -> {
            String nome = edtNome.getText().toString().trim();
            String telefone = edtTelefone.getText().toString().trim();
            String cidade = edtCidade.getText().toString().trim();

            if (nome.isEmpty() || telefone.isEmpty() || cidade.isEmpty()) {
                Toast.makeText(Cadastro.this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean sucesso = dbHelper.inserirPesquisa(nome, telefone, cidade, dataHoraAtual, localizacaoFormatada, origemSelecionada, destinoSelecionado);

            if (sucesso) {
                Toast.makeText(Cadastro.this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show();
                Intent intentRedireciona = new Intent(Cadastro.this, Redirecionamento.class);
                startActivity(intentRedireciona);
                finish();
            } else {
                Toast.makeText(Cadastro.this, "Erro ao salvar no banco de dados", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void pegarLocalizacao() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        double lat = location.getLatitude();
                        double lon = location.getLongitude();
                        localizacaoFormatada = "Lat: " + lat + " | Lon: " + lon;
                        txtEndereco.setText(localizacaoFormatada);
                    } else {
                        Toast.makeText(this, "Localização não encontrada", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                pegarLocalizacao();
            } else {
                Toast.makeText(this, "Permissão de localização negada", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

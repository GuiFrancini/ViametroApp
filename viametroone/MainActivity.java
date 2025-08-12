package com.example.viametroone;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private EditText edtUsuario, edtSenha;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);


        //pega os dados do xml
        edtUsuario = findViewById(R.id.edtUsuario);
        edtSenha = findViewById(R.id.edtSenha);
        btnLogin = findViewById(R.id.btnLogin);

        //click do botao login
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usuario = edtUsuario.getText().toString();
                String senha = edtSenha.getText().toString();

                if (usuario.equals("admin") && senha.equals("123")) {
                    Intent intent = new Intent(MainActivity.this, Admin.class);
                    startActivity(intent);
                    finish();
                } else if (usuario.equals("pesquisa") && senha.equals("123")) {
                    Intent intent = new Intent(MainActivity.this, Origem.class);
                    startActivity(intent);
                    finish();
                } else {
                    //envia mesnagem de toast com a mensagemm
                    Toast.makeText(MainActivity.this, "Usuário ou senha incorretos!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
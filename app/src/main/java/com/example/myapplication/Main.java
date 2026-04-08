

package com.example.myapplication;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
public class Main extends AppCompatActivity {

    TextView tvNome, tvMatricula, tvLotacao, tvFuncao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        tvNome = findViewById(R.id.tvNome);
        tvMatricula = findViewById(R.id.tvMatricula);
        tvLotacao = findViewById(R.id.tvLotacao);
        tvFuncao = findViewById(R.id.tvFuncao);

        User u = (User) getIntent().getSerializableExtra("usuario");

        if (u != null) {
            tvNome.setText("Nome: " + u.nomeCompleto);
            tvMatricula.setText("Matrícula: " + u.matricula);
            tvLotacao.setText("Lotação: " + u.lotacao);
            tvFuncao.setText("Função: " + u.funcao);
        }
    }
}

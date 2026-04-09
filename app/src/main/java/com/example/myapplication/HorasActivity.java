package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class HorasActivity extends AppCompatActivity {

    private LinearLayout llHorarios;
    private TextView tvPreenchidas, tvFaltantes;
    private TextView tvNome, tvMatricula, tvLotacao, tvFuncao;
    private Button btnCompartilhar;

    private User usuario;
    private Map<String, String> horasPreenchidas = new HashMap<>();
    private List<String> horariosDisponiveis = new ArrayList<>();
    private String dataSalva;

    private static final String PREFS_NAME = "HorasPrefs";
    private static final String KEY_DATA = "data_atual";
    private static final String KEY_USER_PREFIX = "user_";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.horas);

        llHorarios = findViewById(R.id.llHorarios);
        tvPreenchidas = findViewById(R.id.tvPreenchidas);
        tvFaltantes = findViewById(R.id.tvFaltantes);
        btnCompartilhar = findViewById(R.id.btnCompartilhar);

        tvNome = findViewById(R.id.tvNome);
        tvMatricula = findViewById(R.id.tvMatricula);
        tvLotacao = findViewById(R.id.tvLotacao);
        tvFuncao = findViewById(R.id.tvFuncao);

        usuario = (User) getIntent().getSerializableExtra("usuario");

        if (usuario != null) {
            tvNome.setText(usuario.nomeCompleto);
            tvMatricula.setText(usuario.matricula);
            tvLotacao.setText(usuario.lotacao);
            tvFuncao.setText(usuario.funcao);
        }

        btnCompartilhar.setOnClickListener(v -> compartilhar());
    }

    @Override
    protected void onResume() {
        super.onResume();
        verificarEAtualizarTela();
    }

    private void verificarEAtualizarTela() {
        String dataAtual = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        dataSalva = prefs.getString(KEY_DATA, "");

        if (!dataAtual.equals(dataSalva)) {
            // Se mudou o dia, limpa tudo (Requisito 6)
            prefs.edit().clear().putString(KEY_DATA, dataAtual).apply();
            horasPreenchidas.clear();
        } else {
            carregarDados();
        }

        atualizarCamposDinamicos();
    }

    private void carregarDados() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        horasPreenchidas.clear();
        String[] todosHorarios = {"08:00 - 09:00", "09:00 - 10:00", "10:00 - 11:00", "11:00 - 12:00",
                                   "13:00 - 14:00", "14:00 - 15:00", "15:00 - 16:00", "16:00 - 17:00"};
        
        for (String h : todosHorarios) {
            String valor = prefs.getString(KEY_USER_PREFIX + usuario.username + "_" + h, "");
            if (!valor.isEmpty()) {
                horasPreenchidas.put(h, valor);
            }
        }
    }

    private void atualizarCamposDinamicos() {
        llHorarios.removeAllViews();
        horariosDisponiveis.clear();

        Calendar now = Calendar.getInstance();
        int hour = now.get(Calendar.HOUR_OF_DAY);

        // Adiciona campos conforme a hora atual (Requisito 3)
        adicionarSeDisponivel("08:00 - 09:00", 9, hour);
        adicionarSeDisponivel("09:00 - 10:00", 10, hour);
        adicionarSeDisponivel("10:00 - 11:00", 11, hour);
        adicionarSeDisponivel("11:00 - 12:00", 12, hour);
        adicionarSeDisponivel("13:00 - 14:00", 14, hour);
        adicionarSeDisponivel("14:00 - 15:00", 15, hour);
        adicionarSeDisponivel("15:00 - 16:00", 16, hour);
        adicionarSeDisponivel("16:00 - 17:00", 17, hour);
        
        // Se for após às 17h, todos os campos ficam visíveis até meia-noite
        if (hour >= 17) {
            // Já foram adicionados acima pela lógica do >=
        }

        if (horariosDisponiveis.isEmpty()) {
            TextView tvAviso = new TextView(this);
            tvAviso.setText("Nenhum horário disponível para lançamento no momento.\n(Os campos aparecem após a hora concluída)");
            tvAviso.setPadding(20, 50, 20, 0);
            tvAviso.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            llHorarios.addView(tvAviso);
        }

        for (String horario : horariosDisponiveis) {
            // Título do Horário
            TextView tvLabel = new TextView(this);
            tvLabel.setText("Horário: " + horario);
            tvLabel.setTextSize(16);
            tvLabel.setTextColor(Color.BLACK);
            tvLabel.setPadding(0, 30, 0, 10);
            
            // Campo de Digitação
            EditText etAtividade = new EditText(this);
            etAtividade.setHint("Descreva o que foi feito nesta hora...");
            etAtividade.setMinLines(2);
            
            String valorSalvo = horasPreenchidas.get(horario);
            if (valorSalvo != null) {
                etAtividade.setText(valorSalvo);
            }

            etAtividade.addTextChangedListener(new TextWatcher() {
                @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
                @Override public void afterTextChanged(Editable s) {
                    horasPreenchidas.put(horario, s.toString());
                    salvarDado(horario, s.toString());
                    atualizarContagem(); // Requisito 4: Atualiza contagem em tempo real
                }
            });

            llHorarios.addView(tvLabel);
            llHorarios.addView(etAtividade);
        }

        atualizarContagem();
    }

    private void adicionarSeDisponivel(String label, int horaNecessaria, int horaAtual) {
        if (horaAtual >= horaNecessaria) {
            horariosDisponiveis.add(label);
        }
    }

    private void salvarDado(String horario, String valor) {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        prefs.edit().putString(KEY_USER_PREFIX + usuario.username + "_" + horario, valor).apply();
    }

    private void atualizarContagem() {
        int preenchidas = 0;
        for (String horario : horariosDisponiveis) {
            String texto = horasPreenchidas.get(horario);
            if (texto != null && !texto.trim().isEmpty()) {
                preenchidas++;
            }
        }
        int faltantes = horariosDisponiveis.size() - preenchidas;

        tvPreenchidas.setText(String.format(Locale.getDefault(), "%d horas de trabalho já estão preenchidas.", preenchidas));
        tvFaltantes.setText(String.format(Locale.getDefault(), "%d horas de trabalho ainda não foram registradas.", faltantes));
    }

    private void compartilhar() {
        StringBuilder sb = new StringBuilder();
        String dataAtual = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        sb.append("RELATÓRIO DE HORAS - ").append(dataAtual).append("\n");
        sb.append("Funcionário: ").append(usuario.nomeCompleto).append(" (").append(usuario.matricula).append(")\n");
        sb.append("Lotação: ").append(usuario.lotacao).append(" | Função: ").append(usuario.funcao).append("\n\n");

        boolean temConteudo = false;
        for (String horario : horariosDisponiveis) {
            String atividade = horasPreenchidas.get(horario);
            if (atividade != null && !atividade.trim().isEmpty()) {
                sb.append("[").append(horario).append("]: ").append(atividade).append("\n");
                temConteudo = true;
            }
        }

        if (!temConteudo) {
            Toast.makeText(this, "Preencha ao menos uma hora para compartilhar.", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, sb.toString());
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, "Enviar para..."));
    }
}

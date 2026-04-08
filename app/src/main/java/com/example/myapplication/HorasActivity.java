package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
    private Button btnCompartilhar;

    private User usuario;
    private Map<String, String> horasPreenchidas;
    private List<String> horariosDisponiveis;
    private String dataAtual;

}
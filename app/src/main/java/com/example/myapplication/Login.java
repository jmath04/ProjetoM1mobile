package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class Login extends AppCompatActivity {

    EditText etUser, etSenha;
    Button btnLogin;

    List<User> usuarios = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        etUser = findViewById(R.id.etUser);
        etSenha = findViewById(R.id.etSenha);
        btnLogin = findViewById(R.id.btnLogin);

        // usuários fixos
        usuarios.add(new User("Joao", "123", "João Matheus", "001", "TI", "Dev"));
        usuarios.add(new User("Scheila", "456", "Scheila Ceresa", "002", "TI", "Analista"));

        btnLogin.setOnClickListener(v -> login());
    }

    private void login() {
        String user = etUser.getText().toString();
        String senha = etSenha.getText().toString();

        for (User u : usuarios) {
            if (u.username.equals(user) && u.senha.equals(senha)) {

                Intent i = new Intent(this, Main.class);
                i.putExtra("usuario", u);
                startActivity(i);
                return;
            }
        }

        Toast.makeText(this, "Login inválido", Toast.LENGTH_SHORT).show();
    }
}

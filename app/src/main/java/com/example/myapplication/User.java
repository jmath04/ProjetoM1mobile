package com.example.myapplication;

import java.io.Serializable;

public class User implements Serializable {

    public String username;
    public String senha;
    public String nomeCompleto;
    public String matricula;
    public String lotacao;
    public String funcao;

    public User(String username, String senha, String nomeCompleto,
                String matricula, String lotacao, String funcao) {
        this.username = username;
        this.senha = senha;
        this.nomeCompleto = nomeCompleto;
        this.matricula = matricula;
        this.lotacao = lotacao;
        this.funcao = funcao;
    }
}

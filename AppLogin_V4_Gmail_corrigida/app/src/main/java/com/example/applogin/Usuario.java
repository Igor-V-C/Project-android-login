package com.example.applogin;

public class Usuario {
    private long id;
    private String usuario;
    private String senhaHash;
    private String email;
    private String telefone;
    private String nomeCompleto;
    private String dataNascimento;
    private String endereco;
    private String cidade;
    private String estado;
    private String fotoPath;

    public Usuario() {}

    public Usuario(long id, String usuario, String senhaHash, String email, String telefone,
                   String nomeCompleto, String dataNascimento, String endereco, String cidade,
                   String estado, String fotoPath) {
        this.id = id;
        this.usuario = usuario;
        this.senhaHash = senhaHash;
        this.email = email;
        this.telefone = telefone;
        this.nomeCompleto = nomeCompleto;
        this.dataNascimento = dataNascimento;
        this.endereco = endereco;
        this.cidade = cidade;
        this.estado = estado;
        this.fotoPath = fotoPath;
    }

    public long getId() { return id; }
    public String getUsuario() { return usuario; }
    public String getSenhaHash() { return senhaHash; }
    public String getEmail() { return email; }
    public String getTelefone() { return telefone; }
    public String getNomeCompleto() { return nomeCompleto; }
    public String getDataNascimento() { return dataNascimento; }
    public String getEndereco() { return endereco; }
    public String getCidade() { return cidade; }
    public String getEstado() { return estado; }
    public String getFotoPath() { return fotoPath; }

    public void setId(long id) { this.id = id; }
    public void setUsuario(String usuario) { this.usuario = usuario; }
    public void setSenhaHash(String senhaHash) { this.senhaHash = senhaHash; }
    public void setEmail(String email) { this.email = email; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public void setNomeCompleto(String nomeCompleto) { this.nomeCompleto = nomeCompleto; }
    public void setDataNascimento(String dataNascimento) { this.dataNascimento = dataNascimento; }
    public void setEndereco(String endereco) { this.endereco = endereco; }
    public void setCidade(String cidade) { this.cidade = cidade; }
    public void setEstado(String estado) { this.estado = estado; }
    public void setFotoPath(String fotoPath) { this.fotoPath = fotoPath; }
}

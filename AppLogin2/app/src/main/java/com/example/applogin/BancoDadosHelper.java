package com.example.applogin;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BancoDadosHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "app.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_USUARIOS = "usuarios";

    public BancoDadosHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql = "CREATE TABLE " + TABLE_USUARIOS + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "usuario TEXT NOT NULL UNIQUE, " +
                "senha_hash TEXT NOT NULL, " +
                "email TEXT NOT NULL UNIQUE, " +
                "telefone TEXT NOT NULL, " +
                "nome_completo TEXT NOT NULL, " +
                "data_nascimento TEXT, " +
                "endereco TEXT, " +
                "cidade TEXT, " +
                "estado TEXT" +
                ")";

        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Como estamos na versão 1, não há migração ainda.
        //
        // Quando você alterar a estrutura futuramente,
        // aumente DATABASE_VERSION e faça uma migração
        // utilizando ALTER TABLE, preservando os dados.
    }

    public long inserirUsuario(Usuario usuario) {

        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("usuario", usuario.getUsuario());
        values.put("senha_hash", usuario.getSenhaHash());
        values.put("email", usuario.getEmail());
        values.put("telefone", usuario.getTelefone());
        values.put("nome_completo", usuario.getNomeCompleto());
        values.put("data_nascimento", usuario.getDataNascimento());
        values.put("endereco", usuario.getEndereco());
        values.put("cidade", usuario.getCidade());
        values.put("estado", usuario.getEstado());

        try {
            return db.insertOrThrow(TABLE_USUARIOS, null, values);
        } catch (SQLiteConstraintException e) {
            return -1;
        }
    }

    public Usuario buscarPorUsuario(String usuario) {

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_USUARIOS,
                null,
                "usuario = ?",
                new String[]{usuario},
                null,
                null,
                null
        );

        try {

            if (cursor.moveToFirst()) {

                return new Usuario(
                        cursor.getLong(cursor.getColumnIndexOrThrow("id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("usuario")),
                        cursor.getString(cursor.getColumnIndexOrThrow("senha_hash")),
                        cursor.getString(cursor.getColumnIndexOrThrow("email")),
                        cursor.getString(cursor.getColumnIndexOrThrow("telefone")),
                        cursor.getString(cursor.getColumnIndexOrThrow("nome_completo")),
                        cursor.getString(cursor.getColumnIndexOrThrow("data_nascimento")),
                        cursor.getString(cursor.getColumnIndexOrThrow("endereco")),
                        cursor.getString(cursor.getColumnIndexOrThrow("cidade")),
                        cursor.getString(cursor.getColumnIndexOrThrow("estado"))
                );
            }

        } finally {
            cursor.close();
        }

        return null;
    }

    public Usuario buscarPorId(long id) {

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_USUARIOS,
                null,
                "id = ?",
                new String[]{String.valueOf(id)},
                null,
                null,
                null
        );

        try {

            if (cursor.moveToFirst()) {

                return new Usuario(
                        cursor.getLong(cursor.getColumnIndexOrThrow("id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("usuario")),
                        cursor.getString(cursor.getColumnIndexOrThrow("senha_hash")),
                        cursor.getString(cursor.getColumnIndexOrThrow("email")),
                        cursor.getString(cursor.getColumnIndexOrThrow("telefone")),
                        cursor.getString(cursor.getColumnIndexOrThrow("nome_completo")),
                        cursor.getString(cursor.getColumnIndexOrThrow("data_nascimento")),
                        cursor.getString(cursor.getColumnIndexOrThrow("endereco")),
                        cursor.getString(cursor.getColumnIndexOrThrow("cidade")),
                        cursor.getString(cursor.getColumnIndexOrThrow("estado"))
                );
            }

        } finally {
            cursor.close();
        }

        return null;
    }
}

package com.example.applogin;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BancoDadosHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "app.db";
    private static final int DATABASE_VERSION = 2;
    public static final String TABLE_USUARIOS = "usuarios";

    public BancoDadosHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_USUARIOS + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "usuario TEXT NOT NULL UNIQUE, " +
                "senha_hash TEXT NOT NULL, " +
                "email TEXT NOT NULL UNIQUE, " +
                "telefone TEXT NOT NULL, " +
                "nome_completo TEXT NOT NULL, " +
                "data_nascimento TEXT, " +
                "endereco TEXT, " +
                "cidade TEXT, " +
                "estado TEXT, " +
                "fotoPath TEXT" +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE usuarios ADD COLUMN fotoPath TEXT");
        }
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
        values.put("fotoPath", usuario.getFotoPath());
        try {
            return db.insertOrThrow(TABLE_USUARIOS, null, values);
        } catch (SQLiteConstraintException e) {
            return -1;
        }
    }

    public Usuario buscarPorUsuario(String usuario) {
        return buscar("usuario = ?", new String[]{usuario});
    }

    public Usuario buscarPorEmail(String email) {
        return buscar("email = ?", new String[]{email});
    }

    public Usuario buscarPorId(long id) {
        return buscar("id = ?", new String[]{String.valueOf(id)});
    }

    private Usuario buscar(String selecao, String[] args) {
        Cursor cursor = getReadableDatabase().query(TABLE_USUARIOS, null, selecao, args, null, null, null);
        try {
            if (cursor.moveToFirst()) return cursorParaUsuario(cursor);
            return null;
        } finally {
            cursor.close();
        }
    }

    private Usuario cursorParaUsuario(Cursor cursor) {
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
                cursor.getString(cursor.getColumnIndexOrThrow("estado")),
                cursor.getString(cursor.getColumnIndexOrThrow("fotoPath"))
        );
    }
}

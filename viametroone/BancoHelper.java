package com.example.viametroone;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BancoHelper extends SQLiteOpenHelper {

    private static final String NOME_BANCO = "viametro.db";
    private static final int VERSAO_BANCO = 1;

    public static final String TABELA_PESQUISA = "pesquisa";

    public BancoHelper(Context context) {
        super(context, NOME_BANCO, null, VERSAO_BANCO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABELA_PESQUISA + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "origem TEXT, " +
                "destino TEXT, " +
                "nome TEXT, " +
                "telefone TEXT, " +
                "cidade TEXT, " +
                "datahora TEXT, " +
                "latitude TEXT, " +
                "longitude TEXT" +
                ");";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABELA_PESQUISA);
        onCreate(db);
    }


    public boolean salvarPesquisa(String origem, String destino, String nome,
                                  String telefone, String cidade, String datahora,
                                  String latitude, String longitude) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valores = new ContentValues();

        valores.put("origem", origem);
        valores.put("destino", destino);
        valores.put("nome", nome);
        valores.put("telefone", telefone);
        valores.put("cidade", cidade);
        valores.put("datahora", datahora);
        valores.put("latitude", latitude);
        valores.put("longitude", longitude);

        long resultado = db.insert(TABELA_PESQUISA, null, valores);
        db.close();

        return resultado != -1;
    }


    public Cursor listarPesquisas() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABELA_PESQUISA, null);
    }


    // Insere todos os dados de uma vez
    public boolean inserirPesquisa(String nome, String telefone, String cidade,
                                   String dataHora, String localizacao,
                                   String origem, String destino) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valores = new ContentValues();

        valores.put("nome", nome);
        valores.put("telefone", telefone);
        valores.put("cidade", cidade);
        valores.put("datahora", dataHora);
        valores.put("latitude", localizacao.split(",")[0]);
        valores.put("longitude", localizacao.split(",")[1]);
        valores.put("origem", origem);
        valores.put("destino", destino);

        long resultado = db.insert(TABELA_PESQUISA, null, valores);
        db.close();

        return resultado != -1;
    }

    // Atualiza o destino de uma entrada com base na origem
    public boolean atualizarDestino(String origem, String novoDestino) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put("destino", novoDestino);

        int linhasAfetadas = db.update(
                TABELA_PESQUISA,
                valores,
                "origem = ? AND destino IS NULL",
                new String[]{origem}
        );

        db.close();
        return linhasAfetadas > 0;
    }
    // Dentro da classe BancoHelper
    public boolean salvarOrigem(String origem) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put("origem", origem);

        long resultado = db.insert("pesquisas", null, valores);
        return resultado != -1;
    }


}

package pl2.g7.iamsi.stuffngo.Models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;


public class ProdutoBDHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "stuffngo";
    public static final int DB_VERSION = 1;
    public static final String TABLE_PRODUTOS = "produtos";
    public static final String ID = "idProduto", NOME = "nome", DESCRICAO = "descricao", PRECO = "preco_unit", IDCATEGORIA = "id_categoria", IMAGEM = "imagem";
    private final SQLiteDatabase db;

    public ProdutoBDHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE_PRODUTOS + " ( " +
                ID + " INTEGER PRIMARY KEY, " +
                NOME + " TEXT NOT NULL, " +
                DESCRICAO + " TEXT NOT NULL, " +
                PRECO + " DOUBLE(10,2) NOT NULL, " +
                IDCATEGORIA + " INTEGER NOT NULL, " +
                IMAGEM + " TEXT NOT NULL"+
                ");";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String sql = "DROP TABLE IF EXISTS " + TABLE_PRODUTOS + ";";
        db.execSQL(sql);
        this.onCreate(db);
    }

    public void removerAllProdutosBD() {
        db.delete(TABLE_PRODUTOS, null, null);
    }

    public Produto adicionarProdutoBD(Produto produto) {
        ContentValues values = new ContentValues();
        values.put(ID, produto.getId());
        values.put(NOME, produto.getNome());
        values.put(DESCRICAO, produto.getDescricao());
        values.put(PRECO, produto.getPreco_unit());
        values.put(IDCATEGORIA, produto.getId_categoria());
        values.put(IMAGEM, produto.getImagem());


        long id = db.insert(TABLE_PRODUTOS, null, values);
        if(id >= -1) {
            produto.setId((int) id);
            return produto;
        }
        return null;
    }

    public ArrayList<Produto> getAllProdutosBD() {
        ArrayList<Produto> produtos = new ArrayList<>();
        Cursor cursor = db.query(TABLE_PRODUTOS, new String[]{ID, NOME, DESCRICAO, PRECO, IDCATEGORIA, IMAGEM}, null, null, null, null, null);
        if(cursor.moveToFirst()) {
            do {
                Produto produto = new Produto(cursor.getInt(0), cursor.getInt(3), cursor.getInt(4), cursor.getString(1), cursor.getString(2), cursor.getString(5));
                produtos.add(produto);
            } while (cursor.moveToNext());
            cursor.close();
        }

        return produtos;
    }

}

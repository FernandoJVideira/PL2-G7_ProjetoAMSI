package pl2.g7.iamsi.stuffngo.Models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;


public class BDHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "stuffngo";
    public static final int DB_VERSION = 1;
    public static final String
            TABLE_PRODUTOS = "produtos",
            TABLE_FAVORITOS = "favoritos",
            TABLE_IVA = "iva",
            TABLE_CATEGORIAS = "categorias",
            TABLE_MORADAS = "moradas",
            TABLE_CARRINHO = "carrinho",
            TABLE_LINHA_CARRINHO = "linhacarrinho";

    public static final String
            IDPRODUTO = "idProduto",
            IDCARRINHO = "idCarrinho",
            IDCATEGORIA = "idCategoria",
            IDIVA = "idIva",
            IDFAVORITO = "idFavorito",
            IDLOJA = "idLoja",
            IDPROMOCAO = "idPromocao",
            IDLINHA_CARRINHO = "idLinhaCarrinho",
            IDMORADA = "idMorada";

    public static final String
            NOME = "nome",
            DESCRICAO = "descricao",
            PRECO = "preco_unit",
            IMAGEM = "imagem",
            VALOR = "valor",
            QUANTIDADE = "quantidade",
            DATA_CRIACAO = "data_criacao",
            RUA = "rua",
            COD_POSTAL = "cod_postal",
            CIDADE = "cidade",
            PAIS = "pais";




    private final SQLiteDatabase db;

    public BDHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE_PRODUTOS + " ( " +
                IDPRODUTO + " INTEGER PRIMARY KEY, " +
                NOME + " TEXT NOT NULL, " +
                DESCRICAO + " TEXT NOT NULL, " +
                PRECO + " DOUBLE(10,2) NOT NULL, " +
                IDCATEGORIA + " INTEGER NOT NULL, " +
                IMAGEM + " TEXT NOT NULL);";
        db.execSQL(sql);

        sql = "CREATE TABLE " + TABLE_FAVORITOS + " ( " +
                IDFAVORITO + " INTEGER PRIMARY KEY, " +
                IDPRODUTO + " INTEGER NOT NULL," +
                "FOREIGN KEY ("+IDPRODUTO+") REFERENCES "+TABLE_PRODUTOS+"("+IDPRODUTO+"));";
        db.execSQL(sql);

        sql = "CREATE TABLE " + TABLE_MORADAS + " (" +
                IDMORADA + " INTEGER NOT NULL," +
                RUA + " TEXT NOT NULL," +
                CIDADE + " TEXT NOT NULL," +
                COD_POSTAL + " TEXT NOT NULL," +
                PAIS + " TEXT NOT NULL," +
                "PRIMARY KEY ("+IDMORADA+"));";
        db.execSQL(sql);

        sql = "CREATE TABLE loja (" +
                "id INTEGER PRIMARY KEY," +
                "descricao TEXT NOT NULL," +
                "email TEXT NOT NULL," +
                "telefone TEXT NOT NULL," +
                "rua TEXT NOT NULL," +
                "cidade TEXT NOT NULL," +
                "cod_postal TEXT NOT NULL," +
                "pais TEXT NOT NULL" +
                ")";
        db.execSQL(sql);

        sql = "CREATE TABLE " + TABLE_CARRINHO + " (" +
                IDCARRINHO + " INTEGER NOT NULL," +
                DATA_CRIACAO + " TEXT NOT NULL," +
                IDMORADA + " INTEGER," +
                IDLOJA + " INTEGER," +
                IDPROMOCAO + " INTEGER," +
                "estado INTEGER(1) NOT NULL," +
                "subTotal INTEGER NOT NULL," +
                "iva INTEGER NOT NULL," +
                "desconto INTEGER NOT NULL," +
                "total INTEGER NOT NULL," +
                "PRIMARY KEY ("+IDCARRINHO+"),"+
                "FOREIGN KEY ("+IDMORADA+") REFERENCES "+TABLE_MORADAS+"("+IDMORADA+")," +
                "FOREIGN KEY ("+IDLOJA+") REFERENCES loja("+IDLOJA+"));";
        db.execSQL(sql);

        sql = "CREATE TABLE " + TABLE_LINHA_CARRINHO + " (" +
                IDLINHA_CARRINHO + " INTEGER PRIMARY KEY," +
                IDCARRINHO + " INTEGER NOT NULL," +
                IDPRODUTO + " INTEGER NOT NULL," +
                QUANTIDADE + " INTEGER NOT NULL," +
                "FOREIGN KEY ("+IDCARRINHO+") REFERENCES "+TABLE_CARRINHO+"("+IDCARRINHO+")," +
                "FOREIGN KEY ("+IDPRODUTO+") REFERENCES "+TABLE_PRODUTOS+"("+IDPRODUTO+"));";
        db.execSQL(sql);

        sql = "CREATE TABLE promocao (" +
                "id INTEGER PRIMARY KEY," +
                "nome_promo TEXT NOT NULL," +
                "codigo TEXT NOT NULL," +
                "data_limite TEXT NOT NULL," +
                "percentagem INTEGER NOT NULL" +
                ")";
        db.execSQL(sql);

        sql = "CREATE TABLE seccao (" +
                "id INTEGER PRIMARY KEY," +
                "nome TEXT NOT NULL," +
                "numeroActual INTEGER NOT NULL," +
                "ultimoNumero INTEGER NOT NULL" +
                ")";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String sql = "DROP TABLE IF EXISTS " + TABLE_PRODUTOS + ";";
        db.execSQL(sql);
        sql = "DROP TABLE IF EXISTS " + TABLE_FAVORITOS + ";";
        db.execSQL(sql);
        sql = "DROP TABLE IF EXISTS " + TABLE_IVA + ";";
        db.execSQL(sql);
        sql = "DROP TABLE IF EXISTS " + TABLE_CATEGORIAS + ";";
        db.execSQL(sql);
        sql = "DROP TABLE IF EXISTS " + TABLE_MORADAS + ";";
        db.execSQL(sql);
        sql = "DROP TABLE IF EXISTS loja;";
        db.execSQL(sql);
        sql = "DROP TABLE IF EXISTS " + TABLE_CARRINHO + ";";
        db.execSQL(sql);
        sql = "DROP TABLE IF EXISTS " + TABLE_LINHA_CARRINHO + ";";
        db.execSQL(sql);
        this.onCreate(db);
    }

    public void removerAllProdutosBD() {
        db.delete(TABLE_PRODUTOS, null, null);
    }

    public void removerAllCategoriasBD() {
        db.delete(TABLE_CATEGORIAS, null, null);
    }

    public void removerAllMoradasBD() {
        db.delete(TABLE_MORADAS, null, null);
    }

    public void removerAllLojasBD() {
        db.delete("loja", null, null);
    }

    public void removerAllCarrinhosBD() {
        db.delete(TABLE_CARRINHO, null, null);
    }

    public void removerAllLinhasCarrinhoBD(int idCarrinho) {
        db.delete(TABLE_LINHA_CARRINHO, IDCARRINHO + " = " + idCarrinho, null);
    }

    public void removerAllFavoritosBD() {
        db.delete(TABLE_FAVORITOS, null, null);
    }

    public void removerAllIvaBD() {
        db.delete(TABLE_IVA, null, null);
    }

    public void removerAllBD() {
        removerAllMoradasBD();
        removerAllLojasBD();
        removerAllFavoritosBD();
        removerAllProdutosBD();
        removerAllIvaBD();
        removerAllCategoriasBD();
    }

    public Produto adicionarProdutoBD(Produto produto) {
        ContentValues values = new ContentValues();
        values.put(IDPRODUTO, produto.getId());
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

    public Loja adicionarLojaBD(Loja loja){
        ContentValues values = new ContentValues();
        values.put("id", loja.getId());
        values.put("descricao", loja.getDescricao());
        values.put("email", loja.getEmail());
        values.put("telefone", loja.getTelefone());
        values.put(RUA, loja.getRua());
        values.put(CIDADE, loja.getCidade());
        values.put(COD_POSTAL, loja.getCod_postal());
        values.put(PAIS, loja.getPais());

        long id = db.insert("loja", null, values);
        if(id >= -1) {
            loja.setId((int) id);
            return loja;
        }
        return null;
    }

    public ArrayList<Loja> getAllLojasBD(){
        ArrayList<Loja> lojas = new ArrayList<>();
        String sql = "SELECT * FROM loja;";
        Cursor cursor = db.rawQuery(sql, null);
        if(cursor.moveToFirst()){
            do{
                Loja loja = new Loja(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7));
                lojas.add(loja);
            }while(cursor.moveToNext());
            cursor.close();
        }
        return lojas;
    }

    public ArrayList<Produto> getAllProdutosBD() {
        ArrayList<Produto> produtos = new ArrayList<>();
        Cursor cursor = db.query(TABLE_PRODUTOS, new String[]{IDPRODUTO, NOME, DESCRICAO, PRECO, IDCATEGORIA, IMAGEM}, null, null, null, null, null);
        if(cursor.moveToFirst()) {
            do {
                Produto produto = new Produto(cursor.getInt(0), cursor.getDouble(3), cursor.getInt(4), cursor.getString(1), cursor.getString(2), cursor.getString(5));
                produtos.add(produto);
            } while (cursor.moveToNext());
            cursor.close();
        }

        return produtos;
    }

    public Favorito adicionarFavoritoBD(Favorito favorito) {
        ContentValues values = new ContentValues();
        values.put(IDFAVORITO, favorito.getId());
        values.put(IDPRODUTO, favorito.getId_produto());

        long id = db.insert(TABLE_FAVORITOS, null, values);
        if(id >= -1) {
            favorito.setId((int) id);
            return favorito;
        }
        return null;
    }

    public void removerFavoritoBD(Produto produto) {
        db.delete(TABLE_FAVORITOS, IDPRODUTO + " = ?", new String[]{String.valueOf(produto.getId())});
    }

    public Carrinho getCarrinhoBD(){
        Carrinho carrinho = null;
        String sql = "SELECT * FROM " + TABLE_CARRINHO + ";";
        Cursor cursor = db.rawQuery(sql, null);
        if(cursor.moveToFirst()){
            //get linhas carrinho from that carrinho
            ArrayList<LinhaCarrinho> linhasCarrinho = getLinhasCarrinhoBD(cursor.getInt(0));
            carrinho = new Carrinho(cursor.getInt(0), cursor.getString(1), cursor.getInt(2), cursor.getInt(3), cursor.getInt(4),linhasCarrinho,  cursor.getInt(5) >= 1, cursor.getDouble(6), cursor.getDouble(7), cursor.getDouble(8), cursor.getDouble(9));
            return carrinho;
        }
        cursor.close();
        return null;
    }

    public void adicionarCarrinhoBD(Carrinho carrinho){
        if(getCarrinhoBD() != null){
           removerAllLinhasCarrinhoBD(getCarrinhoBD().getId());
            removerAllCarrinhosBD();
        }
        ContentValues values = new ContentValues();
        values.put(IDCARRINHO, carrinho.getId());
        values.put(DATA_CRIACAO, carrinho.getData());
        values.put(IDLOJA, carrinho.getIdLoja());
        values.put(IDMORADA, carrinho.getIdMorada());
        values.put(IDPROMOCAO, carrinho.getIdPromo());
        values.put("estado", carrinho.getEstado());
        values.put("subTotal", carrinho.getSubTotal());
        values.put("iva", carrinho.getIva());
        values.put("desconto", carrinho.getDesconto());
        values.put("total", carrinho.getTotal());
        db.insert(TABLE_CARRINHO, null, values);
        values = new ContentValues();
        for (LinhaCarrinho linhaCarrinho : carrinho.getLinhas()) {
            values.put(IDCARRINHO, carrinho.getId());
            values.put(IDPRODUTO, linhaCarrinho.getIdProduto());
            values.put("quantidade", linhaCarrinho.getQuantidade());
            db.insert(TABLE_LINHA_CARRINHO, null, values);
        }
    }

    public ArrayList<LinhaCarrinho> getLinhasCarrinhoBD(int id){
        ArrayList<LinhaCarrinho> linhasCarrinho = new ArrayList<>();
        String sql = "SELECT * FROM " + TABLE_LINHA_CARRINHO + " WHERE " + IDCARRINHO + " = " + id + ";";
        Cursor cursor = db.rawQuery(sql, null);
        if(cursor.moveToFirst()){
            do{
                LinhaCarrinho linhaCarrinho = new LinhaCarrinho(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2), cursor.getInt(3));
                linhasCarrinho.add(linhaCarrinho);
            }while(cursor.moveToNext());
            cursor.close();
        }
        return linhasCarrinho;
    }

    public ArrayList<Favorito> getAllFavoritosBD() {
        ArrayList<Favorito> favoritos = new ArrayList<>();
        String sql = "SELECT * FROM " + TABLE_FAVORITOS + ";";
        Cursor cursor = db.rawQuery(sql, null);
        if(cursor.moveToFirst()) {
            do {
                Favorito favorito = new Favorito(cursor.getInt(0), cursor.getInt(1));
                favoritos.add(favorito);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return favoritos;
    }
}

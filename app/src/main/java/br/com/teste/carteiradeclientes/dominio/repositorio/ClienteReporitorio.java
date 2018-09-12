package br.com.teste.carteiradeclientes.dominio.repositorio;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import br.com.teste.carteiradeclientes.dominio.entidades.Cliente;

public class ClienteReporitorio {

    private SQLiteDatabase sqLiteDatabase;

    public ClienteReporitorio(SQLiteDatabase sqLiteDatabase) {
        this.sqLiteDatabase = sqLiteDatabase;
    }

    public void inserir(Cliente cliente){

        ContentValues contentValues = new ContentValues();
        contentValues.put("NOME", cliente.nome);
        contentValues.put("ENDERECO", cliente.endereco);
        contentValues.put("EMAIL", cliente.email);
        contentValues.put("TELEFONE", cliente.telefone);

        this.sqLiteDatabase.insertOrThrow("CLIENTE", null, contentValues);

    }

    public void excluir(int codigo){

        String[] parametros = new String[1];
        parametros[0] = String.valueOf(codigo);

        this.sqLiteDatabase.delete("CLIENTE","CODIGO = ? ", parametros);
    }


    public void alterar(Cliente cliente){

        ContentValues contentValues = new ContentValues();
        contentValues.put("NOME", cliente.nome);
        contentValues.put("ENDERECO", cliente.endereco);
        contentValues.put("EMAIL", cliente.email);
        contentValues.put("TELEFONE", cliente.telefone);

        String[] parametros = new String[1];
        parametros[0] = String.valueOf(cliente.codigo);

        this.sqLiteDatabase.update("CLIENTE", contentValues, "CODIGO = ? ", parametros);

    }

    public List<Cliente> getAllClientes(){

        List<Cliente> clientes = new ArrayList<Cliente>();

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT CODIGO,NOME,ENDERECO,EMAIL,TELEFONE ");
        sql.append(" FROM CLIENTE ");

        Cursor resultado =  sqLiteDatabase.rawQuery(sql.toString(), null);


        if (resultado.getCount() > 0){

            resultado.moveToFirst();

            do{

                Cliente cli = new Cliente();

                cli.codigo    = resultado.getInt(resultado.getColumnIndexOrThrow("CODIGO"));
                cli.nome      = resultado.getString(resultado.getColumnIndexOrThrow("NOME"));
                cli.endereco  = resultado.getString(resultado.getColumnIndexOrThrow("ENDERECO"));
                cli.email     = resultado.getString(resultado.getColumnIndexOrThrow("EMAIL"));
                cli.telefone  = resultado.getString(resultado.getColumnIndexOrThrow("TELEFONE"));

                clientes.add(cli);

            }while (resultado.moveToFirst());
        }

        return clientes;
    }

    public Cliente getClienteByCodigo(int codigo){

        Cliente cliente = new Cliente();

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT CODIGO,NOME,ENDERECO,EMAIL,TELEFONE ");
        sql.append(" FROM CLIENTE ");
        sql.append("WHERE CODIGO = ? ");

        String[] parametros = new String[1];
        parametros[0] = String.valueOf(codigo);

        Cursor resultado =  sqLiteDatabase.rawQuery(sql.toString(), parametros);

        if (resultado.getCount() > 0){

            resultado.moveToFirst();


            cliente.codigo    = resultado.getInt(resultado.getColumnIndexOrThrow("CODIGO"));
            cliente.nome      = resultado.getString(resultado.getColumnIndexOrThrow("NOME"));
            cliente.endereco  = resultado.getString(resultado.getColumnIndexOrThrow("ENDERECO"));
            cliente.email     = resultado.getString(resultado.getColumnIndexOrThrow("EMAIL"));
            cliente.telefone  = resultado.getString(resultado.getColumnIndexOrThrow("TELEFONE"));

            return cliente;
        }

        return null;
    }
}
